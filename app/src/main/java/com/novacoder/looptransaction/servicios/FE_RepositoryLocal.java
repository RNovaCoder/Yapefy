package com.novacoder.looptransaction.servicios;

import android.content.Context;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.repository.RepositoryLocal;
import com.novacoder.looptransaction.servicios.repository.implementaciones.RepositoryLocal1;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public class FE_RepositoryLocal {

    static public Context contexto;
    static private RepositoryLocal repo_local;
    static public String ESTADO_ENVIADO = ConfigApp.ESTADO_ENVIADO;
    static public String ESTADO_DUPLICADO = ConfigApp.ESTADO_DUPLICADO;
    static public String ESTADO_REGISTRADO = ConfigApp.ESTADO_REGISTRADO;
    static public String ESTADO_PENDIENTE = ConfigApp.ESTADO_PENDIENTE;
    static public String ESTADO_INVALIDO = ConfigApp.ESTADO_INVALIDO;


    static public void inicializar(Context context) {
        contexto = context;
        repo_local = RepositoryLocal1.getInstance(contexto);
    }

    static public void agregar_registros(ArrayList<Transaccion> transacciones) {
        repo_local.agregar_registros(transacciones);
    }

    ;

    static public ArrayList<Transaccion> transacciones_a_enviar() {
        return repo_local.transacciones_a_enviar();
    }

    static public void set_estado_enviado(Transaccion transaccion) {
        repo_local.transaction_set_estado(transaccion, ESTADO_ENVIADO);
    }

    static public void set_estado_duplicado(Transaccion transaccion) {
        repo_local.transaction_set_estado(transaccion, ESTADO_DUPLICADO);
    }

    static public void set_estado_registrado(Transaccion transaccion) {
        repo_local.transaction_set_estado(transaccion, ESTADO_REGISTRADO);
    }

    static public void set_estado_pendiente(Transaccion transaccion) {
        repo_local.transaction_set_estado(transaccion, ESTADO_PENDIENTE);
    }

    static public void set_estado_invalido(Transaccion transaccion) {
        repo_local.transaction_set_estado(transaccion, ESTADO_INVALIDO);
    }

    static public void transaction_set_estado(Transaccion transaccion, String estado) {
        repo_local.transaction_set_estado(transaccion, estado);
    }

    static public void limpiar_registros() {
        repo_local.limpiarRegistro();
    }


}
