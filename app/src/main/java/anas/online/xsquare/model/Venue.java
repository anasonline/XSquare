package anas.online.xsquare.model;

/**
 * Created by anas on 12.08.17.
 */

public class Venue {
    private String id;
    private String name;
    private String address;
    private String distance;
    private int photoResourceId;

    public Venue(String id, String name, String address, String distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }

}
