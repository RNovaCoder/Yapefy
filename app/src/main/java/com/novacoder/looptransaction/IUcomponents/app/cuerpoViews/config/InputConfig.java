package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.config;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.R;


public class InputConfig extends LinearLayout {

    private LayoutInflater manager_xml;

    static public int RES_NUMBS = 0;
    static public int RES_NUMBS_SPCS = 1;
    static public int RES_WH_SPCS = 2;

    private String DefaultValue;
    private String KeyInp;
    private TextView InpTitle;
    private EditText InpValue;
    private CheckBox InpDefault;
    public InputConfig(Context context) {
        super(context);
        inicializar();
    }

    public InputConfig(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }

    public void inicializar () {
        setOrientation(VERTICAL);

        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));


        manager_xml = LayoutInflater.from(getContext());

        manager_xml.inflate(R.layout.config_input, this);


        InpTitle = findViewById(R.id.InpTitle);
        InpValue = findViewById(R.id.InpValue);
        InpDefault = findViewById(R.id.InpDefault);

        InpDefault.setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    if (b){
                        setConfigValue(KeyInp, DefaultValue);
                        InpValue.setText(getConfigValue(KeyInp));
                    }
                    InpValue.setEnabled(!b);
                });

        InpValue.setOnFocusChangeListener(
                (view, b) -> {
                    if (!b) {
                        setConfigValue(KeyInp, InpValue.getText().toString());
                        InpValue.setText(getConfigValue(KeyInp));
                    }
                }
        );

    }

    public void PrepareInput (String KeyConfig, String defaultValue) {
        KeyInp = KeyConfig;
        InpTitle.setText(KeyInp);
        InpValue.setText(getConfigValue(KeyInp));
        DefaultValue = defaultValue;

        if (DefaultValue == null) {
            ((ViewGroup) InpDefault.getParent()).removeView(InpDefault);
        }
        else {
            if (getConfigValue(KeyConfig).equals(defaultValue)) {
                InpDefault.setChecked(true);
            }
        }

    }

    public void setRestriction (int restriction) {

        if (restriction == 0){
            InpValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (restriction == 1) {
            InpValue.setInputType(InputType.TYPE_CLASS_NUMBER);
            InpValue.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        } else if (restriction == 2) {

            InputFilter noSpacesFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (source.toString().contains(" ")) {return "";}
                    return null;
                }
            };
            InpValue.setFilters(new InputFilter[]{noSpacesFilter});
        }

    }

    private String getConfigValue (String Key) {
        return ConfigApp.get(Key);
    }

    private void setConfigValue (String Key, String Value) {
        ConfigApp.set(Key, Value);
    }






}
