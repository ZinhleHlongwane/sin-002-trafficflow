package co.wethinkcode.trafficflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class IngestionClient {

    private static final String INGESTION_URL =
            "http://localhost:7020/intersections";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Intersection> getIntersections() {

        // Build a GET request to the ingestion service.
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(INGESTION_URL)).GET().build();

        try {
            // Send the request and receive the JSON response as a String.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Stop if the ingestion service returns an unsuccessful response.
            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Ingestion service returned status " + response.statusCode()
                );
            }

            // Convert the JSON array into a list of Intersection objects.
            return objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<Intersection>>() {}
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(
                    "Could not communicate with ingestion service", e
            );
        }
    }
}
