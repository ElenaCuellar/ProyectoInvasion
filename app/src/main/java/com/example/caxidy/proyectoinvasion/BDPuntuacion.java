package com.example.caxidy.proyectoinvasion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BDPuntuacion extends SQLiteOpenHelper {

    private static Puntuacion puntuacion;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "BD_Puntuacion.db";
    private static final String NOMBRE_TABLA = "Puntuaciones";
    private static final String ins = "CREATE TABLE Puntuaciones (nombre VARCHAR(100), puntuacion INT)";

    public BDPuntuacion(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ins);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + NOMBRE_TABLA);
        onCreate(db);
    }

    public long insertar(Puntuacion p) {
        long numReg = -1;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", p.getNombre());
            valores.put("puntuacion", p.getPuntuacion());
            numReg = db.insert(NOMBRE_TABLA, null, valores);
        }
        db.close();
        return numReg;
    }

    //devuelve todos los registros
    public ArrayList<Puntuacion> listado() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Puntuacion> listaPunt = new ArrayList<Puntuacion>();
        if (db != null) {
            String[] campos = {"nombre", "puntuacion"};
            Cursor c = db.query(NOMBRE_TABLA, campos, null, null, null, null,
                    "puntuacion", null);
            if (c.moveToFirst()) {
                do {
                    puntuacion = new Puntuacion(c.getString(0), c.getInt(1));
                    listaPunt.add(puntuacion);
                } while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return listaPunt;
    }
}
