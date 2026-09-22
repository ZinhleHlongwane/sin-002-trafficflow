package co.wethinkcode.trafficflow;

import io.javalin.Javalin;

public class IngestionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7020);

        // Reads and cleans the legacy intersection data for the API.
        IntersectionCsvReader reader = new IntersectionCsvReader();

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/intersections-legacy.csv (intersections, districts, signal types data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.

        // Expose the cleaned intersection data as JSON for other services to consume.
        app.get("/intersections", ctx -> {
            ctx.json(reader.readIntersections());
        });
    }
}
