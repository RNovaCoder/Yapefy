package com.novacoder.looptransaction.IUcomponents.app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.novacoder.looptransaction.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscador extends LinearLayout {

    static public LayoutInflater manager_xml;
    public InputMethodManager imm = (InputMethodManager) ContextCompat.getSystemService(getContext(), InputMethodManager.class);
    Map<String, ArrayList<Listeners>> escuchadores = new HashMap<>();
    private EditText input;
    private ImageButton config;
    private ImageButton lupa;
    private TextView bar_titlee;
    private String[] eventos = {"input_search", "toggle_lupa", "toggle_config"};
    public Buscador(Context contexto, AttributeSet attrs) {
        super(contexto, attrs);
        inicializar();
    }

    public Buscador(Context contexto) {
        super(contexto);
        inicializar();
    }

    private void inicializar() {
        for (String evento : eventos) {
            escuchadores.put(evento, new ArrayList<>());
        }

        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.buscador, this, true);
        setBackgroundColor(0xFF600773);

        lupa = this.findViewById(R.id.lupa);
        input = this.findViewById(R.id.input);
        config = this.findViewById(R.id.cont_config);
        bar_titlee = this.findViewById(R.id.bar_titlee);


        lupa.setTag(true);
        config.setTag(true);


        lupa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean new_state = !(boolean) view.getTag();

                view.setTag(new_state);

                animate_view(bar_titlee, new_state);
                animate_view(input, !new_state);

                if (!new_state) {
                    lupa.setImageResource(R.drawable.icon_close);
                    input.requestFocus();
                } else {
                    lupa.setImageResource(R.drawable.icon_search);
                    input.clearFocus();
                }

                toogle_teclado(!new_state);

                input.setText("");

                eje_listeners("toggle_lupa", lupa);

            }
        });

        config.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean new_state = !(boolean) view.getTag();

                if (new_state) {
                    config.setImageResource(R.drawable.icon_config);
                } else {
                    config.setImageResource(R.drawable.icon_close);
                }

                view.setTag(new_state);
                lupa.setTag(true);

                animate_view(lupa, new_state);
                animate_view(bar_titlee, true);
                animate_view(input, false);
                lupa.setImageResource(R.drawable.icon_search);
                input.clearFocus();
                toogle_teclado(false);

                eje_listeners("toggle_config", view);

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

                if ((s.toString()).length() >= 3) {
                    eje_listeners("input_search", input);
                }

            }
        });
    }

    private void animate_view(View witget, Boolean flag) {

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

    public void add_listener(String evento, Listeners escuchador) {
        ArrayList<Listeners> list_evento = escuchadores.get(evento);
        list_evento.add(escuchador);
    }

    private void eje_listeners(String evento, View target) {

        ArrayList<Listeners> list_evento = escuchadores.get(evento);
        for (Listeners accion : list_evento) {
            accion.run(target);
        }

        //Log.d("EVENTO EJECUTADO::  " , evento);

    }

    private void toogle_teclado(boolean flag) {

        new Thread(() -> {
            if (flag) {
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
            } else {
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        }).start();

    }

    @FunctionalInterface
    public interface Listeners {
        void run(View target);
    }

}
