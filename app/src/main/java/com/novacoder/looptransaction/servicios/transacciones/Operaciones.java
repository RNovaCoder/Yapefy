package com.novacoder.looptransaction.servicios.transacciones;

import android.content.ContentValues;

import com.novacoder.looptransaction.ConfigApp;

import org.json.JSONObject;

public abstract class Operaciones {

    static public String ESTADO_PENDIENTE = ConfigApp.ESTADO_PENDIENTE;
    abstract public ContentValues format_sql(Transaccion transaccion);
    abstract public JSONObject crear_postjson (Transaccion transaccion);
    abstract public String ssml_local (Transaccion transaccion);
    abstract public String ssml_remoto (Transaccion transaccion);
}
