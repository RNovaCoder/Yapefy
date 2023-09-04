package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.novacoder.looptransaction.R;

public class AlertConfig {

    public LayoutInflater manager_xml;
    private AlertDialog dialog;
    private RadioGroup radioGroup;
    private TextView title;
    private ObjRunnable Runnable;

    @FunctionalInterface
    public interface ObjRunnable {
        void ChangeItem (String newValue);
    }

    public AlertConfig (Context context) {
        inicializar(context);
    }

    private void inicializar (Context contexto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        dialog = builder.create();
        manager_xml = LayoutInflater.from(contexto);

        View dialogView = manager_xml.inflate(R.layout.alert_layout, null);
        radioGroup = dialogView.findViewById(R.id.radiogroup);

        title = dialogView.findViewById(R.id.RadioGroupTitle);

        dialog.setView(dialogView);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                if (radioButton != null) {
                    String selectedText = radioButton.getText().toString();
                    Runnable.ChangeItem(selectedText);
                }


            }
        });
    }

    public void showAlert () {
        dialog.show();
    }

    public void dismissAlert () {
        dialog.dismiss();
    }

    public void prepareAlert(String title, String [] valores, String valueAc){
        this.title.setText(title);

        radioGroup.removeAllViews();
        for (String valor: valores) {
            RadioButton radioButton = (RadioButton)  manager_xml.inflate(R.layout.radio_button, null);
            radioButton.setText(valor);
            radioGroup.addView(radioButton);
            if (valor.equals(valueAc)){ radioButton.setChecked(true);}
        }
    }



    public void setRunnableChangeItem (ObjRunnable runnable) {
        Runnable = runnable;
    }



}
