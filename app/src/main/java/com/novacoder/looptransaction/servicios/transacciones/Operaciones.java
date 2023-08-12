package com.novacoder.looptransaction.servicios.transacciones;

import android.content.ContentValues;

import org.json.JSONObject;

public abstract class Operaciones {

    abstract public ContentValues format_sql(Transaccion transaccion);
    abstract public JSONObject crear_postjson (Transaccion transaccion);
    abstract public String ssml_local (Transaccion transaccion);
    abstract public String ssml_remoto (Transaccion transaccion);
}
