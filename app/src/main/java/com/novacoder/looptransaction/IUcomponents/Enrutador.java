package com.novacoder.looptransaction.IUcomponents;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config.Config;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Enrutador {

    static RequestQueue manager_request;
    static Context contexto;
    static boolean ocupado = false;
    static String api_point ="https://webprized.com/yapeNot/get.php";
    @FunctionalInterface
    public interface Run {
        void run(JSONArray data);
    }


    static public void inicializar (Context context){
        contexto = context;
        if (manager_request == null){
            manager_request = Volley.newRequestQueue(context);
        }
    }

    static public void destruir (){
        contexto = null;
        manager_request = null;
    }

    static public void traer_data (Run run) {

        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                api_point,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ocupado = false;
                        //Log.d("VALOR RESPONSE ", response);

                        try {
                            JSONArray respuesta = new JSONArray(response);
                            run.run(respuesta);

                        } catch (JSONException e) {
                            //Log.d("ERROR AL CREAR JSON",e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ocupado = false;

                        //Log.d("ERROR EN ENVÍO: " ,  "Server Error");

                    }
                }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("Cant", ConfigApp.get(ConfigApp.KEY_NUM_TRANSACCIONES));

                return params;
            }

        };


        //Log.d("NUMERO SOLICTUDES ", String.valueOf(manager_request.getSequenceNumber()));


        if (!ocupado) {
            manager_request.add(stringRequest);
            ocupado = true;
        }



    }

    public void data_recibida (String data) {

    }

}