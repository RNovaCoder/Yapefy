package com.example.interfaz.IUcomponents;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.interfaz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscador extends LinearLayout {

    static public LayoutInflater manager_xml;

    private EditText input;
    private Button config;
    private Button cerrar;
    private Button lupa;

    InputMethodManager imm = (InputMethodManager) ContextCompat.getSystemService(getContext(), InputMethodManager.class);


    private TextView bar_titlee;


    @FunctionalInterface
    public interface Listeners {
        void run(View target);
    }

    Map<String, ArrayList<Listeners>> escuchadores = new HashMap<>();
    private String[] eventos = {"input_search", "toggle_lupa", "toggle_config"};

    private void inicializar () {
        for (String evento : eventos) {
            escuchadores.put(evento, new ArrayList<>());
        }

        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.buscador, this, true);

        setBackgroundColor(0xFF600773);

        lupa =  this.findViewById(R.id.lupa);
        input =  this.findViewById(R.id.input);
        config =  this.findViewById(R.id.config);
        bar_titlee = this.findViewById(R.id.bar_titlee);


        lupa.setTag(true);
        config.setTag(true);


        lupa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean new_state = !(boolean) view.getTag();

                view.setTag(new_state);

                //Ver como cambiará el button en sí
                //animate_view(lupa, new_state);

                animate_view(bar_titlee, new_state);
                animate_view(input, !new_state);

                if (!new_state) {
                    input.requestFocus();
                }
                else {
                    input.clearFocus();
                }

                toogle_teclado(!new_state);


                input.setText("");

                ejecutar_eventos("toggle_lupa", lupa);


            }
        });

        config.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean new_state = !(boolean) view.getTag();
                view.setTag(new_state);
                animate_view(lupa, new_state);
                animate_view(bar_titlee, true);
                animate_view(input, false);

                ejecutar_eventos("toggle_config", view);


            }
        });


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if ((s.toString()).length() >= 4) {
                    ejecutar_eventos("input_search", input);
                }

            }
        });
    }




    public Buscador (Context contexto, AttributeSet attrs){
        super(contexto,attrs);
        inicializar();

    }

    public Buscador (Context contexto){
        super(contexto);
        inicializar();
    }



    private void animate_view (View witget, Boolean flag) {

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



    public void add_listener (String evento, Listeners escuchador) {

        ArrayList<Listeners> list_evento = escuchadores.get(evento);
        list_evento.add(escuchador);
    }

    private void ejecutar_eventos (String evento, View target){

        ArrayList<Listeners> list_evento = escuchadores.get(evento);
        for (Listeners accion : list_evento) {
            accion.run(target);
        }


        Log.d("EVENTO EJECUTADO::  " , evento);

    }

    private void toogle_teclado (boolean flag) {
        if (flag) {
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        }
        else {
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }

    }



}
