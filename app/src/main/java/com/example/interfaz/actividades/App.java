package com.example.interfaz.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfaz.IUcomponents.App_View;
import com.example.interfaz.IUcomponents.Buscador;
import com.example.interfaz.IUcomponents.Lista_View;
import com.example.interfaz.R;
import com.example.interfaz.Socket_Reproductor;

import org.json.JSONException;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent socket_rep = new Intent(getApplicationContext(), Socket_Reproductor.class);
        startService(socket_rep);

        // Establecer el ViewGroup personalizado como contenido de la actividad
        setContentView(R.layout.activity_app);
        getSupportActionBar().hide();


    }

}
