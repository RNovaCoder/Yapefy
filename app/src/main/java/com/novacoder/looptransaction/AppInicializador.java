package com.novacoder.looptransaction;

import android.app.Application;

public class AppInicializador extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigApp.inicializar(getApplicationContext());
    }
}
