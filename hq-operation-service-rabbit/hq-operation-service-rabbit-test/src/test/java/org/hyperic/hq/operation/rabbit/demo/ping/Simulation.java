package org.hyperic.hq.operation.rabbit.demo.ping;

import java.io.IOException;

/**
 * @author Helena Edelson
 */
public class Simulation {

    public static void main(String[] args) throws InterruptedException, IOException {

        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Server().receiveAndReply();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        Thread agent = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Agent agent = new Agent();
                    long duration = agent.ping(5);
                    System.out.println("duration="+duration);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        agent.start();
        server.start();
        Thread.sleep(1000);
        System.exit(0);
    }
}
