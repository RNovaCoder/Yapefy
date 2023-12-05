package com.novacoder.looptransaction.actividades;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.MainActivity;
import com.novacoder.looptransaction.R;
import com.novacoder.looptransaction.servicios.Utils;
import com.novacoder.looptransaction.servicios.api.BuilderRouter;
import com.novacoder.looptransaction.servicios.api.Router;
import com.novacoder.looptransaction.servicios.contextos.ContextSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Login2 extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    static public GoogleSignInClient mSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestId().requestProfile().build();

        mSignInClient = GoogleSignIn.getClient(getApplicationContext(), options);

        SignInButton signB = findViewById(R.id.signButton);
        signB.setOnClickListener(view -> {
            if (Utils.isNetworkAvailable()) {
                signIn();
            } else {
                Utils.ToastLong("Revisa tu conexión a Internet");
            }
        });
    }

    private void signIn() {
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
                Router<JSONObject> auth = BuilderRouter.Login(acct);

                auth.setResponse(response -> {
                    ContextSession.LogIn(response, acct);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
                auth.send();
            }
        }
    }
}
