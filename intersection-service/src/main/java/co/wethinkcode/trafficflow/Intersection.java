package co.wethinkcode.trafficflow;

public class Intersection {

    private String id;
    private String district;
    private String signalType;
    private Boolean active;

    public Intersection() {
        // Empty constructor used when JSON is converted into a Java object.
    }

    public String getId() {
        return id;
    }

    public String getDistrict() {
        return district;
    }

    public String getSignalType() {
        return signalType;
    }

    public Boolean getActive() {
        return active;
    }
}
