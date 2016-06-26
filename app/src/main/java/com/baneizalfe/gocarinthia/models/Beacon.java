package com.baneizalfe.gocarinthia.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baneizalfe on 6/26/16.
 */
@Table(name = "beacons")
public class Beacon extends Model implements Parcelable {

    public static final String TYPE_BUS = "BUS";
    public static final String TYPE_BIKE = "BIKE";
    public static final String TYPE_TRAIN = "TRAIN";
    public static final String TYPE_CAR = "CAR";

    @SerializedName("Name")
    public String name;

    @SerializedName("Identifier")
    public String identifier;

    @SerializedName("TransportType")
    public String transportType;

    public Beacon(String name, String identifier, String transportType) {
        this.name = name;
        this.identifier = identifier;
        this.transportType = transportType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.identifier);
        dest.writeString(this.transportType);
    }

    public Beacon() {
    }

    protected Beacon(Parcel in) {
        this.name = in.readString();
        this.identifier = in.readString();
        this.transportType = in.readString();
    }

    public static final Creator<Beacon> CREATOR = new Creator<Beacon>() {
        @Override
        public Beacon createFromParcel(Parcel source) {
            return new Beacon(source);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };
}
