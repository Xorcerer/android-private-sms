package com.anibug.smsmanager.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable {
    public static String INTENT_KEY = "session";

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Session createFromParcel(Parcel in) {
                    return new Session(in);
                }

                public Session[] newArray(int size) {
                    return new Session[size];
                }
            };

    private ContentDisplayMode displayMode = ContentDisplayMode.NORMAL;

    public ContentDisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(ContentDisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public Session() {
    }

    public Session(Parcel in) {
        displayMode = ContentDisplayMode.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayMode.toString());
    }
}
