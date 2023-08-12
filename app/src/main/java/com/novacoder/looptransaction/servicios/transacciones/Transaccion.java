package com.novacoder.looptransaction.servicios.transacciones;


import android.content.ContentValues;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;

import org.json.JSONObject;

public abstract class Transaccion implements Cloneable {

    static public String tipo;
    static public Operaciones operador;
    static public String columnas;

    public int _id;
    public long transaccion_id;

    public int get_id () {
        return _id;
    }
    public long get_transaccion_id () {
        return transaccion_id;
    }
    public String get_tipo() {return tipo;};

    public String armar_query_tabla (){

        String query_lista = "CREATE TABLE IF NOT EXISTS " +
                tipo +
                " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "trasaccion_id INTEGER, " +
                "estado TEXT, " +
                columnas +
                ")";

        return query_lista;
    }

    public abstract void set_data(Cursor cursor);
    public abstract void set_data(StatusBarNotification notification);


    @Override
    public Transaccion clone() throws CloneNotSupportedException {
        return (Transaccion) super.clone();
    }

    public ContentValues format_sql (){
        ContentValues resultado = operador.format_sql(this);
        return resultado;
    };

    public JSONObject crear_postjson (){
        JSONObject resultado = operador.crear_postjson(this);
        return resultado;
    };

    public String ssml_local (){
        String resultado = operador.ssml_local(this);
        return resultado;
    };

    public String ssml_remoto (){
        String resultado = operador.ssml_remoto(this);
        return resultado;
    };




}