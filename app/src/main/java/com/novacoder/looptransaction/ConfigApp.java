package com.novacoder.looptransaction;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class ConfigApp {

    static public Context contexto;
    static private SharedPreferences config;
    static private SharedPreferences.Editor editor_config;
    static private String SHARED_PREFERENCE = "Config";
    static public String KEY_NAME_MODEL = "Tipo de Transacción";
    static public String KEY_URL_BACKEND = "Url Backend";
    static public String KEY_NUM_TRANSACCIONES = "Número de Transacciones";
    static public String KEY_G_CORREO = "googleCorreo";
    static public String APP_TOKEN_LOCAL = "K6D82B1M7I";
    static public String KEY_TOKEN = "Token";
    static public String KEY_SONIDO = "Tipo de Notificación";
    static public String URL_BACKEND = "https://yape.webprized.com/api/transaccion/registrar";
    static public String URL_LOGIN = "https://yape.webprized.com/api/usuario/registrar";
    static public String URL_LOGOUT = "https://yape.webprized.com/api/usuario/cerrar.sesion";
    static public String URL_GET_DATA = "https://yape.webprized.com/api/transaccion/obtener";
    static public String NUM_TRANSACCIONES = "3000";
    static public String SONIDO_LOCAL = "Local";
    static public String SONIDO_REMOTO = "Remoto";
    static public String SONIDO_NULL = "Ninguno";
    static public String ESTADO_ENVIADO = "enviado";
    static public String ESTADO_DUPLICADO = "duplicado";
    static public String ESTADO_REGISTRADO = "resgistrado";
    static public String ESTADO_PENDIENTE = "pendiente";
    static public String ESTADO_INVALIDO = "invalido";

    static public String[] sonidos = new String[]{
            SONIDO_LOCAL,
            SONIDO_REMOTO,
            SONIDO_NULL
    };

    static public String YAPE_MODEL_NAME = "Yape";
    static public String BCP_MODEL_NAME = "Bcp";

    static public String[] models = new String[]{
            YAPE_MODEL_NAME,
            //BCP_MODEL_NAME
    };

    static public HashMap<String, String> ConfigParams = new HashMap<String, String>() {{
        put(KEY_NAME_MODEL, YAPE_MODEL_NAME);
        put(KEY_SONIDO, SONIDO_LOCAL);
        put(KEY_URL_BACKEND, URL_BACKEND);
        put(KEY_NUM_TRANSACCIONES, NUM_TRANSACCIONES);
        put(KEY_G_CORREO, "");
        put(KEY_TOKEN, null);
    }};

    static public void inicializar(Context context) {
        contexto = context;
        config = contexto.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor_config = config.edit();

        for (Map.Entry<String, String> entrada : ConfigParams.entrySet()) {
            String atributo = entrada.getKey();

            String result = config.getString(atributo, null);

            if (result != null) {
                ConfigParams.put(atributo, result);
            }
        }
    }

    static public synchronized String set(String atributo, String valor) {
        editor_config.putString(atributo, valor).apply();
        String n_valor = config.getString(atributo, null);
        ConfigParams.put(atributo, n_valor);
        return n_valor;
    }

    static public String get(String key) {
        return ConfigParams.get(key);
    }


}