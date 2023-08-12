package com.novacoder.looptransaction.actividades;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.novacoder.looptransaction.R;

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
