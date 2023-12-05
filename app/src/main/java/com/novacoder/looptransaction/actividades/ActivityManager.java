package com.novacoder.looptransaction.actividades;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ActivityManager {
    static Context AppContext;
    static Context CurrentContext;

    static public void inicializar(Context context) {
        AppContext = context;
    }

    static void setCurrentContext(Context context) {
        CurrentContext = context;
    }

    static Context getCurrentContext(Context context) {
        return CurrentContext;
    }

    static AppCompatActivity getCurrentActivity(Context context) {
        return (AppCompatActivity) CurrentContext;
    }

    static void StartActivity(Class<?> cls, int[] flags) {
        Intent intent = new Intent(CurrentContext, cls);
        for (int i = 0; i < flags.length; i++) {
            intent.addFlags(flags[i]);
        }
        StartActivity(intent);
    }

    static void StartActivity(Context context, Class<?> cls) {
        new Intent(CurrentContext, cls);
    }

    static void StartActivity(Class<?> cls) {
        Intent intent = new Intent(CurrentContext, cls);
        StartActivity(intent);
    }

    static void StartActivity(Intent intent) {
        try {
            ((AppCompatActivity) CurrentContext).startActivity(intent);
        } catch (NullPointerException e) {
            ((Application) AppContext).startActivity(intent);
        }
    }

    static void CurrentActivityFinish() {
        try {
            ((AppCompatActivity) CurrentContext).finish();
        } catch (NullPointerException e) {
        }
    }

}
