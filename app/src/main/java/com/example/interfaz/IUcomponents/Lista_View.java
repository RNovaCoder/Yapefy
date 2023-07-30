package com.example.interfaz.IUcomponents;

import android.content.Context;

import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.interfaz.R;
import com.example.interfaz.servicios.Enrutador;

import org.json.JSONArray;
import org.json.JSONObject;


public class Lista_View extends ConstraintLayout {

    static Enrutador enrutador;
    static LayoutInflater manager_xml;
    private JSONArray Data_actual = new JSONArray();
    private ScrollView scroll_data;
    private Button cargar_pagos;

    private ScrollView config;

    public Lista_View (Context context, AttributeSet atr){

        super(context, atr);
        inicializar();
    }
    public Lista_View (Context context){

        super(context);
        inicializar();

    }

    private void inicializar () {
        enrutador = new Enrutador(getContext());

        manager_xml = LayoutInflater.from(this.getContext());

        manager_xml.inflate(R.layout.list_view, this, true);

        scroll_data = this.findViewById(R.id.scroll_data);
        cargar_pagos = this.findViewById(R.id.cargar_pagos);
        config = this.findViewById(R.id.config);


        cargar_pagos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                enrutador.traer_data((json) -> {agregar_datos(json);});
            }
        });
    }

    private View llenar_item (View item, JSONObject datos){


        try {
            TextView nombre = item.findViewById(R.id.nombre);
            nombre.setText(datos.getString("nombre"));

            TextView monto = item.findViewById(R.id.monto);
            monto.setText(datos.getString("monto"));

            TextView fecha = item.findViewById(R.id.fecha);
            fecha.setText(datos.getString("fecha"));

            Log.d("Errore al llenar", fecha.getText().toString() + nombre.getText().toString() + monto.getText().toString());


        } catch (Exception e) {
            Log.d("Errore al llenar", e.getMessage());
        }


        return item;

    }

    public void agregar_datos (JSONArray data, String type_data) {


        ViewGroup root_layout = (ViewGroup) scroll_data.getChildAt(0);
        boolean new_state = type_data.equals("data");
        scroll_data.setTag(new_state);

        int num_datos = data.length();
        int num_item = root_layout.getChildCount();
        int resto = num_item - num_datos;
        JSONObject trans_data;
        View item;

        for (int i = 0; i < num_datos; i++) {

            try {
                trans_data = data.getJSONObject(i);
                item = root_layout.getChildAt(i);

                if (item == null){
                    int recurso = (i == 0 && type_data.equals("data")) ? R.layout.itemgrande: R.layout.itemnormal;
                    item = manager_xml.inflate(recurso, null);
                    llenar_item(item, trans_data);
                    root_layout.addView(item);
                    Log.d("TEST :: LIS_VIEW:::" , "NUME ITEM INFLADO LLENADO::" + i);

                }

                else {
                    llenar_item(item, trans_data);
                    item.setVisibility(VISIBLE);
                    Log.d("TEST :: LIS_VIEW:::" , "NUME ITEM REEEMPLAZADO LLENADO::" + i);

                }


            }
            catch (Exception e) {
                Log.d("TEST :: LIS_VIEW:::" , e.getMessage());
            }

        }

        if (resto > 0){
            for (int i = num_datos; i < num_item; i++) {
                try {
                    root_layout.getChildAt(i).setVisibility(GONE);
                }
                catch (Exception e){
                    Log.d("TEST :: LIS_VIEW:::" , e.getMessage() + " " + resto + " "  + i);
                }
            }
        }


        if (type_data.equals("data")) {
            Data_actual = data;
        }
    }

    public void agregar_datos (JSONArray data) {
        agregar_datos(data, "data");
    }


    public void filtrar_data (String filtro) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Realizar la tarea en segundo plano aquí
                JSONArray data_filtrada = new JSONArray();

                for (int i = 0; i < Data_actual.length(); i++) {

                    try {
                        JSONObject jsonObject = Data_actual.getJSONObject(i);

                        // Verificar la condición en la data del JSONObject
                        if (jsonObject.getString("nombre").contains(filtro)) {
                            data_filtrada.put(jsonObject);

                        } else if (jsonObject.getString("monto").contains(filtro)) {
                            data_filtrada.put(jsonObject);

                        } else if (jsonObject.getString("fecha").contains(filtro)) {
                            data_filtrada.put(jsonObject);

                        }

                    }
                    catch (Exception e){}

                }


                // Comunicarte con el hilo principal para actualizar la interfaz de usuario
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        agregar_datos(data_filtrada, "data_filtrada");
                        Log.d("TEST :: LIS_VIEW:::" , String.valueOf(data_filtrada.length()));
                        //animar_filtro(true);
                    }
                });
            }
        }).start();

    }


    public void data_default () {
        scroll_data.setTag(true);
        agregar_datos(Data_actual);
    }

    public void animar_config (Boolean flag) {

        animar_view(config, flag);
        animar_view(cargar_pagos, !flag);

        if (!(boolean) scroll_data.getTag()) {
            data_default();
        }

    }

    public void animar_button (Boolean flag) {
        animar_view(cargar_pagos, flag);
    }


    private void animar_view (View witget, boolean flag) {

        int new_status = (flag)? View.VISIBLE : View.GONE;
        float valor = (flag)? 1.0f : 0.0f;
        int duracion = 100;

        if (flag){
            witget.setVisibility(new_status);
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        }

        else {
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(()-> {
                witget.setVisibility(new_status);
            });
        }


    }



}
