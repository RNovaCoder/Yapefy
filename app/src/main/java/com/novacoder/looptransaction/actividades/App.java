package com.novacoder.looptransaction.actividades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.novacoder.looptransaction.R;
import com.novacoder.looptransaction.servicios.Call_Response;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Call_Response.inicializar(this);
        // Establecer el ViewGroup personalizado como contenido de la actividad
        setContentView(R.layout.activity_app);
        getSupportActionBar().hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Call_Response.destruir();
    }

}
