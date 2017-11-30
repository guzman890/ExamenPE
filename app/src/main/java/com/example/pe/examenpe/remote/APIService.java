package com.example.pe.examenpe.remote;

/**
 * Created by Home-User on 17/07/2017.
 */
import  com.example.pe.examenpe.model.post;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/api/user")
    @FormUrlEncoded
    Call<post> savePost(@Field("lat") String lat,
                        @Field("lon") String lon,
                        @Field("alt") String alt,
                        @Field("vel") String vel,
                        @Field("hora") String hora
    );
}