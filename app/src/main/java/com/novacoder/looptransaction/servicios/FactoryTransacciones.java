package com.novacoder.looptransaction.servicios;

import android.database.Cursor;
import android.service.notification.StatusBarNotification;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;
import com.novacoder.looptransaction.servicios.transacciones.yape.Yape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FactoryTransacciones {

    //Registro de todas las implementaciones de la interfaz Transacción
    static HashMap<String, Transaccion> implementaciones = new HashMap<String, Transaccion>(){{
        put(ConfigApp.YAPE_MODEL_NAME, new Yape());
    }};

    static public Transaccion crear_transaccion (String tipo) throws CloneNotSupportedException {
        Transaccion transaccion = implementaciones.get(tipo).clone();
        return transaccion;

    }

    static public Transaccion crear_transaccion (String tipo, Cursor cursor) throws Exception {

        Transaccion transaccion = implementaciones.get(tipo).clone();
        transaccion.set_data(cursor);
        return transaccion;

    }

    static public ArrayList<Transaccion> filtrar_transacciones (StatusBarNotification[] notificaciones) {

        //Array de las Transacciones finales
        ArrayList<Transaccion> transacciones = new ArrayList<>();

        //Array de todas las implementaciones
        ArrayList<StatusBarNotification> list_notificaciones = new ArrayList<>(Arrays.asList(notificaciones));

        for (Map.Entry<String, Transaccion> entry : implementaciones.entrySet()) {

            for (StatusBarNotification notificacion : list_notificaciones) {

                try {
                    Transaccion n_transaccion = entry.getValue().clone();
                    n_transaccion.set_data(notificacion);
                    transacciones.add(n_transaccion);
                    list_notificaciones.remove(notificacion);
                }
                catch (Exception e) {

                }
            }
        }

        return transacciones;

    }


}
