package com.novacoder.looptransaction.servicios.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public interface RepositoryLocal {

    String NOM_BD = "Transacciones";
    int DATABASE_VERSION = 1;
    static String ESTADO_PENDIENTE = ConfigApp.ESTADO_PENDIENTE;
    static String ESTADO_ENVIADO = ConfigApp.ESTADO_ENVIADO;

    default ArrayList<String> getAllTables(SQLiteDatabase db) {
        ArrayList<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }
            cursor.close();
        }

        return tables;
    }

    default long dateDifference(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(date);
            Date currentDate = new Date();

            long diffInMilliseconds = Math.abs(currentDate.getTime() - d1.getTime());
            long diffInSeconds = diffInMilliseconds / 1000;

            return diffInSeconds;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Error en el formato de fecha
        }
    }

    void agregar_registros (ArrayList<Transaccion> transacciones);

    ArrayList<Transaccion> transacciones_a_enviar();

    void limpiarRegistro ();

    void transaction_set_estado (Transaccion transaction, String estado);
}
