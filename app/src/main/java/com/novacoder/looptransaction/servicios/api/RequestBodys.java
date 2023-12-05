package com.novacoder.looptransaction.servicios.api;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.novacoder.looptransaction.ConfigApp;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestBodys {
    static private final String ID_USER = "id";
    static private final String NAME_USER = "name";
    static private final String EMAIL_USER = "email";
    static private final String AVATAR_USER = "avatar";
    static private final String KEY_APP_TOKEN_LOCAL = "token";
    static private final String APP_TOKEN_LOCAL = ConfigApp.APP_TOKEN_LOCAL;

    public static JSONObject BodyRequest(GoogleSignInAccount account) {
        JSONObject postData = new JSONObject();
        try {
            postData.put(KEY_APP_TOKEN_LOCAL, APP_TOKEN_LOCAL);
            postData.put(ID_USER, account.getId());
            postData.put(NAME_USER, account.getDisplayName());
            postData.put(EMAIL_USER, account.getEmail());
            postData.put(AVATAR_USER, account.getPhotoUrl());
        } catch (JSONException e) {}
        return postData;
    }

    public static JSONObject FetchDataBody( ) {
        String tipo = ConfigApp.get(ConfigApp.KEY_NAME_MODEL);
        String meses = ConfigApp.get(ConfigApp.KEY_NUM_MESES);
        JSONObject postData = new JSONObject();
        try {
            postData.put("tipo", tipo);
            postData.put("meses", tipo);
        } catch (JSONException e) {}
        return postData;
    }
    public static JSONObject UpdateBody(String data) {
        String tipo = ConfigApp.get(ConfigApp.KEY_NAME_MODEL);
        JSONObject postData = new JSONObject();
        try {
            postData.put("tipo", tipo);
            postData.put("csvContent", data);
        } catch (JSONException e) {}
        return postData;
    }
}
