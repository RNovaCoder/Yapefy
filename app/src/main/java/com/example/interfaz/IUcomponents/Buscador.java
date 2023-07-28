package com.example.interfaz.IUcomponents;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.interfaz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscador extends LinearLayout {

    static public LayoutInflater manager_xml;

    private boolean input_state;
    private boolean config_state;

    private Button busqueda;
    private Button config;
    private Button cerrar;
    private EditText input;
    private FrameLayout cont_button;

    @FunctionalInterface
    public interface Listeners {
        void run(View target);
    }

    Map<String, ArrayList<Listeners>> escuchadores = new HashMap<>();
    private String[] eventos = {"input_search", "input_ocultado", "input_mostrado"};

    private void inicializar () {
        for (String evento : eventos) {
            escuchadores.put(evento, new ArrayList<>());
        }

        input_state = true;
        config_state = true;


        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.buscador, this, true);


        setBackgroundColor(0xFF600773);


        busqueda =  this.findViewById(R.id.busqueda);
        config =  this.findViewById(R.id.config);
        input =  this.findViewById(R.id.input);
        cerrar =  this.findViewById(R.id.cerrar);
        cont_button = this.findViewById(R.id.cont_button);


        cont_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                animate_view(input, !input_state);
                animate_view(busqueda, input_state);

                String evento = (!input_state)? "input_mostrado":"input_ocultado";
                ejecutar_eventos(evento, cont_button);

                input_state = !input_state;

            }
        });

        config.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                animate_view(input, config_state);
                animate_view(busqueda, !config_state);

                String evento = (!input_state)? "input_mostrado":"input_ocultado";
                ejecutar_eventos(evento, cont_button);

                config_state = !config_state;

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

                if (s.length() >= 4) {
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

        int status_visible = (flag)? View.VISIBLE : View.GONE;
        float valor = (flag)? 0.0f : 1.0f;

        witget.animate().alpha(valor).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                witget.setVisibility(status_visible);
            }
        });

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

    }



}
