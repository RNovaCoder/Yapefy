package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.R;


public class Config extends LinearLayout {
    public LayoutInflater manager_xml;
    private LinearLayoutCompat layoutConfigs;

    public Config(Context context){
        super(context);
        inicializar ();

    }

    public Config(Context context, AttributeSet atr){
        super(context, atr);
        inicializar ();
    }

    private void inicializar (){

        manager_xml = LayoutInflater.from(getContext());
        manager_xml.inflate(R.layout.config, this, true);
        layoutConfigs = this.findViewById(R.id.layout_configs);

        //Banner de la Cuenta Google
        AuthConfig authConfig = new AuthConfig(getContext());
        authConfig.prepareAuth(ConfigApp.get(ConfigApp.KEY_G_CORREO));
        addView(authConfig, 0);

        ItemConfig ItConSonidos = new ItemConfig(getContext());
        ItConSonidos.PrepareItem(ConfigApp.KEY_SONIDO, ConfigApp.sonidos);
        addConfig(ItConSonidos);

        ItemConfig ItConModels = new ItemConfig(getContext());
        ItConModels.PrepareItem(ConfigApp.KEY_NAME_MODEL, ConfigApp.models);
        addConfig(ItConModels);

        InputConfig InpConUrlBack = new InputConfig(getContext());
        InpConUrlBack.PrepareInput(ConfigApp.KEY_URL_BACKEND, ConfigApp.URL_BACKEND);
        InpConUrlBack.setRestriction(InpConUrlBack.RES_WH_SPCS);
        addConfig(InpConUrlBack);

        /*InputConfig InpConToken = new InputConfig(getContext());
        InpConToken.PrepareInput(ConfigApp.KEY_TOKEN, null);
        InpConToken.setRestriction(InpConToken.RES_WH_SPCS);
        addConfig(InpConToken);*/

        InputConfig InpConCantTransc = new InputConfig(getContext());
        InpConCantTransc.PrepareInput(ConfigApp.KEY_NUM_TRANSACCIONES, ConfigApp.NUM_TRANSACCIONES);
        InpConCantTransc.setRestriction(InpConCantTransc.RES_NUMBS);
        addConfig(InpConCantTransc);

        addConfig(new ButtonOut(getContext()));


    }

    public void addConfig (View item) {
        Space space = new Space(getContext());
        int widthInPixels = ViewGroup.LayoutParams.MATCH_PARENT; // Ancho completo
        int heightInPixels = getResources().getDimensionPixelSize(R.dimen.space_height); // Altura en píxeles
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(widthInPixels, heightInPixels);
        space.setLayoutParams(layoutParams);

        layoutConfigs.addView(item);
        layoutConfigs.addView(space);
    }
}
