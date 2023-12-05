package com.novacoder.looptransaction.servicios.contextos;

import java.lang.ref.WeakReference;

public class WrapperContext<T> extends WeakReference<T> {
    private Listener listener;

    public WrapperContext(T referent) {
        super(referent);
    }

    public interface Listener {
        void referenceCleared(WrapperContext context);
    }

    public void setRunClear(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void clear() {
        super.clear();
        if (listener != null) {
            listener.referenceCleared(this);
        }
    }
}
