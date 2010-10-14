/**
 * NOTE: This copyright does *not* cover user programs that use Hyperic
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 *  "derived work".
 *
 *  Copyright (C) [2010], VMware, Inc.
 *  This file is part of Hyperic.
 *
 *  Hyperic is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */
package org.hyperic.hq.measurement.server.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.authz.server.session.Resource;
import org.hyperic.hq.authz.shared.AuthzConstants;
import org.hyperic.hq.authz.shared.PermissionManager;
import org.hyperic.hq.measurement.MeasurementConstants;
import org.hyperic.hq.measurement.TimingVoodoo;
import org.hyperic.hq.measurement.shared.AvailabilityManager;
import org.hyperic.hq.product.MetricValue;
import org.hyperic.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link BackfillPointsService} Code was extracted
 * unmodified from AvailabilityCheckServiceImpl to allow for that class to add
 * data points in separate transactions
 * @author jhickey
 * 
 */
@Transactional(readOnly = true)
@Service
public class BackfillPointsServiceImpl implements BackfillPointsService {

    private static final double AVAIL_DOWN = MeasurementConstants.AVAIL_DOWN;
    private static final double AVAIL_PAUSED = MeasurementConstants.AVAIL_PAUSED;
    private static final double AVAIL_NULL = MeasurementConstants.AVAIL_NULL;
    private final Log log = LogFactory.getLog(BackfillPointsServiceImpl.class);
    private AvailabilityManager availabilityManager;
    private PermissionManager permissionManager;
    private AvailabilityCache availabilityCache;

    @Autowired
    public BackfillPointsServiceImpl(AvailabilityManager availabilityManager,
                                     PermissionManager permissionManager,
                                     AvailabilityCache availabilityCache) {
        this.availabilityManager = availabilityManager;
        this.permissionManager = permissionManager;
        this.availabilityCache = availabilityCache;
    }

    public Map<Integer, DataPoint> getBackfillPoints(long current) {
        Map<Integer, ResourceDataPoint> downPlatforms = getDownPlatforms(current);
        return getBackfillPts(downPlatforms, current);
    }

  
    private Map<Integer, ResourceDataPoint> getDownPlatforms(long timeInMillis) {
        final boolean debug = log.isDebugEnabled();
        final List<Measurement> platformResources = availabilityManager.getPlatformResources();
        final long now = TimingVoodoo.roundDownTime(timeInMillis, MeasurementConstants.MINUTE);
        final String nowTimestamp = TimeUtil.toString(now);
        final Map<Integer, ResourceDataPoint> rtn = new HashMap<Integer, ResourceDataPoint>(
            platformResources.size());
        Resource resource = null;
        synchronized (availabilityCache) {
            for (final Measurement meas : platformResources) {
                final long interval = meas.getInterval();
                final long end = getEndWindow(now, meas);
                final long begin = getBeginWindow(end, meas);
                final DataPoint defaultPt = new DataPoint(meas.getId().intValue(), AVAIL_NULL, end);
                final DataPoint last = availabilityCache.get(meas.getId(), defaultPt);
                final long lastTimestamp = last.getTimestamp();
                if (debug) {
                    String msg = "Checking availability for " + last + ", CacheValue=(" +
                                 TimeUtil.toString(lastTimestamp) + ") vs. Now=(" + nowTimestamp +
                                 ")";
                    log.debug(msg);
                }
                if (begin > end) {
                    // this represents the scenario where the measurement mtime
                    // was modified recently and therefore we need to wait
                    // another interval
                    continue;
                }
                if (!meas.isEnabled()) {
                    final long t = TimingVoodoo.roundDownTime(now - interval, interval);
                    final DataPoint point = new DataPoint(meas.getId(), new MetricValue(
                        AVAIL_PAUSED, t));
                    resource = meas.getResource();
                    rtn.put(resource.getId(), new ResourceDataPoint(resource, point));
                } else if (last.getValue() == AVAIL_DOWN || (now - lastTimestamp) > interval * 2) {
                    // HQ-1664: This is a hack: Give a 5 minute grace period for
                    // the agent and HQ
                    // to sync up if a resource was recently part of a downtime
                    // window
                    if (last.getValue() == AVAIL_PAUSED && (now - lastTimestamp) <= 5 * 60 * 1000) {
                        continue;
                    }
                    long t = (last.getValue() != AVAIL_DOWN) ? lastTimestamp + interval
                                                            : TimingVoodoo.roundDownTime(now -
                                                                                         interval,
                                                                interval);
                    t = (last.getValue() == AVAIL_PAUSED) ? TimingVoodoo.roundDownTime(now,
                        interval) : t;
                    DataPoint point = new DataPoint(meas.getId(), new MetricValue(AVAIL_DOWN, t));
                    resource = meas.getResource();
                    rtn.put(resource.getId(), new ResourceDataPoint(resource, point));
                }
            }
        }
        if (!rtn.isEmpty()) {
            permissionManager.getHierarchicalAlertingManager().performSecondaryAvailabilityCheck(
                rtn);
        }
        return rtn;
    }

    private long getBeginWindow(long end, Measurement meas) {
        final long interval = 0;
        final long wait = 5 * MeasurementConstants.MINUTE;
        long measInterval = meas.getInterval();

        // We have to get at least the measurement interval
        long maxInterval = Math.max(Math.max(interval, wait), measInterval);

        // Begin is maximum of interval or measurement create time
        long begin = Math.max(end - maxInterval, meas.getMtime() + measInterval);
        return TimingVoodoo.roundDownTime(begin, measInterval);
    }

    // End is at least more than 1 interval away
    private long getEndWindow(long current, Measurement meas) {
        return TimingVoodoo.roundDownTime((current - meas.getInterval()), meas.getInterval());
    }

    private Map<Integer, DataPoint> getBackfillPts(Map<Integer, ResourceDataPoint> downPlatforms,
                                                   long current) {
        final boolean debug = log.isDebugEnabled();
        final Map<Integer, DataPoint> rtn = new HashMap<Integer, DataPoint>();
        final List<Integer> resourceIds = new ArrayList<Integer>(downPlatforms.keySet());
        final Map<Integer, List<Measurement>> rHierarchy = availabilityManager
            .getAvailMeasurementChildren(resourceIds,
                AuthzConstants.ResourceEdgeContainmentRelation);
        for (ResourceDataPoint rdp : downPlatforms.values()) {
            final Resource platform = rdp.getResource();
            if (debug) {
                log.debug(new StringBuilder(256).append("platform name=")
                    .append(platform.getName()).append(", resourceid=").append(platform.getId())
                    .append(", measurementid=").append(rdp.getMetricId()).append(
                        " is being marked ").append(rdp.getValue()).append(" with timestamp = ")
                    .append(TimeUtil.toString(rdp.getTimestamp())).toString());
            }
            rtn.put(platform.getId(), rdp);
            if (rdp.getValue() != AVAIL_DOWN) {
                // platform may be paused, so skip pausing its children
                continue;
            }
            final List<Measurement> associatedResources = rHierarchy.get(platform.getId());
            if (associatedResources == null) {
                continue;
            }
            if (debug) {
                log.debug("platform [resource id " + platform.getId() + "] has " +
                          associatedResources.size() + " associated resources");
            }
            for (Measurement meas : associatedResources) {
                if (!meas.isEnabled()) {
                    continue;
                }
                final long end = getEndWindow(current, meas);
                final DataPoint defaultPt = new DataPoint(meas.getId().intValue(), AVAIL_NULL, end);
                final DataPoint lastPt = availabilityCache.get(meas.getId(), defaultPt);
                final long backfillTime = lastPt.getTimestamp() + meas.getInterval();
                if (backfillTime > current) {
                    continue;
                }
                if (debug) {
                    log.debug("measurement id " + meas.getId() + " is being marked down, time=" +
                              backfillTime);
                }
                final MetricValue val = new MetricValue(AVAIL_DOWN, backfillTime);
                final MeasDataPoint point = new MeasDataPoint(meas.getId(), val, meas.getTemplate()
                    .isAvailability());
                rtn.put(meas.getResource().getId(), point);
            }
        }
        return rtn;
    }

}