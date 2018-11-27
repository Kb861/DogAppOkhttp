package com.example.ketri.dogsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ketri on 25.11.2018.
 */

public class BreedPhoto {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
