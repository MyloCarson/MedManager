package com.mylocarson.medmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 05/04/2018.
 */

public class Reminder extends RealmObject implements Parcelable {


    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String medicationName;
    private String medicationID;
    private int frequency;
    private String startDate;
    private String endDate;
    private String startTime;



    public Reminder() {
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id='" + id + '\'' +
                ", medicationName='" + medicationName + '\'' +
                ", medicationID='" + medicationID + '\'' +
                ", frequency=" + frequency +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.medicationName);
        dest.writeString(this.medicationID);
        dest.writeInt(this.frequency);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.startTime);
    }

    protected Reminder(Parcel in) {
        this.id = in.readString();
        this.medicationName = in.readString();
        this.medicationID = in.readString();
        this.frequency = in.readInt();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.startTime = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
