package com.example.interfaz;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfaz.IUcomponents.Buscador;
import com.example.interfaz.IUcomponents.Lista_View;

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



        actualizar_vista();

        Intent socket_rep = new Intent(getApplicationContext(), Socket_Reproductor.class);
        startService(socket_rep);

        Lista_View lista = new Lista_View(getApplicationContext());


        Buscador toolbar = new Buscador(getApplicationContext());

        toolbar.add_listener("sad", (target) -> {
            EditText input = (EditText) target;
            lista.filtrar_data( input.getText().toString());
        });


    }

    public void crear_items (String datos) throws JSONException {

    }

    public void actualizar_vista () {



    }
}