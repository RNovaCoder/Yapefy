package com.example.interfaz.IUcomponents;


import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutCustom extends LinearLayoutManager {

    private static final int DURATION_MILLISECONDS = 500; // Duración deseada de la animación en milisegundos

    public LinearLayoutCustom(Context context) {
        super(context);
    }



    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int calculateTimeForScrolling(int dx) {
                return DURATION_MILLISECONDS;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                // Controla la velocidad del desplazamiento
                return 1f / displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}