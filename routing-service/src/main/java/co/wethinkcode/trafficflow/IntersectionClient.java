package co.wethinkcode.trafficflow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IntersectionClient {

    private static final String INTERSECTION_URL =
            "http://localhost:7021/intersections/";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean intersectionExists(String id) {

        // Build a request to validate the intersection through intersection-service.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(INTERSECTION_URL + id))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // A 200 response means the intersection exists.
            if (response.statusCode() == 200) {
                return true;
            }

            // A 404 response means the requested intersection does not exist.
            if (response.statusCode() == 404) {
                return false;
            }

            throw new RuntimeException(
                    "Intersection service returned status " + response.statusCode()
            );

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(
                    "Could not communicate with intersection service", e
            );
        }
    }
}
