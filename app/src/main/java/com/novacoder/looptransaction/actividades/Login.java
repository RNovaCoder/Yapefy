package com.novacoder.looptransaction.actividades;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.novacoder.looptransaction.Auth.Router;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.MainActivity;
import com.novacoder.looptransaction.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private static String ERROR_LOGIN = "signError";

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

        mSignInClient = GoogleSignIn.getClient(getApplicationContext(), options);

        SignInButton signB = findViewById(R.id.signButton);
        signB.setOnClickListener(view -> {
            if (isNetworkAvailable()) {
                signIn();
            } else {
                Toast.makeText(this, "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
            }
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

                        Intent intent = new Intent(this, MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


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
        Log.d("LogOutLOG", "LOGOUT DESDE LOGIN");

        try {
            FirebaseAuth.getInstance().signOut();
            mSignInClient.signOut().addOnCompleteListener(runnable -> {
                clearSesion(context);
            });
        } catch (Exception e) {
            Log.d("LogOutLOG", "No hay mSignInClient");
            clearSesion(context);
        }
    }

    private static void clearSesion(Context context){
        String token = ConfigApp.get(ConfigApp.KEY_TOKEN);
        ConfigApp.set(ConfigApp.KEY_TOKEN, null);
        ConfigApp.set(ConfigApp.KEY_G_CORREO, "");

        if (token != null) {
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
        }
        Intent Main = new Intent(context, MainActivity.class);
        context.startActivity(Main);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}