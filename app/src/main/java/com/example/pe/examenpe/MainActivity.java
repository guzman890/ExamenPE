package com.example.pe.examenpe;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.pe.examenpe.DataBase.DbHelper;
import com.example.pe.examenpe.DataBase.Localizadores;
import com.example.pe.examenpe.model.post;
import com.example.pe.examenpe.remote.APIService;
import com.example.pe.examenpe.remote.ApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    // dbhleper create
    private DbHelper mDbHelper;

    //flag switch
    private static Switch sTrack;
    private static Switch sLocal;
    private static Switch sAuto;
    private static Button enviar;
    private static boolean tracking ;
    private static boolean automatic ;
    private static boolean localizacion ;
    //consola
    private static final String TAG;
    static {
        TAG = MainActivity.class.getName();
    }
    //Time request
    private static Time now;
    //GAC
    private static final int LocationPermissionFinal= 100;
    private GoogleApiClient uniqueGAC;
    private LocationRequest LRequest;
    private boolean flagUpdate = false;
    private Location UbicacionActual;

    public APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAPIService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_main);
        mDbHelper = new DbHelper(this);
        now = new Time();
        //Espera de permisos
        sTrack = findViewById(R.id.sTrack);
        sLocal = findViewById(R.id.sLocal);
        sAuto = findViewById(R.id.sAuto);



        tracking = sTrack.isChecked();
        automatic = sAuto.isChecked();
        localizacion = sLocal.isChecked();

        sTrack.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                Log.e(TAG, "-----------track------------"+tracking);
                if(tracking){
                    stopUpdates();
                }else{
                    startUpdates();
                }
                tracking = sTrack.isChecked();
                Log.e(TAG, "-----------track------------"+tracking);
            }
        });

        sAuto.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {

                automatic = sAuto.isChecked();
                Log.e(TAG, "-----------automatic-----------"+automatic);
            }
        });

        enviar = findViewById(R.id.bEnviar);
        enviar.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v) {
                EnviarData();
            }
        });

        permisosLocationFineAcces();
        iniciarGAC();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (uniqueGAC != null) {
            if (uniqueGAC.isConnected())
                startUpdates();
            else
                uniqueGAC.connect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdates();
    }

    private void iniciarGAC(){
        if (uniqueGAC == null) {
            uniqueGAC = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            crearLRequest();
        }
    }

    protected void crearLRequest(){
        LRequest = new LocationRequest();
        LRequest.setInterval(10000);
        LRequest.setFastestInterval(5000);
        LRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startUpdates(){
        if (uniqueGAC != null && uniqueGAC.isConnected() && !flagUpdate) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(uniqueGAC, LRequest, this);
                flagUpdate = true;
            }
        }
    }

    private void stopUpdates() {
        if (uniqueGAC != null && uniqueGAC.isConnected() && flagUpdate){
            LocationServices.FusedLocationApi.removeLocationUpdates(uniqueGAC, this);
            uniqueGAC.disconnect();
        }
    }

    private void permisosLocationFineAcces() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LocationPermissionFinal);
        }
    }

    private void EnviarData(){
        if (UbicacionActual != null && automatic) {
            now.setToNow();

            Localizadores loc = new Localizadores(String.valueOf(UbicacionActual.getLatitude()),
                    String.valueOf(UbicacionActual.getLongitude()),
                    String.valueOf(UbicacionActual.getAltitude()),
                    String.valueOf(UbicacionActual.getSpeed()),
                    now.format("%Y_%m_%d_%H_%M")
            );
            mDbHelper.save(loc);

            Log.e(TAG,"--------"+loc.getFechaHora()+"---------");
            sendPost(loc);
            /*
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(
                            " lat:"+String.valueOf(UbicacionActual.getLatitude())+
                            " long"+String.valueOf(UbicacionActual.getLongitude())+
                            " Alt:"+String.valueOf(UbicacionActual.getAltitude())+
                            " vel:"+String.valueOf(UbicacionActual.getSpeed())+
                            "Hora: "+now.format("%Y_%m_%d_%H_%M")
            );
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            */
        }
    }

    // Stack over flow Code
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationPermissionFinal) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startUpdates();
            } else {
                permisosLocationFineAcces();
            }
        }
    }

 
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        if (!flagUpdate) {
            startUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }
    @Override
    public void onLocationChanged(Location location) {
        UbicacionActual = location;
        EnviarData();
        Log.e(TAG, "---------tracking activate---------");
    }

    public void sendPost(Localizadores loc) {
        mAPIService.savePost(loc.getLat(),loc.getLon(),loc.getAlt(),loc.getVel(),loc.getFechaHora()).enqueue(new Callback<post>() {
            @Override
            public void onResponse(Call<post> call, Response<post> response) {

                if(response.isSuccessful()) {
                    //showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<post> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

}

