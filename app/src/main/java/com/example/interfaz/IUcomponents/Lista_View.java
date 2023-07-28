package com.example.interfaz.IUcomponents;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private JSONArray Data_actual;
    private ScrollView scroll_data;
    private ScrollView scroll_data_filtrada;
    private Button cargar_pagos;
    private ConstraintLayout config;

    public Lista_View (Context context, AttributeSet atr){
        super(context, atr);
    }
    public Lista_View (Context context){

        super(context);
        enrutador = new Enrutador(getContext());

        manager_xml = LayoutInflater.from(this.getContext());

        manager_xml.inflate(R.layout.list_view, this, true);

        scroll_data = this.findViewById(R.id.scroll_data);
        scroll_data_filtrada = this.findViewById(R.id.scroll_data_filtrada);
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

        } catch (Exception e) {}

        return item;

    }

    public void agregar_datos (JSONArray data, String type_data) {

        ViewGroup root_layout = (type_data.equals("data"))? scroll_data: scroll_data_filtrada;
        root_layout = (ViewGroup) root_layout.getChildAt(0);


        int num_datos = data.length();
        int num_item = root_layout.getChildCount();
        int resto = num_item - num_datos;
        JSONObject trans_data = new JSONObject();
        View item;

        for (int i = 0; i < data.length(); i++) {

            try {
                trans_data = data.getJSONObject(i);
                item = root_layout.getChildAt(i);
                llenar_item(item, trans_data);

            }
            catch (Exception e) {

                int recurso = (i == 0 && type_data.equals("data")) ? R.layout.itemgrande: R.layout.itemnormal;
                item = manager_xml.inflate(recurso, null);
                llenar_item(item, trans_data);
                root_layout.addView(item);
            }

        }

        if (resto > 0){
            for (int i = num_item - resto; i < num_item; i++) {
                root_layout.removeViewAt(i);
            }
        }

        Data_actual = data;

    }

    public void agregar_datos (JSONArray data) {
        agregar_datos(data, "data");
    }


    public void filtrar_data (String filtro) {

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

        agregar_datos(data_filtrada, "data_filtrada");

    }


    private void animar_filtro (Boolean flag) {

        int status_visible = (flag)? View.VISIBLE : View.GONE;
        float valor = (flag)? 1.0f : 0.0f;


        scroll_data_filtrada.animate().alpha(valor).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                scroll_data_filtrada.setVisibility(status_visible);
            }
        });

    }

    private void animar_config (Boolean flag) {

        int status_visible = (flag)? View.VISIBLE : View.GONE;
        float valor = (flag)? 1.0f : 0.0f;

        config.animate().alpha(valor).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                scroll_data_filtrada.setVisibility(status_visible);
            }
        });

    }



}
