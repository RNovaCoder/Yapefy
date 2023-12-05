package com.novacoder.looptransaction.servicios.contextos;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReactContext<T> {
    @FunctionalInterface
    public interface ListenersData<T> {
        void run(T data);
    }

    private T Data = null;
    private boolean State = true;
    private HashMap<WrapperContext<Context>, ArrayList<ListenersData>> ObserversData = new HashMap<>();

    public void setData(T data) {
        if (data != null) {
            data = Data;
            for (Map.Entry<WrapperContext<Context>, ArrayList<ListenersData>> entrada : ObserversData.entrySet()) {
                for (ListenersData listener : entrada.getValue()) {
                    listener.run(Data);
                }
            }
        }
    }

    public void ObserverData (Context context, ListenersData<T> listener) {

        WrapperContext<Context> KeyWrapperContext = FindRowContext(context);
        if (KeyWrapperContext != null) {
            ObserversData.get(KeyWrapperContext).add(listener);
        }

        else {
            WrapperContext<Context> contextWrapperContext = new WrapperContext<>(context);
            contextWrapperContext.setRunClear(contexto -> {
                WrapperContext<Context> KeyContext = FindRowContext(context);
                ObserversData.remove(KeyContext);
            });
            ArrayList<ListenersData> listenersData = new ArrayList<>();
            listenersData.add(listener);
            ObserversData.put(contextWrapperContext, listenersData);
        }

        if (Data != null) {
            listener.run(Data);
        }
    }

    public WrapperContext<Context> FindRowContext (Context context) {
        for (Map.Entry<WrapperContext<Context>, ArrayList<ListenersData>> entrada : ObserversData.entrySet()) {
            Context contexto = (entrada.getKey()).get();
            if (contexto == context) {
                WrapperContext<Context> key = entrada.getKey();
                return key;
            }
        }
        return null;
    }

}



