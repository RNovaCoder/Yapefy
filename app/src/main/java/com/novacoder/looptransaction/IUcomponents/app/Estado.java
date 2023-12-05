package com.novacoder.looptransaction.IUcomponents.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import com.novacoder.looptransaction.R;



public class Estado extends ConstraintLayout {

    private LayoutInflater manager_xml;
    private ContentLoadingProgressBar progressBar;
    private AppCompatImageButton refreshButton;
    private AppCompatTextView estadoText;
    private Runnable callBackRefresh = () -> {};
    public Estado(@NonNull Context context) {
        super(context);
        inicializar();
    }
    public Estado(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    private void inicializar() {
        manager_xml = LayoutInflater.from(this.getContext());
        manager_xml.inflate(R.layout.estado, this, true);
        progressBar = this.findViewById(R.id.loader);
        estadoText = this.findViewById(R.id.estadoText);
        refreshButton = this.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> {callBackRefresh.run();});
    }

    public void setCallBackRefresh (Runnable callBack) {
        callBackRefresh = callBack;
    }

    public void setLoadIndeterminate () {
        estadoText.setText(getResources().getString(R.string.estado_actualizando));
        animar_view(this, true);
        animar_view(refreshButton, false);
        animar_view(progressBar, true);
    }

    public void refresh() {
        refreshButton.performClick();
    }
    public void setRefreshActive (String estado) {
        if (estado != null) {
            estadoText.setText(estado);
        }
        else {
            estadoText.setText(getResources().getString(R.string.estado_error));
        }
        animar_view(this, true);
        animar_view(progressBar, false);
        animar_view(refreshButton, true);
    }
    public void hiddenEstado () {
        animar_view(this, false);
        animar_view(refreshButton, false);
        animar_view(progressBar, true);
    }



    private void animar_view(View witget, boolean flag) {

        int new_status = (flag) ? View.VISIBLE : View.GONE;
        float valor = (flag) ? 1.0f : 0.0f;
        int duracion = 50;

        if (flag) {
            witget.setVisibility(new_status);
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            witget.animate().alpha(valor).setDuration(duracion)
                    .setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> {
                        witget.setVisibility(new_status);
                    });
        }
    }
}
