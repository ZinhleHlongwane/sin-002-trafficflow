package co.wethinkcode.trafficflow;

import io.javalin.Javalin;

public class RoutingServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7023);

        IntersectionClient intersectionClient = new IntersectionClient();

        CongestionSubscriber congestionSubscriber = new CongestionSubscriber();
        congestionSubscriber.start();

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO (Provides estimated travel times based on congestion and intersection.)
        // Add domain endpoints for routing-service here.

        // Estimate travel time using a valid intersection and current congestion level.
        app.get("/route/{intersectionId}", ctx -> {
            String intersectionId =
                    ctx.pathParam("intersectionId").trim().toUpperCase();

            boolean intersectionExists;

            try {
                intersectionExists =
                        intersectionClient.intersectionExists(intersectionId);
            } catch (RuntimeException e) {
                ctx.status(503).result("Intersection service unavailable");
                return;
            }

            if (!intersectionExists) {
                ctx.status(404).result("Intersection not found");
                return;
            }

            int congestionLevel =
                    congestionSubscriber.getLatestCongestionLevel();

            int baseTravelTime = 10;
            int estimatedTravelTime =
                    baseTravelTime + (congestionLevel * 2);

            ctx.result(
                    "Estimated travel time for "
                    + intersectionId
                    + ": "
                    + estimatedTravelTime
                    + " minutes"
            );
        });
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.trafficflow.mq.MqConfig)
