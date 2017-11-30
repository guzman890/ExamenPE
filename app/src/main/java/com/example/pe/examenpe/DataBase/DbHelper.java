package com.example.pe.examenpe.DataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.pe.examenpe.DataBase.Localizadores.*;

/**
 * Created by pbl_8 on 29/11/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Localizadores.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL
        sqLiteDatabase.execSQL(String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
                LocalizadoresContract.LocalizadoresEntry.TABLE_NAME,
                LocalizadoresContract.LocalizadoresEntry._ID,
                LocalizadoresContract.LocalizadoresEntry.LAT,
                LocalizadoresContract.LocalizadoresEntry.LON,
                LocalizadoresContract.LocalizadoresEntry.ALT,
                LocalizadoresContract.LocalizadoresEntry.VEL,
                LocalizadoresContract.LocalizadoresEntry.FECHAHORA)
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long save(Localizadores loc) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                LocalizadoresContract.LocalizadoresEntry.TABLE_NAME,
                null, loc.toContentValues());

    }
}
