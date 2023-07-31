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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.interfaz.R;
import com.example.interfaz.servicios.Enrutador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;


public class Lista_View extends ConstraintLayout {

    static LayoutInflater manager_xml;
    private JSONArray Data_actual = new JSONArray();
    private LinearLayout contain_data;
    private Button cargar_pagos;

    private ScrollView config;

    private DataList model_data = new DataList();

    public Lista_View(Context context, AttributeSet atr) {

        super(context, atr);
        inicializar();
    }

    public Lista_View(Context context) {

        super(context);
        inicializar();

    }

    private void inicializar() {

        Enrutador.inicializar(getContext());

        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.list_view, this, true);

        contain_data = this.findViewById(R.id.contain_data);
        cargar_pagos = this.findViewById(R.id.cargar_pagos);
        config = this.findViewById(R.id.config);

        contain_data.setTag(false);

        View item_fantasma;
        for (int i = 0; i < 200; i++) {
            item_fantasma = manager_xml.inflate(R.layout.itemnormal, null);
            item_fantasma.setVisibility(GONE);
            contain_data.addView(item_fantasma);
        }

        cargar_pagos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Enrutador.traer_data((json) -> {

                    model_data.set_data(json);
                    data_default();

                });
            }
        });
    }

    private View llenar_item(View item, JSONObject datos) {

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

    public void pintar_datos (JSONArray data, boolean new_state) {

        View primer_item = contain_data.getChildAt(0);
        String tipo = (String) primer_item.getTag();

        if (!new_state && tipo.equals("grande")){
            contain_data.removeViewAt(0);
        }
        else if (new_state && tipo.equals("chico")) {
            contain_data.addView(manager_xml.inflate(R.layout.itemgrande, null), 0);
        }


        int num_datos = data.length();
        int num_item = contain_data.getChildCount();
        int resto = num_item - num_datos;
        JSONObject trans_data;
        View item;

        for (int i = 0; i < num_datos; i++) {

            try {
                trans_data = data.getJSONObject(i);
                item = contain_data.getChildAt(i);

                if (item == null) {
                    int recurso = (i == 0 && new_state) ? R.layout.itemgrande : R.layout.itemnormal;
                    item = manager_xml.inflate(recurso, null);
                    llenar_item(item, trans_data);
                    contain_data.addView(item);
                    //Log.d("TEST :: LIS_VIEW:::", "NUME ITEM INFLADO LLENADO::" + i);

                } else {
                    llenar_item(item, trans_data);
                    item.setVisibility(VISIBLE);
                    //Log.d("TEST :: LIS_VIEW:::", "NUME ITEM REEEMPLAZADO LLENADO::" + i);

                }


            } catch (Exception e) {
                Log.d("TEST :: LIS_VIEW:::", e.getMessage());
            }

        }

        if (resto > 0) {
            for (int i = num_datos; i < num_item; i++) {
                try {
                    contain_data.getChildAt(i).setVisibility(GONE);
                } catch (Exception e) {
                    Log.d("TEST :: LIS_VIEW:::", e.getMessage() + " " + resto + " " + i);
                }
            }
        }
    }

    
    public void eliminar_items () {

        int hijos = contain_data.getChildCount();
        contain_data.removeViews(200, hijos - 200);

    }
    
    
    public void filtrar_data(String filtro) {

        model_data.set_contador(0);
        model_data.set_estado(false);
        model_data.filtrar_data(filtro, () -> {
            pintar_datos(model_data.obtener_data(), false);
        });

    }


    public void data_default() {


        if (!model_data.state_data || model_data.get_contador() != 0) {
            model_data.set_estado(true);
            model_data.set_contador(0);
            pintar_datos(model_data.obtener_data(), model_data.state_data);
        }
        else {
            model_data.set_estado(true);
            model_data.set_contador(0);
        }



    }

    public void animar_config(Boolean flag) {

        animar_view(config, flag);
        animar_view(cargar_pagos, !flag);
        data_default();

    }

    public void animar_button(Boolean flag) {
        animar_view(cargar_pagos, flag);
    }


    private void animar_view(View witget, boolean flag) {

        int new_status = (flag) ? View.VISIBLE : View.GONE;
        float valor = (flag) ? 1.0f : 0.0f;
        int duracion = 100;

        if (flag) {
            witget.setVisibility(new_status);
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> {
                        witget.setVisibility(new_status);
                    });
        }


    }


}
