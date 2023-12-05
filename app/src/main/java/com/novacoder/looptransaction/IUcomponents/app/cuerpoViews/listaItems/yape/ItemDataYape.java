package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemDataYape {
    private String monto;
    public String fecha;
    private String nombre;

    private List<String> atributos;

    public ItemDataYape(String nombre, String monto, String fecha) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String get_nombre() {
        return nombre;
    }

    public String get_monto() {
        return monto;
    }

    public String get_fecha() {
        return fecha;
    }


    public boolean comprobar(String filtro) {

        filtro = formatFilter(filtro);

        if (formatFilter(get_nombre()).contains(filtro)) {
            return true;
        }
        if (formatFilter(get_fecha()).contains(filtro)) {
            return true;
        }
        if (formatFilter(get_monto()).contains(filtro)) {
            return true;
        }

        return false;
    }

    private String formatFilter(String filter) {
        return filter.toLowerCase().replaceAll("\\s", "");
    }

    static public List<ItemDataYape> JsonArrayToList(JSONArray data) {

        List<ItemDataYape> data_lista = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String nombre = (String) item.get("nombre");
                String fecha = (String) item.get("fecha_visual");
                String monto = "S/ " + (String) item.get("monto");
                data_lista.add(new ItemDataYape(nombre, monto, fecha));
            }
        } catch (Exception e) {
        }
        return data_lista;
    }

}
