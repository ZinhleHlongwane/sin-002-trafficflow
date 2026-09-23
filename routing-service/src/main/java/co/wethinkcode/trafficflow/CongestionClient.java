package co.wethinkcode.trafficflow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CongestionClient {

    private static final String CONGESTION_URL =
            "http://localhost:7022/congestion";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public int getCongestionLevel() {

        // Build a request to read the current congestion level.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CONGESTION_URL))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Congestion service returned status " + response.statusCode()
                );
            }

            return Integer.parseInt(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(
                    "Could not communicate with congestion service", e
            );

        }
    }
}
