package co.wethinkcode.trafficflow;

public class IntersectionCleaner {

    public String cleanId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return id.trim().toUpperCase();
    }

    public String cleanDistrict(String district) {

        // Treat blank or placeholder district values as missing data.
        if (isMissingValue(district)) {
            return null;
        }

        String cleaned = district.trim().toLowerCase();

        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1);
    }

    public String cleanSignalType(String signalType) {

        // Treat blank or "unknown" signal types as missing data.
        if (isMissingValue(signalType)) {
            return null;
        }

        return signalType.trim().toLowerCase();
    }

    public Boolean cleanActiveFlag(String activeFlag) {

        // Treat blank or "unknown" active flags as missing data.
        if (isMissingValue(activeFlag)) {
            return null;
        }

        String cleaned = activeFlag.trim().toLowerCase();

        if (cleaned.equals("y")
                || cleaned.equals("yes")
                || cleaned.equals("true")
                || cleaned.equals("1")) {
            return true;
        }

        if (cleaned.equals("n")
                || cleaned.equals("no")
                || cleaned.equals("false")
                || cleaned.equals("0")) {
            return false;
        }

        return null;
    }

    // Reuse one rule for values that should be treated as missing.
    private boolean isMissingValue(String value) {
        if (value == null) {
            return true;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty()
                || cleaned.equalsIgnoreCase("unknown")
                || cleaned.equalsIgnoreCase("n/a")
                || cleaned.equalsIgnoreCase("tbd")
                || cleaned.equals("-")
                || cleaned.equalsIgnoreCase("nan");
    }
}
