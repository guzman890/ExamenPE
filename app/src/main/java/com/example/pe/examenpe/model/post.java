package com.example.pe.examenpe.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Home-User on 17/07/2017.
 */

public class post {
    @SerializedName("response")
    @Expose
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    public String toString() {
        return  "Respuesta :"+response;
    }
}
