package com.novacoder.looptransaction.servicios.transacciones.yape;


import android.app.Notification;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import org.apache.commons.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Yape extends Transaccion {

    public String nombre;
    public String monto;
    public String fecha;

    static {
        operador = new YapeOperaciones();
        tipo = ConfigApp.YAPE_MODEL_NAME;
        columnas =
                "nombre TEXT, " +
                "monto TEXT, " +
                "fecha TEXT ";
    }

    public Yape (){}

    @Override
    public void set_data (Cursor cursor){

        _id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        transaccion_id = cursor.getLong(cursor.getColumnIndexOrThrow("transaccion_id"));
        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
        monto = cursor.getString(cursor.getColumnIndexOrThrow("monto"));
        fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
    }

    @Override
    public void set_data (StatusBarNotification notification) throws IllegalArgumentException {

        validar_data(notification);
        //Ya validado los datos, se llenan en el Objeto
        Notification body_notifi = notification.getNotification();

        transaccion_id = body_notifi.when;
        String mensaje = body_notifi.extras.getString(Notification.EXTRA_TEXT);
        fecha = llenar_fecha(transaccion_id);
        nombre = llenar_nombre(mensaje);
        monto = llenar_monto(mensaje);

    }


    private void validar_data (StatusBarNotification notification) throws IllegalArgumentException {

        //Comprobando que sea una notificacion de Yape
        String packName = String.valueOf(notification.getPackageName());
        if (!packName.equals("com.bcp.innovacxion.yapeapp")) {
            throw new IllegalArgumentException("No es una notificación de Yape");
        }
        //Es de Yape, comprobando que sea una notificación de Pago
        String titulo = String.valueOf(notification.getNotification().extras.getString(Notification.EXTRA_TITLE));
        if (!titulo.contains("n de Pago")) {
            throw new IllegalArgumentException("No es una notificación de Pago Yape");
        }
    }


    public String llenar_nombre (String mesj) {
        int pos_ini_nombre = mesj.indexOf("! ");
        int pos_fin_nombre = mesj.indexOf("te envió");

        String nombre = mesj.substring(pos_ini_nombre + 1, pos_fin_nombre -1);
        nombre = WordUtils.capitalizeFully(nombre).trim();

        return nombre;
    }

    public String llenar_monto (String mesj) {
        int pos_monto = mesj.indexOf("S/ ");

        String monto = mesj.substring(pos_monto + 3).trim();
        double numero = Double.parseDouble(monto);

        if (numero % 1 != 0) {
            monto = String.format(Locale.US, "%.2f", numero);
        }

        return monto;
    }

    public String llenar_fecha (Long id_fecha) {

        Date fechaPublicacion = new Date(id_fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fecha = sdf.format(fechaPublicacion);

        return fecha;
    }


}
