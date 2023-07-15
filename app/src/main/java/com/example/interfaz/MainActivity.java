package com.example.interfaz;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    }

    public void crear_items (String datos) throws JSONException {


        LinearLayout linearLayout = findViewById(R.id.Content);
        linearLayout.removeAllViews();


        JSONArray Yapes =  new JSONArray(datos);

        for (int i = 0; i < Yapes.length(); i++) {

            JSONObject yape = Yapes.getJSONObject(i);


            LayoutInflater inflater = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
            }


            View itemG;



            if (i == 0) {
                itemG = inflater.inflate(R.layout.itemgrande, linearLayout, false);
            } else {
                itemG = inflater.inflate(R.layout.itemnormal, linearLayout, false);
            }

            TextView nombre = itemG.findViewById(R.id.nombre);
            nombre.setText(yape.getString("nombre"));

            TextView monto = itemG.findViewById(R.id.monto);
            monto.setText(yape.getString("monto"));

            TextView fecha = itemG.findViewById(R.id.fecha);
            fecha.setText(yape.getString("fecha"));

            linearLayout.addView(itemG);

        }


    }

    public void actualizar_vista () {


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://webprized.com/yapeNot/get.php";


        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PEDIDO RECIBIDO",  response);
                        try {
                            Log.d("VALOR RESPONSE ", response);
                            crear_items(response);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("ERROR EN ENVÍO: " ,  "Server Error");

                    }
                }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("Cant", "20");

                return params;
            }

        };



        //Envía el Pedido
        queue.add(stringRequest);

    }
}