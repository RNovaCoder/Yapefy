package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.novacoder.looptransaction.actividades.Login;


public class ButtonOut extends LinearLayoutCompat {

    public AlertDialog dialog;
    public AppCompatButton buttonOut;
    public ButtonOut(@NonNull Context context) {
        super(context);
        inicializar ();
    }

    public ButtonOut(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar ();
    }

    private void inicializar (){
        setOrientation(VERTICAL);

        setLayoutParams(new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
            )
        );

        setGravity(Gravity.CENTER_HORIZONTAL);

        dialog = BuilderDialog();
        buttonOut = BuilderButton();

        addView(buttonOut);

    }

    private AlertDialog BuilderDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Seguro que desea cerrar sesión?")
                .setPositiveButton("Sí",(dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Login.Logout(getContext());
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

        return builder.create();
    }

    private AppCompatButton BuilderButton () {
        AppCompatButton button = new AppCompatButton(getContext());
        button.setText("Cerrar Sesión");
        button.setOnClickListener(view -> {
            dialog.show();
        });

        return button;
    }


}




