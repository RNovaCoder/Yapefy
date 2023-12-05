package com.novacoder.looptransaction.Auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.Call_Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private Context contexto;
    private RequestQueue objet_http;

    static private final String ID_USER = "id";
    static private final String NAME_USER = "name";
    static private final String EMAIL_USER = "email";
    static private final String AVATAR_USER = "avatar";

    static private final String KEY_APP_TOKEN_LOCAL = "token";
    static private final String APP_TOKEN_LOCAL = ConfigApp.APP_TOKEN_LOCAL;
    static private final String URL_LOGIN = ConfigApp.URL_LOGIN;
    static private final String URL_LOGOUT = ConfigApp.URL_LOGOUT;
    static private final String URL_GET_DATA = ConfigApp.URL_GET_DATA;
    public boolean ocupado = false;
    private StringRequest jsonRequest;
    private Response.Listener listener = response -> {ocupado = false;};
    private Response.ErrorListener errorListener = error -> {
        ocupado = false;
        Call_Response.error_response(error, null, contexto);
    };

    public Router(Context context){
        contexto = context;
        objet_http = Volley.newRequestQueue (contexto);
        objet_http.addRequestEventListener((request, event) -> {
            switch (event) {
                case RequestQueue.RequestEvent.REQUEST_QUEUED:
                    ocupado = true;
                    break;
                case RequestQueue.RequestEvent.REQUEST_FINISHED:
                    ocupado = false;
                    break;
            }
        });
    }

    public void setAuthUser (GoogleSignInAccount account)  {

        JSONObject postData = null;
        try {
            postData = CreateData(account);
            jsonRequest = CreateJsonRequest(postData, URL_LOGIN);}
        catch (JSONException e) {}

    }
    public void setLogout () {
        jsonRequest = CreateJsonRequest(new JSONObject(), URL_LOGOUT);
    }
    public void getData () {
        String tipo = ConfigApp.get(ConfigApp.KEY_NAME_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject().put("tipo", tipo);
        } catch (JSONException e) {}

        jsonRequest = CreateJsonRequest(jsonObject, URL_GET_DATA);
    }

    public void setResponse (Response.Listener newListener) {
        listener = newListener;
    }
    public void setError (Response.ErrorListener error) {
        errorListener = error;
    }


    private StringRequest CreateJsonRequest(JSONObject postData, String URL_POST) {

        StringRequest jsonRequest = new StringRequest (
                Request.Method.POST, URL_POST, listener, errorListener)
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return postData.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = getCustomHeader();
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setShouldCache(false);
        return jsonRequest;
    }


    static Map<String, String> getCustomHeader () {
        String token = ConfigApp.get(ConfigApp.KEY_TOKEN);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Accept", "application/json");
        return headers;
    }

    static private JSONObject CreateData (GoogleSignInAccount account) throws JSONException {

        JSONObject postData = new JSONObject();
        postData.put(KEY_APP_TOKEN_LOCAL, APP_TOKEN_LOCAL);
        postData.put(ID_USER, account.getId());
        postData.put(NAME_USER, account.getDisplayName());
        postData.put(EMAIL_USER, account.getEmail());
        postData.put(AVATAR_USER, account.getPhotoUrl());

        return postData;

    }

    public boolean send(){
        if (!ocupado){
            objet_http.add(jsonRequest);
            return true;
        }
        return false;
    }
}
