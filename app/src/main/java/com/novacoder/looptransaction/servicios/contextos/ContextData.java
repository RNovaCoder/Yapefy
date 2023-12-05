package com.novacoder.looptransaction.servicios.contextos;

import android.content.Context;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape.ItemDataYape;
import com.novacoder.looptransaction.servicios.api.BuilderRouter;
import com.novacoder.looptransaction.servicios.api.Router;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContextData {
    static ReactContext<List<ItemDataYape>> contextData = new ReactContext<List<ItemDataYape>>();
    static ReactContext<Boolean> state = new ReactContext<Boolean>();
    static Router<JSONArray> router = BuilderRouter.FetchData();

    static void ObserverData(Context context, ReactContext.ListenersData listener) {
        contextData.ObserverData(context, listener);
    }

    static void FetchData() {
        state.setData(false);
        router.setResponse(response -> {
            contextData.setData(ItemDataYape.JsonArrayToList(response));
        });
        router.setError(response -> {

        });
        router.setFinally(() -> {
            state.setData(true);
        });
        router.send();
    }

}
