package com.example.ketri.dogsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ketri on 25.11.2018.
 */

public class DogListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private List<String> message;

    public String getStatus() {
        return status;
    }

    public List<String> getMessage() {
        return message;
    }
}
