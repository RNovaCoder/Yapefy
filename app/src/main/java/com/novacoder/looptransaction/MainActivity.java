package com.novacoder.looptransaction;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novacoder.looptransaction.actividades.App;
import com.novacoder.looptransaction.actividades.Login;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNotificationServiceEnabled()) {
            dar_permisos();
        } else {
            inicializar();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!isNotificationServiceEnabled()) {
            dar_permisos();
        } else {
            inicializar();
        }
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (flat != null) {
            return flat.contains(pkgName);
        }
        return false;
    }

    private void inicializar() {

        //ConfigApp.inicializar(getApplicationContext());

        Token = ConfigApp.get(ConfigApp.KEY_TOKEN);

        Intent intent;

        if (Token == null) {
            intent = new Intent(this, Login.class);
        } else {
            intent = new Intent(this, App.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Log.d("signError", "ACTIVIDA PRINCIAPL");
        finish();
    }

    private void dar_permisos () {
        Toast myToast = Toast.makeText(getApplicationContext(), "Debes conceder este permiso para poder continuar", Toast.LENGTH_SHORT);
        myToast.show();
        Intent intentSetting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivityForResult(intentSetting, 4);
    }


}