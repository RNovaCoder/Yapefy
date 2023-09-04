package com.novacoder.looptransaction.IUcomponents.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.novacoder.looptransaction.R;



public class AppView extends LinearLayout {

    static public LayoutInflater manager_xml;

    public AppView(@NonNull Context context) {
        super(context);
        inicializador();
    }

    public AppView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializador();
    }

    public void inicializador () {
        setOrientation(VERTICAL);

        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)
        );


        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.app_layout, this, true);

        SetListeners();

    }

    private void SetListeners (){

        Buscador buscador = this.findViewById(R.id.buscador);
        Cuerpo cuerpo = this.findViewById(R.id.lista);

        buscador.add_listener("input_search", (input) -> {
            cuerpo.filtrar_data(((EditText) input).getText().toString());
        });

        buscador.add_listener("toggle_lupa", (lupa) -> {

            boolean state = (boolean) lupa.getTag();
            cuerpo.animar_button(state);

            if (state) {
                cuerpo.data_default();
            }
        });

        buscador.add_listener("toggle_config", (config) -> {
            boolean state = (boolean) config.getTag();
            cuerpo.animar_config(!state);
        });
    }
}
