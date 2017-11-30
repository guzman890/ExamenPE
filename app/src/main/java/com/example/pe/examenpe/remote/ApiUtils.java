package com.example.pe.examenpe.remote;

/**
 * Created by Home-User on 17/07/2017.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://token-dpa.herokuapp.com/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
