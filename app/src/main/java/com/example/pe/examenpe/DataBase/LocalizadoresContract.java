package com.example.pe.examenpe.DataBase;

import android.provider.BaseColumns;

/**
 * Created by pbl_8 on 29/11/2017.
 */

public class LocalizadoresContract {
    public static abstract class LocalizadoresEntry implements BaseColumns {
        public static final String TABLE_NAME ="localizadores";

        public static final String LAT = "lat";
        public static final String LON = "lon";
        public static final String ALT = "alt";
        public static final String VEL = "vel";
        public static final String FECHAHORA = "FechaHora";
    }

}
