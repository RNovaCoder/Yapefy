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

    static public String KEY_NAME_MODEL = "KeyNameModel";
    static public String KEY_URL_BACKEND = "KeyUrlBackend";
    static public String KEY_TOKEN = "KeyToken";
    static public String KEY_SONIDO = "KeySonido";

    static private String URL_BACKEND = "https://yape.webprized.com/api/yape/registrar";

    static public String SONIDO_LOCAL = "Local";
    static public String SONIDO_REMOTO = "Remoto";
    static public String SONIDO_NULL = "Ninguno";

    static public String[] sonidos = new String[]{
            SONIDO_LOCAL,
            SONIDO_REMOTO,
            SONIDO_NULL
    };

    static public String YAPE_MODEL_NAME = "Yape";
    static public String BCP_MODEL_NAME = "Bcp";

    static public String[] models = new String[]{
            YAPE_MODEL_NAME,
            BCP_MODEL_NAME
    };

    static public HashMap<String, String> ConfigParams = new HashMap<String, String>() {{
        put(KEY_NAME_MODEL, YAPE_MODEL_NAME);
        put(KEY_SONIDO, SONIDO_LOCAL);
        put(KEY_URL_BACKEND, URL_BACKEND);
        put(KEY_TOKEN, null);
    }};

    static public void inicializar (Context context){
        contexto = context;
        config = contexto.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor_config = config.edit();

        for (Map.Entry<String, String> entrada : ConfigParams.entrySet()) {
            String atributo = entrada.getKey();

            String result = config.getString(atributo, null);

            if (result != null){
                ConfigParams.put(atributo, result);
            }
        }
    }

    static public synchronized void set (String atributo, String valor) {
        editor_config.putString(atributo, valor).apply();
        String n_valor = config.getString(atributo, null);
        ConfigParams.put(atributo, n_valor);
    }

    static public String get (String key) {
        return ConfigParams.get(key);
    }

}
