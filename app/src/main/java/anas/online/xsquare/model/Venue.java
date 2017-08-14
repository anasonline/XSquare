package anas.online.xsquare.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anas on 12.08.17.
 */

public class Venue implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };
    private String id;
    private String name;
    private String address;
    private String distance;

    public Venue(String id, String name, String address, String distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    protected Venue(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        distance = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(distance);
    }
}