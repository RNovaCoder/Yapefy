package com.novacoder.looptransaction.servicios.contextos;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.MainActivity;
import com.novacoder.looptransaction.servicios.api.BuilderRouter;

import org.json.JSONException;
import org.json.JSONObject;

public class ContextSession {
    static public void Logout () {
        FirebaseAuth.getInstance().signOut();
        BuilderRouter.Logout().send();
        ConfigApp.set(ConfigApp.KEY_TOKEN, null);
        ConfigApp.set(ConfigApp.KEY_G_CORREO, "");
    }

    static public void LogIn (JSONObject response, GoogleSignInAccount signIn) {
        try {
            String token = response.getString("token");
            ConfigApp.set(ConfigApp.KEY_TOKEN, token);
            ConfigApp.set(ConfigApp.KEY_G_CORREO, signIn.getEmail());
        } catch (JSONException e) {
        }
    }
}
