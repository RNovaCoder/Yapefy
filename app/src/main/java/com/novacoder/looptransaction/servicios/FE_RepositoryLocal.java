package com.novacoder.looptransaction.servicios;

import android.content.Context;

import com.novacoder.looptransaction.servicios.repository.RepositoryLocal;
import com.novacoder.looptransaction.servicios.repository.implementaciones.RepositoryLocal1;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public class FE_RepositoryLocal {

    static public Context contexto;
    static private RepositoryLocal repo_local;

    static public void inicializar(Context context){
        contexto = context;
        repo_local = RepositoryLocal1.getInstance(contexto);
    }

    static public void agregar_registros (ArrayList<Transaccion> transacciones){
        repo_local.agregar_registros(transacciones);
    };

    static public ArrayList<Transaccion> transacciones_a_enviar() {
        return repo_local.transacciones_a_enviar();
    }

    static public void transaction_set_estado(Transaccion transaccion, String estado) {
        repo_local.transaction_set_estado(transaccion, estado);
    }


}
