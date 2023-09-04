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
    public String get_fecha() {
        return formatDate(fecha);
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

    public String formatDate(String fechaOriginal) {
        try {
            SimpleDateFormat sdfOriginal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date fechaPublicacion = sdfOriginal.parse(fechaOriginal);


            SimpleDateFormat sdfNuevo = new SimpleDateFormat("d MMM. yyyy - h:mm a",Locale.getDefault());
            String fechaFormateada = sdfNuevo.format(fechaPublicacion);

            // Corregir el doble punto después del mes
            fechaFormateada = fechaFormateada.replace("..", ".");

            // Capitalizar la primera letra del mes
            String[] partes = fechaFormateada.split(" ");
            if (partes.length >= 2) {
                partes[1] = partes[1].substring(0, 1).toUpperCase() + partes[1].substring(1);
                fechaFormateada = partes[0] + " " + partes[1] + " " + partes[2] + " " + partes[3] + " " + partes[4] + " " + partes[5];
                fechaFormateada = fechaFormateada.replaceAll("\\s+", " ");
                fechaFormateada = (fechaFormateada.replace("p. m.", "pm")).replace("a. m.", "am");
            }

            return fechaFormateada;
        } catch (ParseException e) {

            return "";
        }
    }

    public String formatMont(String monto) {
        return "S/ " + monto;
    }

}
