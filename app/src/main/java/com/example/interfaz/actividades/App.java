package com.example.interfaz.actividades;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfaz.IUcomponents.App_View;
import com.example.interfaz.IUcomponents.Buscador;
import com.example.interfaz.IUcomponents.Lista_View;
import com.example.interfaz.R;

import org.json.JSONException;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App_View app = new App_View(getApplicationContext());
        setContentView(app);


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
