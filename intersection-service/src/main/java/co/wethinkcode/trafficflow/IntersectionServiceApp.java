package co.wethinkcode.trafficflow;

import io.javalin.Javalin;

public class IntersectionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7021);

        // Connects this service to the cleaned intersection data from ingestion-service
        IngestionClient ingestionClient = new IngestionClient();

        HeartbeatPublisher heartbeatPublisher = new HeartbeatPublisher();

        Thread heartbeatThread = new Thread(() -> {
            while (true) {
                heartbeatPublisher.publish();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        heartbeatThread.setDaemon(true);
        heartbeatThread.start();

        app.get("/health", ctx -> ctx.result("OK"));

        // Look up a specific intersection by ID.
        app.get("/intersections/{id}", ctx -> {
            String requestedId = ctx.pathParam("id").trim().toUpperCase();

            Intersection intersection = ingestionClient.getIntersections()
                    .stream()
                    .filter(item -> item.getId().equals(requestedId))
                    .findFirst()
                    .orElse(null);

            if (intersection == null) {
                ctx.status(404).result("Intersection not found");
                return;
            }

            ctx.json(intersection);
        });

        // TODO (Validates intersection/district names (source of truth).)
        // Add domain endpoints for intersection-service here.
    }
}

// MQ TODO: publishes a periodic heartbeat to ActiveMQ queue MqConfig.HEARTBEAT_QUEUE at
// MqConfig.BROKER_URL (see co.wethinkcode.trafficflow.mq.MqConfig), consumed by intersection-watchdog.
