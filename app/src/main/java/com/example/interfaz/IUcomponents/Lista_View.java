package com.example.interfaz.IUcomponents;

import android.content.Context;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.interfaz.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class Lista_View extends ConstraintLayout {

    static LayoutInflater manager_xml;
    private JSONArray Data_actual;
    private FrameLayout cont_data;
    private FrameLayout cont_data_filtrada;
    public Lista_View (Context context){

        super(context);

        manager_xml = LayoutInflater.from(this.getContext());

        View diseño_base = manager_xml.inflate(R.layout.list_view, null);

        this.addView(diseño_base);

        cont_data = this.findViewById(R.id.data);
        cont_data_filtrada = this.findViewById(R.id.data_filtrada);

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

    public void agregar_datos (JSONArray data, String type_data) {

        ViewGroup root_layout = (type_data.equals("data"))? cont_data: cont_data_filtrada;

        int num_datos = data.length();
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

                int recurso = (i == 0 && type_data.equals("data")) ? R.layout.itemgrande: R.layout.itemnormal;
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
        agregar_datos(data, "data");
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

        agregar_datos(data_filtrada, "data_filtrada");

    }


    private void animar_filtro (Boolean flag) {

        int status_visible = (flag)? View.VISIBLE : View.GONE;
        float valor = (flag)? 1.0f : 0.0f;

        cont_data_filtrada.animate().alpha(valor).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                cont_data_filtrada.setVisibility(status_visible);
            }
        });

    }



}
