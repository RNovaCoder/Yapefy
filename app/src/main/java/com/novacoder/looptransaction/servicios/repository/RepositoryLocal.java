package com.novacoder.looptransaction.servicios.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public interface RepositoryLocal {

    String NOM_BD = "Transacciones";
    int DATABASE_VERSION = 1;


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

    void agregar_registros (ArrayList<Transaccion> transacciones);

    ArrayList<Transaccion> transacciones_a_enviar();

    void transaction_set_estado (Transaccion transaction, String estado);
}
