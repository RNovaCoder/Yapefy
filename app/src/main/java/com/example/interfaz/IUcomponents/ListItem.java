package com.example.interfaz.IUcomponents;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private String monto;
    private String fecha;
    private String nombre;

    private List<String> atributos;

    public ListItem(String nombre, String monto, String fecha) {
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
        return monto;
    }
    public String get_fecha() {
        return fecha;
    }


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

}
