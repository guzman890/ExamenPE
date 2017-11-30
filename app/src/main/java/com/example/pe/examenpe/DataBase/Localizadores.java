package com.example.pe.examenpe.DataBase;

import android.content.ContentValues;

import java.util.UUID;

/**
 * Created by pbl_8 on 29/11/2017.
 */

public class Localizadores {
    private String lat;
    private String lon;
    private String alt;
    private String vel;
    private String FechaHora;

    public Localizadores(String lat, String lon, String alt, String vel, String FechaHora) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.vel = vel;
        this.FechaHora = FechaHora;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getVel() {
        return vel;
    }

    public void setVel(String vel) {
        this.vel = vel;
    }

    public String getFechaHora() {
        return FechaHora;
    }

    public void setFechaHora(String fechaHora) {
        FechaHora = fechaHora;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(LocalizadoresContract.LocalizadoresEntry.LAT, this.lat);
        values.put(LocalizadoresContract.LocalizadoresEntry.LON, this.lon);
        values.put(LocalizadoresContract.LocalizadoresEntry.ALT, this.alt);
        values.put(LocalizadoresContract.LocalizadoresEntry.VEL, this.vel );
        values.put(LocalizadoresContract.LocalizadoresEntry.FECHAHORA, this.FechaHora);
        return values;
    }
}
