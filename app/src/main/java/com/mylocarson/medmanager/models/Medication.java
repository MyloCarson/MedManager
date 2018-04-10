package com.mylocarson.medmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 05/04/2018.
 */

@SuppressWarnings("ALL")
public class Medication extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private String description;
    private String dateCreated;

    public Medication() {
    }

    public Medication(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.dateCreated);
    }

    protected Medication(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.dateCreated = in.readString();
    }

    public static final Parcelable.Creator<Medication> CREATOR = new Parcelable.Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel source) {
            return new Medication(source);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };
}
