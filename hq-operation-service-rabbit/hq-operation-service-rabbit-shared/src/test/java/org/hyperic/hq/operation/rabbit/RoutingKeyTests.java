package org.hyperic.hq.operation.rabbit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.operation.rabbit.mapping.RoutingKeys;
import org.hyperic.hq.operation.rabbit.mapping.Routings;
import org.hyperic.util.security.SecurityUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.*;

/** 
 * @author Helena Edelson
 */
public class RoutingKeyTests {

    protected final Log logger = LogFactory.getLog(this.getClass().getName());

    protected Routings routings = new Routings();

    protected RoutingKeys routingKeys = new RoutingKeys();
    
    protected final int agents = 1000;
 
    @Before
    public void prepare() throws UnknownHostException {
        logger.debug("Created routing keys for " + routingKeys.getAgentOperations().length
                + " agent and " + routingKeys.getServerOperations().length + " server operations");
    }

    /**
     * Each agent will generate a  unique set of routing keys in order to bind
     * it's exchange to the existing agent exchanges which will route to their respective queues
     * and eventually route messages to the server and to bind its queue to the existing
     * server exchanges
     * @throws UnsupportedEncodingException
     */
    @Test  @Ignore("Long-running test not suitable for CI of entire HQ build")
    public void agentRoutingKeys() throws UnsupportedEncodingException {
        for (int count = 0; count < agents; count++) {
            String agentToken = SecurityUtil.generateRandomToken();

            List<String> keys = routings.createAgentOperationRoutingKeys(agentToken);

            for (String key : keys) { 
                testKey(key);
            }
        }
    }

    @Test
    public void serverRoutingKeys() {
        for (String key : routings.createServerOperationRoutingKeys()) {
            testKey(key);
        }
    }

    /**
     * TODO improve these assertion tests for accuracy
     * @param routingKey
     * @throws UnsupportedEncodingException
     */
    private void testKey(String routingKey) {
        try {
            assertTrue(routingKey.getBytes("UTF8").length <= 255);
            assertTrue(routingKey.getBytes("UTF16").length <= 255);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
    }

    /**
     * What is actually returned from AgentDao is indeed unique as it
     * checks agaist existing agent tokens for uniqueness before returning
     * a token String.
     */
    @Test
    public void agentTokenDuplication() {
        int failures = 0;
        String agentToken = null;

        for (int count = 0; count < agents; count++) {
            String tmp = SecurityUtil.generateRandomToken();

            if (tmp.equalsIgnoreCase(agentToken)) {
                logger.debug("Generated agent token already exists. Attempt=" + count);
                failures++;
            } else {
                assertFalse(tmp.equalsIgnoreCase(agentToken));
                agentToken = tmp;
                assertEquals(agentToken.length(), agentToken.getBytes().length);
            }
        }
        logger.debug(failures + " failures out of " + agents + " attempts");
    }
}
