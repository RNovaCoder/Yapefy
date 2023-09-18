package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemDataYape {
    private String monto;
    private String fecha;
    private String nombre;

    private List<String> atributos;

    public ItemDataYape(String nombre, String monto, String fecha) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;

        this.atributos = new ArrayList<String>() {{
            add(nombre);
            add(monto);
            add(fecha);}};
    }

    public String get_nombre() {
        return nombre;
    }
    public String get_monto() {
        return formatMont(monto);
    }
    public String get_fecha() {return fecha;}


    public boolean comprobar (String filtro){

        filtro = filtro.toLowerCase().replaceAll("\\s", "");

        for (String valor: atributos) {
            valor = valor.toLowerCase().replaceAll("\\s", "");
            if (valor.contains(filtro)) {
                return true;
            }
        }

        return false;
    }

    public String formatMont(String monto) {
        return "S/ " + monto;
    }

}
