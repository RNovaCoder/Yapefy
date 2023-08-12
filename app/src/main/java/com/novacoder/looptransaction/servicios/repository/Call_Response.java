package com.novacoder.looptransaction.servicios.repository;

import android.util.Log;

import com.android.volley.VolleyError;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.FE_RepositoryLocal;
import com.novacoder.looptransaction.servicios.RepTts;
import com.novacoder.looptransaction.servicios.Reproductor;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import org.json.JSONException;
import org.json.JSONObject;


public class Call_Response {


    static public void response (JSONObject response, Transaccion transaccion) {

        try {

            String message = response.getString("message");

            if (message.equals("Guardado con exito")){

                FE_RepositoryLocal.transaction_set_estado(transaccion, "registrado");
                String audio = response.getString("audio");

                if (audio != null){
                    Reproductor.notificar_audio(audio);
                }
                else {
                    if (ConfigApp.get(ConfigApp.KEY_SONIDO).equals(ConfigApp.SONIDO_LOCAL)) {
                        String ssml = transaccion.ssml_local();
                        RepTts.Speak(ssml);
                    }
                }
            }
            //
            else {
                Log.d("ERROR RARÍSIMO", message);
            }

        } catch (Exception e) {
            Log.d("ERROR SINTAXIS", e.getMessage());
        }

    }

    static void error_response (VolleyError error, Transaccion transaccion) {

        try {
            //Errores de validación
            if (error.networkResponse.statusCode == 422) {

                String errorBody = new String(error.networkResponse.data);
                JSONObject errorJSON= new JSONObject(errorBody);
                String mensaje = errorJSON.getString("message");

                String nuevo_estado;

                if (mensaje.equals("The yape id has already been taken.")) {
                    nuevo_estado = "dup_registrado";
                }
                else {
                    nuevo_estado = "invalido";
                }

                Log.d("ERROR HTTP", nuevo_estado);
                FE_RepositoryLocal.transaction_set_estado(transaccion, nuevo_estado);
            }

            else {
                //Error de conexión y no de Validación o Duplicidad
                //Debe volver a enviarlo
                FE_RepositoryLocal.transaction_set_estado(transaccion, "pendiente");
            }


        } catch (JSONException e) {
            Log.d("ERROR GENÉRICO", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
