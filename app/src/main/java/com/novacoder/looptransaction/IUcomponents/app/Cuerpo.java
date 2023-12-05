package com.novacoder.looptransaction.IUcomponents.app;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ScrollView;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.novacoder.looptransaction.Auth.Router;
import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape.AdapterYape;
import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape.ItemDataYape;
import com.novacoder.looptransaction.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Cuerpo extends ConstraintLayout {
    private LayoutInflater manager_xml;
    private SwipeRefreshLayout refreshLayout;
    private ScrollView cont_config;
    private RecyclerView lista;
    private AdapterYape adp_yape;

    public Cuerpo(Context context, AttributeSet atr) {
        super(context, atr);
        inicializar();
    }

    public Cuerpo(Context context) {
        super(context);
        inicializar();
    }

    private void inicializar() {
        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.cuerpo, this, true);
        refreshLayout = this.findViewById(R.id.swipe_refresh_layout);
        cont_config = this.findViewById(R.id.cont_config);
        lista = this.findViewById(R.id.lista_recycler);
        adp_yape = new AdapterYape();
        lista.setAdapter(adp_yape);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void filtrar_data(String filtro) {
        adp_yape.filtrar_data(filtro);
        lista.scrollToPosition(0);
    }

    public void visible(boolean flag) {
        animar_view(lista, flag);
        lista.scrollToPosition(0);
    }

    public void data_default() {
        adp_yape.detener_filter();
        adp_yape.default_data();
        lista.scrollToPosition(0);
    }

    public void set_data(List<ItemDataYape> data) {
        adp_yape.set_data(data);
        lista.scrollToPosition(0);
    }

    public void animar_config(Boolean flag) {
        animar_view(cont_config, flag);
        refreshLayout.setEnabled(!flag);
        data_default();
    }

    public void animar_button(Boolean flag) {
        refreshLayout.setEnabled(flag);
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
