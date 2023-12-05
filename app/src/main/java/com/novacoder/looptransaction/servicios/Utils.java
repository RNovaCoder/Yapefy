package com.novacoder.looptransaction.servicios;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {
    static Context context;
    static public void inicializar (Context contexto) {
        context = contexto;
    }
    static public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void ToastShort(String msj) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show();
    }
    public static void ToastLong(String msj) {
        Toast.makeText(context, msj, Toast.LENGTH_LONG).show();
    }
}
