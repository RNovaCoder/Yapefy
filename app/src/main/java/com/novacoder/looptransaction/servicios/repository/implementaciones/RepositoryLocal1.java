package com.novacoder.looptransaction.servicios.repository.implementaciones;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.FactoryTransacciones;
import com.novacoder.looptransaction.servicios.repository.RepositoryLocal;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public class RepositoryLocal1 extends SQLiteOpenHelper implements RepositoryLocal {
    private static RepositoryLocal1 instance;
    private static SQLiteDatabase db_write;
    public static SQLiteDatabase db_read;

    private RepositoryLocal1(Context context) {
        super(context, NOM_BD, null, DATABASE_VERSION);
    }

    public static synchronized RepositoryLocal1 getInstance(Context context) {


        if (instance == null) {
            instance = new RepositoryLocal1(context);
            db_write = instance.getWritableDatabase();
            db_read = instance.getReadableDatabase();
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    public void crearTabla(Transaccion transaccion) {

        if (transaccion != null){
            //Método default heredado de la Clase Abstracta Transacción
            String createTableQuery = transaccion.armar_query_tabla();
            db_write.execSQL(createTableQuery);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + NOM_BD);
            onCreate(db);
        }
    }

    public synchronized void agregar_registros (ArrayList<Transaccion> transacciones) {

        String tabla;

        for (Transaccion transaccion: transacciones) {

            if (!(comprobar_id(transaccion))) {

                tabla = transaccion.get_tipo();

                ContentValues valores = transaccion.format_sql();

                //Log.d("DATOS Y RECUPERADO", String.valueOf(valores));

                try {
                    db_write.insertOrThrow(tabla, null, valores);
                }
                catch (SQLiteException e){
                    if (e.getMessage().contains("no such table")){
                        crearTabla(transaccion);
                    }
                }

            }
        }
    }

    public synchronized boolean comprobar_id(Transaccion transaccion) {

        String transaccion_id = String.valueOf(transaccion.get_transaccion_id());
        String tabla = transaccion.get_tipo();

        String query = "SELECT _id FROM " + tabla + " WHERE transaccion_id = ?";
        String[] Args = {transaccion_id};

        Cursor cursor = executar_sql(query, Args, transaccion);

        boolean existeRegistro;
        existeRegistro = (cursor != null && cursor.moveToFirst()) ? true: false;

        cursor.close();

        return existeRegistro;

    }

    public synchronized ArrayList<Transaccion> transacciones_a_enviar() {

        ArrayList<Transaccion> Transacciones = new ArrayList<>();
        String[] tablas = ConfigApp.models;

        for (String tabla : tablas) {

            String query = "SELECT * FROM " + tabla + " WHERE estado = ?";
            String[] Args = {this.ESTADO_PENDIENTE};

            Cursor cursor = executar_sql(query, Args, null);

            try {
                if (cursor.moveToFirst()) {
                    do {
                        Transacciones.add(FactoryTransacciones.crear_transaccion(/*tabla=tipo*/tabla, cursor));

                    } while (cursor.moveToNext());
                    cursor.close();
                }

            }

            catch (Exception e) {
                //Log.d ("Transaccioes a Enviar: ", e.getMessage());
            }

        }

        return  Transacciones;
    }

    public synchronized void limpiarRegistro() {

        String[] tablas = ConfigApp.models;

        for (String tabla : tablas) {

            String query = "SELECT * FROM " + tabla + " WHERE estado = ?";
            String[] Args = {this.ESTADO_ENVIADO};
            Cursor cursor = executar_sql(query, Args, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));

                    long diferencia = dateDifference(fecha);

                    if (diferencia > 25) {
                        //Log.d("CANTIDAD DATE", String.valueOf(diferencia));
                        limpiar(id, tabla);
                    }

                }
                cursor.close();
            }
        }
    }

    public synchronized void limpiar(long id, String tabla) {
        String query = "UPDATE " + tabla + " SET estado = ? WHERE _id = ?";
        String[] Args = {this.ESTADO_PENDIENTE, String.valueOf(id)};
        executar_sql(query, Args, null);
    }

    public synchronized void transaction_set_estado(Transaccion transaccion, String estado) {

        int id = transaccion.get_id();
        String tabla = transaccion.get_tipo();

        String query = "UPDATE " + tabla + " SET estado = ? WHERE _id = ?";
        Object[] Args = {estado, String.valueOf(id)};

        executar_sql(query, Args, transaccion);

    }

    private synchronized Cursor executar_sql (String sentence_sql, Object[] arg, Transaccion transaccion) {

        Cursor cursor = null;

        try {

            if (sentence_sql.contains("SELECT")) {
                //Convertir el Obj[] arg, en String[]
                String[] arg_strings = new String[arg.length];
                for (int i = 0; i < arg.length; i++) {
                    arg_strings[i] = String.valueOf(arg[i]); }

                cursor = db_read.rawQuery(sentence_sql, arg_strings);
            }
            else {
                db_write.execSQL(sentence_sql, arg);
            }

        } catch (SQLiteException e) {

            if (e.getMessage().contains("no such table")){
                //Log.d("TEST SQLLLL:", e.getMessage());
                if (transaccion != null) {
                    crearTabla(transaccion);
                    cursor = executar_sql(sentence_sql, arg, transaccion);
                }

            }
            else {}

        }

        return cursor;

    }

    static public void leer_tabla (String tipo) {


        Cursor cursor = db_read.rawQuery("SELECT * FROM " + tipo, null);
        if (cursor.moveToFirst()) {
            int columnCount = cursor.getColumnCount();
            do {
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    //Log.d("LEER TABLA", columnName + ": " + columnValue);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
