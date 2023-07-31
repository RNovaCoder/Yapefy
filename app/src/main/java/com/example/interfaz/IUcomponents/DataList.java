package com.example.interfaz.IUcomponents;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class DataList {
    public boolean state_data = false;
    public JSONArray data = new JSONArray();
    public JSONArray data_filtrada = new JSONArray();

    public JSONArray data_envio;

    public JSONArray data_operada;

    public int contador = 0;
    public int cantidad = 200;



    @FunctionalInterface
    public interface CallbackFiltro {
        void call_filtro();
    }

    public void filtrar_data (String filtro, CallbackFiltro call_back) {

        //Limpiando el filtro para usarlo en el hilo secundario
        filtro = filtro.toLowerCase().replaceAll("\\s", "");
        final String finalFiltro = filtro;

        new Thread(() -> {

            //Recorriendo cada item_objeto de la data actual
            for (int i = 0; i < data.length(); i++) {

                try {
                    JSONObject jsonObject = data.getJSONObject(i);
                    //Recorriendo cada sección del Item y comparándolo con el filtro
                    for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                        String key = it.next();
                        String valor = jsonObject.getString(key);
                        valor = valor.toLowerCase().replaceAll("\\s", "");
                        if (valor.contains(finalFiltro)) {
                            data_filtrada.put(jsonObject);
                        }
                    }
                } catch (Exception e) {
                }

            }

            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> {


                call_back.call_filtro();
                Log.d("TEST :: LIS_VIEW:::", String.valueOf(data_filtrada.length()));
            });

        }).start();

    }


    public JSONArray obtener_data () {

        data_operada = (state_data)? data: data_filtrada;

        for (int i = contador*cantidad; i < (contador+1)*cantidad; i++) {
            try {
                data_envio.put(data_operada.get(i));
            } catch (Exception e){
                if (e.getMessage().contains("out of range")){
                    break;
                }
            }
        }


        //talves contador = contador + 1;

        return data_envio;
    }

    public void set_contador (int valor) {

        contador = contador + valor;
        //Por si ocurre un error (El contador no puede ser cero)
        contador = (contador < 0) ? 0: contador;
    }

    public void set_estado (boolean estado) {
        state_data = estado;
    }

    public int get_contador () {
        return contador;
    }

    public void set_data (JSONArray new_data) {
        data = new_data;
    }




}
