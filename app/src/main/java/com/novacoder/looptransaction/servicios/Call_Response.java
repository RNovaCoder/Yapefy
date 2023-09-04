package com.novacoder.looptransaction.servicios;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.actividades.Login;
import com.novacoder.looptransaction.servicios.FE_RepositoryLocal;
import com.novacoder.looptransaction.servicios.RepTts;
import com.novacoder.looptransaction.servicios.Reproductor;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import org.json.JSONException;
import org.json.JSONObject;


public class Call_Response {

    static Context contexto;

    static public void inicializar(Context context) {
        contexto = context;
    }

    static public void destruir() {
        contexto = null;
    }

    static String nCODE_ERROR = "CALL_RESPONSE";

    static synchronized public void response(JSONObject response, Transaccion transaccion) {

        //Log.d(nCODE_ERROR, "RECIBIDO AUD");
        try {

            String message = response.getString("message");

            if (message.equals("Guardado con exito")) {

                FE_RepositoryLocal.set_estado_registrado(transaccion);
                String audio = response.getString("audio");

                if (!audio.equals("null")) {
                    //Log.d(nCODE_ERROR, audio);
                    Reproductor.notificar_audio(audio);
                } else {
                    if (ConfigApp.get(ConfigApp.KEY_SONIDO).equals(ConfigApp.SONIDO_LOCAL)) {
                        String ssml = transaccion.ssml_local();
                        RepTts.Speak(ssml);
                    }
                }
            }
            //
            else {
                //Log.d(nCODE_ERROR, message);
            }

        } catch (Exception e) {
            //Log.d(nCODE_ERROR, e.getMessage());
        }

    }

    static public void error_response(VolleyError error, Transaccion transaccion) {

        try {
            //Errores de validación
            if (error.networkResponse.statusCode == 422) {

                String errorBody = new String(error.networkResponse.data);
                JSONObject errorJSON = new JSONObject(errorBody);
                String mensaje = errorJSON.getString("message");

                if (transaccion != null) {
                    if (mensaje.equals("El valor del campo transaccion id ya está en uso.")) {
                        FE_RepositoryLocal.set_estado_duplicado(transaccion);
                    } else {
                        FE_RepositoryLocal.set_estado_invalido(transaccion);
                    }
                }

                //Token Inválido, se debe relogear
            } else if (error.networkResponse.statusCode == 401) {
                if (transaccion != null) {
                    FE_RepositoryLocal.set_estado_pendiente(transaccion);
                }
                Login.Logout(contexto);

            } else {
                //Error de conexión y no de Validación o Duplicidad
                //Debe volver a enviarlo
                FE_RepositoryLocal.set_estado_pendiente(transaccion);
            }


        } catch (JSONException e) {
            //Log.d(nCODE_ERROR, e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
