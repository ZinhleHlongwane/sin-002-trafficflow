package co.wethinkcode.trafficflow;

import io.javalin.Javalin;

public class CongestionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7022);

        // Stores the current city-wide congestion level in memory.
        int[] congestionLevel = {0};

        CongestionPublisher congestionPublisher = new CongestionPublisher();

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO (Tracks the city-wide Congestion Level (0-8).)
        // Add domain endpoints for congestion-service here.

        // Return the current congestion level.
        app.get("/congestion", ctx -> {
            ctx.json(congestionLevel[0]);
        });

        // Update the congestion level and publish the change to ActiveMQ.
        app.put("/congestion/{level}", ctx -> {
            int level;

            try {
                level = Integer.parseInt(ctx.pathParam("level"));
            } catch (NumberFormatException e) {
                ctx.status(400).result("Congestion level must be a number between 0 and 8");
                return;
            }

            if (level < 0 || level > 8) {
                ctx.status(400).result("Congestion level must be between 0 and 8");
                return;
            }

            congestionLevel[0] = level;
            congestionPublisher.publish(level);

            ctx.result("Congestion level updated to " + level);
        });
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.trafficflow.mq.MqConfig)
