package com.example.interfaz.IUcomponents;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.interfaz.R;

public class App_View extends LinearLayout {

    static public LayoutInflater manager_xml;


    public App_View(@NonNull Context context) {
        super(context);
        inicializador();
    }

    public App_View(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializador();
    }


    public void inicializador () {




        setOrientation(LinearLayout.VERTICAL);

        // Establecer el ancho y alto a "match_parent" (LayoutParams.MATCH_PARENT)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Ancho: MATCH_PARENT (ocupará todo el ancho disponible)
                LinearLayout.LayoutParams.MATCH_PARENT  // Altura: MATCH_PARENT (ocupará todo el alto disponible)
        );
        setLayoutParams(layoutParams);


        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.app_actividad, this, true);




    }





}
