package com.novacoder.looptransaction.actividades;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.novacoder.looptransaction.Auth.Router;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.MainActivity;
import com.novacoder.looptransaction.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private String ERROR_LOGIN = "signError";

    static public GoogleSignInClient mSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestId()
                        .requestProfile()
                        .build();

        mSignInClient = GoogleSignIn.getClient(this, options);


        SignInButton signB = findViewById(R.id.signButton);
        signB.setOnClickListener(view -> {
            signIn();
        });


    }

    private void signIn() {
        // Launches the sign in flow, the result is returned in onActivityResult
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {

                GoogleSignInAccount acct = task.getResult();

                Router auth = new Router(this);

                //Log.d(ERROR_LOGIN, String.valueOf(acct.getId()));
                auth.setResponse(response -> {
                    try {
                        String token = (new JSONObject((String) response)).getString("token");
                        //Log.d(ERROR_LOGIN, token);
                        ConfigApp.set(ConfigApp.KEY_TOKEN, token);
                        ConfigApp.set(ConfigApp.KEY_G_CORREO, acct.getEmail());
                        finish();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //Log.d(ERROR_LOGIN, "ACTIVIDA FINALIZADA");
                    } catch (JSONException e) {
                        //Log.d(ERROR_LOGIN, e.getMessage().toString());
                    }
                });

                auth.setAuthUser(acct);
                auth.send();

            } else {
            }
        }
    }

    static public void Logout(Context context) {
        try {
            mSignInClient.signOut().addOnCompleteListener(runnable -> {

                if (context != null) {
                    Router logout = new Router(context);
                    logout.setLogout();
                    logout.setResponse(response -> {
                        try {
                            String msg = ((JSONObject) response).getString("msg");
                            //Log.d("Call AUTHa", msg);
                        } catch (JSONException e) {
                        }
                    });
                    logout.send();

                    ConfigApp.set(ConfigApp.KEY_TOKEN, null);
                    ConfigApp.set(ConfigApp.KEY_G_CORREO, "");
                    ((AppCompatActivity) context).finish();

                    mSignInClient = null;

                    Intent Main = new Intent(context, MainActivity.class);
                    Main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(Main);

                }

            });
        } catch (Exception e) {
        }
    }


}