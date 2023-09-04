package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.novacoder.looptransaction.R;

public class AuthConfig extends LinearLayoutCompat {

    private LayoutInflater manager_xml;
    private TextView email;

    public AuthConfig(@NonNull Context context) {
        super(context);
        inicializar();
    }

    public AuthConfig(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    private void inicializar () {
        manager_xml = LayoutInflater.from(getContext());
        manager_xml.inflate(R.layout.auth_config, this);

        setOrientation(VERTICAL);
    }

    public void prepareAuth (String email) {
        this.email = findViewById(R.id.email);
        this.email.setText(email);
    }
}
