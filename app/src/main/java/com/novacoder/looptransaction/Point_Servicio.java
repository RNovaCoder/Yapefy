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

public class Point_Servicio extends NotificationListenerService {

    static private Context serv_contexto;
    private Thread Th_servicio;

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

        serv_contexto = getApplicationContext();

        //Pasando el contexto necesario para inicializar los servicios
        FE_RepositoryLocal.inicializar(serv_contexto);
        FE_RepositoryRemoto.inicializar(serv_contexto);
        Reproductor.inicializar(serv_contexto);
        RepTts.inicializar(serv_contexto);

        //El servicio se ha conectado correctamente
        iniciar_servicio();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void iniciar_servicio (){

        Th_servicio = new Thread(new Runnable(){
            public void run() {

                ArrayList<Transaccion> transacciones;
                StatusBarNotification[] notificaciones_activas;

                while(true) {

                    //Recupera todas las notificaciones
                    notificaciones_activas = getActiveNotifications();

                    //Filtra todas las notficcaiones de un tipo de Transacción
                    transacciones = FactoryTransacciones.filtrar_transacciones(notificaciones_activas);

                    //Guarda las transacciones en la BD Local
                    FE_RepositoryLocal.agregar_registros(transacciones);

                    //Recuperar las transacciones a enviar de la BD Local
                    transacciones = FE_RepositoryLocal.transacciones_a_enviar();

                    //Envía las transacciones a la BD remota
                    FE_RepositoryRemoto.enviar_registros(transacciones);

                    //Pausa de 300 ms
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
        Th_servicio.start();

    }

}
