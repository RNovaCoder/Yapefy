package com.example.interfaz.IUcomponents;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interfaz.R;
import com.example.interfaz.servicios.Enrutador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Lista_View extends ConstraintLayout {

    static LayoutInflater manager_xml;
    private Button cargar_pagos;

    private ScrollView cont_config;

    private RecyclerView lista;
    private AdapterYape adp_yape;


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

        cargar_pagos = this.findViewById(R.id.cargar_pagos);
        cont_config = this.findViewById(R.id.cont_config);

        lista = this.findViewById(R.id.lista_recycler);






        adp_yape = new AdapterYape();
        lista.setAdapter(adp_yape);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));


        cargar_pagos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Enrutador.traer_data((json) -> {

                    set_data(json);

                });
            }
        });
    }

    
    public void filtrar_data(String filtro) {

        adp_yape.filtrar_data(filtro);
        lista.scrollToPosition(0);

    }



    public void data_default() {
        adp_yape.default_data();
        lista.scrollToPosition(0);

    }

    public void set_data(JSONArray data) {
        adp_yape.set_data(JsonToListObjetc(data));
        lista.scrollToPosition(0);
    }




    public void animar_config(Boolean flag) {

        animar_view(cont_config, flag);
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

    public List<ListItem> JsonToListObjetc (JSONArray data) {

        List<ListItem> data_lista =  new ArrayList<>();
        ListItem listItem;
        try {
            for (int i = 0; i < data.length() ; i++) {

                JSONObject item = data.getJSONObject(i);

                String nombre = (String) item.get("nombre");
                String fecha = (String) item.get("fecha");
                String monto = (String) item.get("monto");

                listItem = new ListItem(nombre, monto, fecha);

                data_lista.add(listItem);

            }
        }
        catch (Exception e) {

        }

        return data_lista;

    }


}
