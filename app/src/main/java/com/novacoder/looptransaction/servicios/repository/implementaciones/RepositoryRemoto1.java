package com.novacoder.looptransaction.servicios.repository.implementaciones;


import android.content.Context;

import com.android.volley.toolbox.Volley;
import com.novacoder.looptransaction.servicios.repository.RepositoryRemoto;

public class RepositoryRemoto1 extends RepositoryRemoto {

    static private Context contexto;
    static private RepositoryRemoto1 instancia;

    public static RepositoryRemoto1 getInstance (Context context) {
        if (instancia == null) {
            contexto = context;
            objet_http = Volley.newRequestQueue (contexto);
            instancia = new RepositoryRemoto1();
        }

        return instancia;
    }


}