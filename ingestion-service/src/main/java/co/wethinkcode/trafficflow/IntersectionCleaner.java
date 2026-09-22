package co.wethinkcode.trafficflow;

public class IntersectionCleaner {

    public String cleanId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return id.trim().toUpperCase();
    }

    public String cleanDistrict(String district) {
        if (district == null || district.trim().isEmpty()) {
            return null;
        }

        String cleaned = district.trim().toLowerCase();

        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1);
    }

    public String cleanSignalType(String signalType) {
        if (signalType == null || signalType.trim().isEmpty()) {
            return null;
        }

        String cleaned = signalType.trim().toLowerCase();

        if (cleaned.equals("unknown")) {
            return null;
        }

        return cleaned;
    }

    public Boolean cleanActiveFlag(String activeFlag) {
        if (activeFlag == null || activeFlag.trim().isEmpty()) {
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
}
