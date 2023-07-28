package com.example.interfaz.servicios;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Enrutador {

    static RequestQueue manager_request;
    static String api_point ="https://webprized.com/yapeNot/get.php";

    @FunctionalInterface
    public interface Run {
        void run(JSONArray data);
    }

    public Enrutador (Context context){
        manager_request = Volley.newRequestQueue(context);
    }

    public void traer_data (Run run) {

        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                api_point,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("VALOR RESPONSE ", response);

                        try {
                            run.run(new JSONArray(response));
                        } catch (JSONException e) {
                            //throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("ERROR EN ENVÍO: " ,  "Server Error");

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
                params.put("Cant", "20");

                return params;
            }

        };


        //Envía el Pedido
        manager_request.add(stringRequest);


    }

    public void data_recibida (String data) {

    }

}
