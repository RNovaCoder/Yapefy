package com.example.interfaz.IUcomponents;

import android.content.Context;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.interfaz.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class Lista_View extends ConstraintLayout {

    static LayoutInflater manager_xml;
    private JSONArray Data_actual;

    public Lista_View (Context context){

        super(context);


        this.setLayoutParams(new ScrollView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));


        manager_xml = LayoutInflater.from(this.getContext());

        ViewGroup diseño_base = (ViewGroup) manager_xml.inflate(R.layout.list_view, null);
        this.addView(diseño_base.getChildAt(0));
        this.addView(diseño_base.getChildAt(1));


    }

    private View llenar_item (View item, JSONObject datos){

        try {
            TextView nombre = item.findViewById(R.id.nombre);
            nombre.setText(datos.getString("nombre"));

            TextView monto = item.findViewById(R.id.monto);
            monto.setText(datos.getString("monto"));

            TextView fecha = item.findViewById(R.id.fecha);
            fecha.setText(datos.getString("fecha"));

        } catch (Exception e) {}

        return item;

    }

    public void agregar_datos (JSONArray data, int layout) {

        int num_datos = data.length();
        ViewGroup root_layout = (ViewGroup) this.getChildAt(layout);
        root_layout = (ViewGroup) root_layout.getChildAt(0);

        int num_item = root_layout.getChildCount();
        int resto = num_item - num_datos;
        JSONObject trans_data = new JSONObject();
        View item;

        for (int i = 0; i < data.length(); i++) {

            try {
                trans_data = data.getJSONObject(i);
                item = root_layout.getChildAt(i);
                llenar_item(item, trans_data);

            }
            catch (Exception e) {

                int recurso = (i == 0 && layout == 0) ? R.layout.itemgrande: R.layout.itemnormal;
                item = manager_xml.inflate(recurso, null);
                llenar_item(item, trans_data);
                root_layout.addView(item);
            }

        }

        if (resto > 0){
            for (int i = num_item - resto; i < num_item; i++) {
                root_layout.removeViewAt(i);
            }
        }

        Data_actual = data;

    }

    public void agregar_datos (JSONArray data) {
        agregar_datos(data, 0);
    }


    public void filtrar_data (String filtro) {

        JSONArray data_filtrada = new JSONArray();

        for (int i = 0; i < Data_actual.length(); i++) {
            
            try {
                JSONObject jsonObject = Data_actual.getJSONObject(i);

                // Verificar la condición en la data del JSONObject
                if (jsonObject.getString("nombre").contains(filtro)) {
                    data_filtrada.put(jsonObject);

                } else if (jsonObject.getString("monto").contains(filtro)) {
                    data_filtrada.put(jsonObject);

                } else if (jsonObject.getString("fecha").contains(filtro)) {
                    data_filtrada.put(jsonObject);

                }

            }
            catch (Exception e){}

        }

        agregar_datos(data_filtrada, 1);

    }

}
