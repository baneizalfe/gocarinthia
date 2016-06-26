package com.baneizalfe.gocarinthia.stations;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by baneizalfe on 6/25/16.
 */
@Table(name = "stations")
public class Station extends Model implements Parcelable {

    @Column(name = "station_id", index = true)
    public long station_id;

    @Column(name = "latitude")
    public double latitude;

    @Column(name = "longitude")
    public double longitude;

    @Column(name = "station_name")
    public String station_name;

    public double distance;

    public Station() {
        super();
    }

    public double distanceTo(double userLat, double userLon) {
        return Haversine.distance(this.latitude, this.longitude, userLat, userLon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.station_id);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.station_name);
        dest.writeDouble(this.distance);
    }

    protected Station(Parcel in) {
        this.station_id = in.readLong();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.station_name = in.readString();
        this.distance = in.readDouble();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
