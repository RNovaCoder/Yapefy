package com.novacoder.looptransaction.servicios;

import android.content.Context;

import com.novacoder.looptransaction.servicios.repository.RepositoryRemoto;
import com.novacoder.looptransaction.servicios.repository.implementaciones.RepositoryRemoto1;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import java.util.ArrayList;

public class FE_RepositoryRemoto {

    public static Context contexto;
    public static RepositoryRemoto repo_remoto;

    static public void inicializar(Context context) {
        contexto = context;
        repo_remoto = RepositoryRemoto1.getInstance(contexto);
    }

    public static void enviar_registros(ArrayList<Transaccion> transacciones_a_enviar) {
        repo_remoto.enviar_registros(transacciones_a_enviar);
    }
}