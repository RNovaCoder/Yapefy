package com.example.interfaz;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.interfaz.IUcomponents.Buscador;
import com.example.interfaz.IUcomponents.Lista_View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_vista();
            }
        });

        actualizar_vista();

        Intent socket_rep = new Intent(getApplicationContext(), Socket_Reproductor.class);
        startService(socket_rep);

        Lista_View lista = new Lista_View(getApplicationContext());
        new Buscador(getApplicationContext()).add_listener("sad", (target) -> {
            //EditText input = (EditText) target;
            //lista.filtrar_data( target.getText().toString());
            });
    }

    public void crear_items (String datos) throws JSONException {

    }

    public void actualizar_vista () {



    }
}