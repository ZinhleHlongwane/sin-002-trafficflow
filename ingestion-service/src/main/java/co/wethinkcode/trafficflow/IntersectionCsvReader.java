package co.wethinkcode.trafficflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IntersectionCsvReader {

    private final IntersectionCleaner cleaner = new IntersectionCleaner();

    public List<Intersection> readIntersections() {
        List<Intersection> intersections = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("intersections-legacy.csv");

        if (inputStream == null) {
            throw new RuntimeException("Could not find intersections-legacy.csv");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            // Skip the CSV header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1);

                if (values.length < 4) {
                    continue;
                }

                String id = cleaner.cleanId(values[0]);
                String district = cleaner.cleanDistrict(values[1]);
                String signalType = cleaner.cleanSignalType(values[2]);
                Boolean active = cleaner.cleanActiveFlag(values[3]);

                Intersection intersection = new Intersection(id, district, signalType, active);

                intersections.add(intersection);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not read intersections CSV", e);
        }

        return intersections;
    }
}
