/*
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 *
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
 * This file is part of HQ.
 *
 * HQ is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package org.hyperic.hq.product.jmx;

import org.hyperic.hq.product.LiveDataPlugin;
import org.hyperic.hq.product.PluginException;
import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.config.ConfigSchema;
import org.hyperic.util.config.StringConfigOption;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Properties;

public class MxLiveDataPlugin extends LiveDataPlugin {

    private static Log _log = LogFactory.getLog(MxLiveDataPlugin.class);

    private static final String PROP_OBJNAME = "ObjectName";
    private static final String PROP_METHOD  = "method";

    private static final String CMD_GET    = "get";
    private static final String CMD_INVOKE = "invoke";

    private static final String[] _COMMANDS = {
        CMD_GET,
        CMD_INVOKE
    };

    public Object getData(String command, ConfigResponse config)
        throws PluginException
    {
        Properties props = config.toProperties();
        _log.info("Using properties: " + props);

        try {
            MBeanServerConnection mBeanServer = MxUtil.getMBeanServer(props);
            ObjectName oName = new ObjectName(props.getProperty(PROP_OBJNAME));
            String method = props.getProperty(PROP_METHOD);

            Object res;
            if (command.equals(CMD_INVOKE)) {
                res = mBeanServer.invoke(oName, method, new Object[0],
                                         new String[0]);
            } else if (command.equals(CMD_GET)) {
                res = mBeanServer.getAttribute(oName, method);
            } else {
                throw new PluginException("Unkown command " + command);
            }

            return res;

        } catch (Exception e) {
            throw new PluginException(e);
        }
    }

    public String[] getCommands() {
        return _COMMANDS;
    }

    public ConfigSchema getConfigSchema() {
        ConfigSchema schema = new ConfigSchema();

        StringConfigOption object =
            new StringConfigOption(PROP_OBJNAME, "Object name");
        StringConfigOption method =
            new StringConfigOption(PROP_METHOD, "Method to invoke");

        schema.addOption(object);
        schema.addOption(method);

        return schema;
    }
}
