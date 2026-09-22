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
}
