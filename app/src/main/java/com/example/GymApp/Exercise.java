package com.example.GymApp;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name;
    private String category;
    private String equipment;
    private int photo;

    public Exercise(String name, String category, String equipment, int photo) {
        this.name = name;
        this.category = category;
        this.equipment = equipment;
        this.photo = photo;
    }

    // Parcelable implementation
    protected Exercise(Parcel in) {
        name = in.readString();
        category = in.readString();
        equipment = in.readString();
        photo = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(equipment);
        dest.writeInt(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
