package com.novacoder.looptransaction.servicios.repository;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.Call_Response;
import com.novacoder.looptransaction.servicios.FE_RepositoryLocal;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class RepositoryRemoto {
    static public RequestQueue objet_http;
    public void enviar_registros (ArrayList<Transaccion> transacciones){

        for (Transaccion transaccion: transacciones) {

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Call_Response.response(response, transaccion);
                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Call_Response.error_response(error, transaccion, null);
                }
            };

            JSONObject postData = transaccion.crear_postjson();
            JsonObjectRequest jsonRequest = crear_jsonRequest(postData, listener, errorListener);
            objet_http.add(jsonRequest);

            //Cambiar el Estado a Enviado
            FE_RepositoryLocal.set_estado_enviado(transaccion);

        }
    }

    private JsonObjectRequest crear_jsonRequest (JSONObject postData,
                                                 Response.Listener<JSONObject> listener,
                                                 Response.ErrorListener errorListener) {


        String token = ConfigApp.get(ConfigApp.KEY_TOKEN);
        String url_post = ConfigApp.get(ConfigApp.KEY_URL_BACKEND);

        JsonObjectRequest json_request = new JsonObjectRequest (
                Request.Method.POST, url_post, postData, listener,
                errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                headers.put("Content-Length", Integer.toString(postData.toString().getBytes().length)); // Longitud del cuerpo

                Map<String, String> defaultHeaders = super.getHeaders();
                headers.putAll(defaultHeaders);

                return headers;
            }

        };

        json_request.setRetryPolicy(new DefaultRetryPolicy(
                20000,  // Timeout en milisegundos
                0,      // Cantidad máxima de reintentos (0 para no reintentar)
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT  // Factor de aumento gradual del timeout
        ));
        json_request.setShouldCache(false);

        return json_request;
    }

}
