package com.novacoder.looptransaction.servicios.api;

import static com.novacoder.looptransaction.servicios.Utils.ToastShort;
import static com.novacoder.looptransaction.servicios.Utils.isNetworkAvailable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.novacoder.looptransaction.servicios.Call_Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class Router<T> {

    @FunctionalInterface
    public interface Listener<T> {
        void run(T response);
    }

    private final RequestQueue requestQueue;
    public boolean ocupado = false;
    public JsonArrayRequestCustom Request;
    private final Listener<T>[] listener = new Listener[]{response -> {}};
    private final Listener<VolleyError>[] errorListener = new Listener[] {error -> {}};
    private final Runnable[] finallyListener = {() -> {}};

    private final Response.Listener<T> listenerDefault = response -> {
        listener[0].run(response);
    };
    private final Response.ErrorListener errorListenerdefault = error -> {
        Call_Response.error_response(error, null, null);
        errorListener[0].run(error);
    };

    public Router(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.addRequestEventListener((request, event) -> {
            switch (event) {
                case RequestQueue.RequestEvent.REQUEST_QUEUED:
                    ocupado = true;
                    break;
                case RequestQueue.RequestEvent.REQUEST_FINISHED:
                    ocupado = false;
                    finallyListener[0].run();
                    break;
            }
        });
    }

    public void setParams(String URL, int Method, JSONObject body) {
        Request = new JsonArrayRequestCustom<JSONArray>(Method, URL, body.toString(), (Response.Listener<T>) listenerDefault, errorListenerdefault);
    }

    public void setResponse(Listener<T> list) {
        listener[0] = list;
    }

    public void setFinally(Runnable runnable) {
        finallyListener[0] = runnable;
    }

    public void setError(Listener<VolleyError> list) {
        errorListener[0] = list;
    }

    public boolean send() {
        if (!ocupado) {
            if (isNetworkAvailable()) {
                requestQueue.add(Request);
            } else {
                ToastShort("Revisa tu conexión a internet");
            }
            return true;
        }
        return false;
    }


}

