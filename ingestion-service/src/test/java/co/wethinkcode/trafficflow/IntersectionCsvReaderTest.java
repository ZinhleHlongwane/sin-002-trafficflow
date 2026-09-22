package co.wethinkcode.trafficflow;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntersectionCsvReaderTest {

    @Test
    void shouldReadIntersectionsFromCsv() {
        IntersectionCsvReader reader = new IntersectionCsvReader();

        List<Intersection> intersections = reader.readIntersections();

        assertNotNull(intersections);
        assertFalse(intersections.isEmpty());
    }

    @Test
    void shouldCleanIntersectionDataFromCsv() {
        IntersectionCsvReader reader = new IntersectionCsvReader();

        List<Intersection> intersections = reader.readIntersections();

        Intersection firstIntersection = intersections.get(0);

        assertEquals("INT-1001", firstIntersection.getId());
        assertEquals("Downtown", firstIntersection.getDistrict());
        assertEquals("4-way", firstIntersection.getSignalType());
        assertTrue(firstIntersection.getActive());
    }

    @Test
    void shouldRemoveDuplicateIntersectionIds() {
        IntersectionCsvReader reader = new IntersectionCsvReader();

        List<Intersection> intersections = reader.readIntersections();

        long count = intersections.stream()
                .filter(intersection -> intersection.getId().equals("INT-1005"))
                .count();

        assertEquals(1, count);
    }

    @Test
    void shouldHandleMissingAndUnknownValues() {
        IntersectionCsvReader reader = new IntersectionCsvReader();

        List<Intersection> intersections = reader.readIntersections();

        // INT-1007 has a blank signal type in the CSV.
        Intersection missingSignal = intersections.stream()
                .filter(intersection -> intersection.getId().equals("INT-1007"))
                .findFirst()
                .orElseThrow();

        // INT-1013 uses "unknown" for both signal type and active flag.
        Intersection unknownValues = intersections.stream()
                .filter(intersection -> intersection.getId().equals("INT-1013"))
                .findFirst()
                .orElseThrow();

        // INT-1015 has a blank district in the CSV.
        Intersection missingDistrict = intersections.stream()
                .filter(intersection -> intersection.getId().equals("INT-1015"))
                .findFirst()
                .orElseThrow();

        assertNull(missingSignal.getSignalType());

        assertNull(unknownValues.getSignalType());
        assertNull(unknownValues.getActive());

        assertNull(missingDistrict.getDistrict());
    }
}
