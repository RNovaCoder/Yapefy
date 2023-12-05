package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.R;

import java.util.Arrays;

public class ItemConfig extends LinearLayout {

    private LayoutInflater manager_xml;
    private TextView TituloView;
    private TextView ValorView;
    private String KeyIt;
    private String[] ValoresIt;
    private AlertConfig alert;

    public ItemConfig(Context context) {
        super(context);
        inicializar ();
    }

    public ItemConfig(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar ();
    }


    private void inicializar () {
        manager_xml = LayoutInflater.from(getContext());

        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)
        );


        manager_xml.inflate(R.layout.config_item, this);



        TituloView = (TextView) findViewById(R.id.itTitle);
        ValorView = (TextView) findViewById(R.id.itValor);

        alert = new AlertConfig(getContext());
    }
    public void PrepareItem (String Key, String [] Valores) {

        KeyIt = Key;
        ValoresIt = Valores;

        TituloView.setText(KeyIt);
        String valueAc = getConfigValue(KeyIt);
        ValorView.setText(valueAc);

        alert.setRunnableChangeItem(
                (newValue) -> {
                    setConfigValue(KeyIt, newValue);
                    ValorView.setText(getConfigValue(KeyIt));
                    alert.dismissAlert();
                }
        );

        alert.prepareAlert(Key, Valores, valueAc);


        this.setOnClickListener(view -> {
            alert.showAlert();
        });

    }

    public void PrepareItem (String Key, String [] Valores, int[] index) {

        KeyIt = Key;
        ValoresIt = Valores;

        TituloView.setText(KeyIt);
        String valueAc = getConfigValue(KeyIt);
        ValorView.setText(valueAc);

        alert.setRunnableChangeItem(
                (newValue) -> {
                    int posicion = Arrays.asList(ValoresIt).indexOf(newValue);
                    String valueIndex = String.valueOf(index[posicion]);
                    setConfigValue(KeyIt, valueIndex);
                    ValorView.setText(getConfigValue(KeyIt));
                    alert.dismissAlert();
                }
        );

        alert.prepareAlert(Key, Valores, valueAc);


        this.setOnClickListener(view -> {
            alert.showAlert();
        });

    }

    private String getConfigValue (String Key) {
        return ConfigApp.get(Key);
    }

    private void setConfigValue (String Key, String Value) {
        ConfigApp.set(Key, Value);
    }


}
