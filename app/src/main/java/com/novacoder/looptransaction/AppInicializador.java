package com.novacoder.looptransaction;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.novacoder.looptransaction.actividades.ActivityManager;
import com.novacoder.looptransaction.servicios.Call_Response;
import com.novacoder.looptransaction.servicios.Utils;
import com.novacoder.looptransaction.servicios.api.BuilderRouter;

public class AppInicializador extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigApp.inicializar(getApplicationContext());
        Call_Response.inicializar(getApplicationContext());
        BuilderRouter.inicializar(getApplicationContext());
        ActivityManager.inicializar(getApplicationContext());
        Utils.inicializar(getApplicationContext());
    }

}
