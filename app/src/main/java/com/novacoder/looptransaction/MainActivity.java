package com.novacoder.looptransaction;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.cargar_pagos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_vista();
            }
        });

    }

    public void crear_items (String datos) throws JSONException {

    }

    public void actualizar_vista () {



    }
}