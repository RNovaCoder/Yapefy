package com.novacoder.looptransaction.servicios.api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.novacoder.looptransaction.ConfigApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuilderRouter {
    static private Context contexto;
    static private final String URL_LOGIN = ConfigApp.URL_LOGIN;
    static private final String URL_LOGOUT = ConfigApp.URL_LOGOUT;
    static private final String URL_GET_DATA = ConfigApp.URL_GET_DATA;

    public static Router<JSONArray> FetchData() {
        Router<JSONArray> router = new Router<>(contexto);
        router.setParams(URL_GET_DATA, Request.Method.POST, RequestBodys.FetchDataBody());
        defaultProps(router);
        return router;
    }

    public static Router<JSONObject> Login(GoogleSignInAccount googleSignIn) {
        Router<JSONObject> router = new Router<>(contexto);
        router.setParams(URL_LOGIN, Request.Method.POST, RequestBodys.BodyRequest(googleSignIn));
        router.Request.setRetryPolicy(getDefaultPolicy());
        router.Request.addHeader(getDefaultHeader());
        return router;
    }

    public static Router<JSONObject> UpdateDate(String data) {
        Router<JSONObject> router = new Router<>(contexto);
        router.setParams(URL_LOGIN, Request.Method.POST, RequestBodys.UpdateBody(data));
        defaultProps(router);
        return router;
    }

    public static Router<JSONObject> Logout() {
        Router<JSONObject> router = new Router<>(contexto);
        router.setParams(URL_LOGOUT, Request.Method.POST, new JSONObject());
        defaultProps(router);
        return router;
    }

    static void defaultProps(Router router) {
        router.Request.addHeader(getDefaultHeader());
        router.Request.addHeader(getAuthorizationHeader());
        router.Request.setRetryPolicy(getDefaultPolicy());
        router.Request.setShouldCache(false);
    }

    static Map<String, String> getAuthorizationHeader() {
        String token = ConfigApp.get(ConfigApp.KEY_TOKEN);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
    static Map<String, String> getDefaultHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    static DefaultRetryPolicy getDefaultPolicy() {
        return new DefaultRetryPolicy(15000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static void inicializar(Context context) {
        contexto = context;
    }
}
