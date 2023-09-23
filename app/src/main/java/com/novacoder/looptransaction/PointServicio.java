package com.novacoder.looptransaction;


import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.novacoder.looptransaction.servicios.FE_RepositoryLocal;
import com.novacoder.looptransaction.servicios.FE_RepositoryRemoto;
import com.novacoder.looptransaction.servicios.FactoryTransacciones;
import com.novacoder.looptransaction.servicios.RepTts;
import com.novacoder.looptransaction.servicios.Reproductor;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public class PointServicio extends NotificationListenerService {

    private Context serv_contexto;
    private Thread Th_servicio;
    private Thread Th_limpieza;
    boolean FlagService = true;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onListenerConnected();
        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        serv_contexto = this;

        //Pasando el contexto necesario para inicializar los sub-servicios
        RepTts.inicializar(serv_contexto);
        Reproductor.inicializar(serv_contexto);
        FE_RepositoryLocal.inicializar(serv_contexto);
        FE_RepositoryRemoto.inicializar(serv_contexto);

        //El servicio se ha conectado correctamente
        iniciar_servicio();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void iniciar_servicio() {

        Th_limpieza = new Thread(() -> {
            while (FlagService) {
                FE_RepositoryLocal.limpiar_registros();
                stop(15000);
            }
        });
        Th_limpieza.start();


        Th_servicio = new Thread(() -> {
            ArrayList<Transaccion> transacciones;
            StatusBarNotification[] notificaciones_activas;
            String Token;

            while (FlagService) {

                Token = ConfigApp.get(ConfigApp.KEY_TOKEN);

                //Recupera todas las notificaciones
                notificaciones_activas = getActiveNotifications();

                //Filtra todas las notficcaiones de un tipo de Transacción
                transacciones = FactoryTransacciones.filtrar_transacciones(notificaciones_activas);

                //Guarda las transacciones en la BD Local
                FE_RepositoryLocal.agregar_registros(transacciones);

                if (Token != null) {

                    //Log.d("Transacciones a guardar", String.valueOf(transacciones));

                    //Recuperar las transacciones a enviar de la BD Local
                    transacciones = FE_RepositoryLocal.transacciones_a_enviar();

                    //Log.d("Transacciones a enviar", String.valueOf(transacciones));

                    //Envía las transacciones a la BD remota
                    FE_RepositoryRemoto.enviar_registros(transacciones);

                }

                //Pausa de 300 ms
                stop(300);
            }

        });
        Th_servicio.start();
    }

    private void stop(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FlagService = false;
        FE_RepositoryLocal.contexto = null;
        FE_RepositoryRemoto.contexto = null;
        Reproductor.destruir();
        RepTts.destruir();
    }


}
