package com.novacoder.looptransaction.servicios.transacciones.yape;


import android.content.ContentValues;

import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.servicios.transacciones.Operaciones;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YapeOperaciones extends Operaciones {


    static private HashMap<String, String> ssml_remoto_template = new HashMap<String, String>() {{
        put("c"  , "<speak> <prosody volume=\"+7dB\" pitch=\"+5%\" rate=\"90%\"> ' Nuevo Yápe ! ' </prosody> <emphasis level=\"strong\"> <prosody volume=\"+6dB\" pitch=\"-10%\" rate=\"86%\">\" dé  \"</prosody> <break time=\"150ms\"/> <prosody pitch=\"-5%\" rate=\"83%\" volume=\"+4.7dB\">' V_nombres '</prosody> </emphasis> <break time=\"200ms\"/><emphasis level=\"strong\"><prosody volume=\"+5dB\"   pitch=\"-5%\" rate=\"65%\"> por, </prosody> <break time=\"80ms\"/><prosody volume=\"+5.7dB\" pitch=\"+5%\" rate=\"81%\">  V_centimos céntimos  </prosody></emphasis> </speak>");
        put("s"  , "<speak> <prosody volume=\"+7dB\" pitch=\"+5%\" rate=\"90%\"> ' Nuevo Yápe ! ' </prosody> <emphasis level=\"strong\"> <prosody volume=\"+6dB\" pitch=\"-10%\" rate=\"86%\">\" dé  \"</prosody> <break time=\"150ms\"/> <prosody pitch=\"-5%\" rate=\"83%\" volume=\"+4.7dB\">' V_nombres '</prosody> </emphasis> <break time=\"200ms\"/><emphasis level=\"strong\"><prosody volume=\"+3.4dB\" pitch=\"+5%\" rate=\"80%\"> por </prosody><break time=\"100ms\"/> <prosody volume=\"+5.7dB\" pitch=\"+5%\" rate=\"67%\"> un sol.  </prosody></emphasis> </speak>");
        put("ss" , "<speak> <prosody volume=\"+7dB\" pitch=\"+5%\" rate=\"90%\"> ' Nuevo Yápe ! ' </prosody> <emphasis level=\"strong\"> <prosody volume=\"+6dB\" pitch=\"-10%\" rate=\"86%\">\" dé  \"</prosody> <break time=\"150ms\"/> <prosody pitch=\"-5%\" rate=\"84%\" volume=\"+4.7dB\">' V_nombres '</prosody> </emphasis> <break time=\"200ms\"/><emphasis level=\"strong\"><prosody volume=\"+4dB\"   pitch=\"+5%\" rate=\"73%\"> por </prosody> <prosody volume=\"+5.7dB\" pitch=\"+5%\" rate=\"81%\">  V_soles soles.  </prosody></emphasis> </speak>");
        put("sc" , "<speak> <prosody volume=\"+7dB\" pitch=\"+5%\" rate=\"90%\"> ' Nuevo Yápe ! ' </prosody> <emphasis level=\"strong\"> <prosody volume=\"+6dB\" pitch=\"-10%\" rate=\"86%\">\" dé  \"</prosody> <break time=\"150ms\"/> <prosody pitch=\"-5%\" rate=\"83%\" volume=\"+4.7dB\">' V_nombres '</prosody> </emphasis> <break time=\"200ms\"/><emphasis level=\"strong\"><prosody volume=\"+3.4dB\" pitch=\"+5%\" rate=\"80%\"> por </prosody><break time=\"100ms\"/> <prosody volume=\"+5.7dB\" pitch=\"+5%\" rate=\"67%\"> un sol,  </prosody></emphasis> <emphasis level=\"strong\"> <break time=\"70ms\"/> <prosody volume=\"+4.4dB\" pitch=\"+3%\" rate=\"75%\">' con V_centimos ' </prosody></emphasis> </speak>");
        put("ssc", "<speak> <prosody volume=\"+7dB\" pitch=\"+5%\" rate=\"90%\"> ' Nuevo Yápe ! ' </prosody> <emphasis level=\"strong\"> <prosody volume=\"+6dB\" pitch=\"-10%\" rate=\"86%\">\" dé  \"</prosody> <break time=\"150ms\"/> <prosody pitch=\"-5%\" rate=\"83%\" volume=\"+4.7dB\">' V_nombres '</prosody> </emphasis> <break time=\"200ms\"/><emphasis level=\"strong\"><prosody volume=\"+4dB\"   pitch=\"+5%\" rate=\"75%\"> por </prosody> <prosody volume=\"+5.7dB\" pitch=\"+5%\" rate=\"83%\"> V_soles soles,  </prosody></emphasis> <emphasis level=\"strong\"> <break time=\"50ms\"/> <prosody volume=\"+5dB\" pitch=\"+3%\" rate=\"88%\">' con V_centimos.' </prosody></emphasis> </speak>");
    }};

    @Override
    public JSONObject crear_postjson (Transaccion transaccion) {

        Yape yape = (Yape) transaccion;
        JSONObject postData = new JSONObject();

        try {
            postData.put("tipo", yape.get_tipo());
            postData.put("transaccion_id", yape.transaccion_id);
            postData.put("nombre", yape.nombre);
            postData.put("monto", yape.monto);
            postData.put("fecha", yape.fecha);

            if (ConfigApp.get(ConfigApp.KEY_SONIDO).equals(ConfigApp.SONIDO_REMOTO)){
                postData.put("ssml", yape.ssml_remoto());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData;
    }



    @Override
    public ContentValues format_sql (Transaccion transaccion) {

        Yape yape = (Yape) transaccion;

        ContentValues values = new ContentValues();
        //por DEFAULT
        values.put("transaccion_id", yape.transaccion_id);
        values.put("estado", this.ESTADO_PENDIENTE);

        //Propios
        values.put("nombre", yape.nombre);
        values.put("monto", yape.monto);
        values.put("fecha", yape.fecha);

        return values;
    }


    @Override
    public String ssml_local(Transaccion transaccion) {
        return applyRateSum(ssml_remoto(transaccion), 30);
    }

    @Override
    public String ssml_remoto(Transaccion transaccion) {

        Yape yape = (Yape) transaccion;
        String nombres = yape.nombre;
        String monto = yape.monto;
        int punto = monto.indexOf(".");
        int soles = Integer.parseInt(monto.substring(0, punto));
        int cent = Integer.parseInt(monto.substring(punto + 1));

        String template = "";

        if (soles > 1 && cent == 0) {
            // Soles
            template = ssml_remoto_template.get("ss");
            template = template.replace("V_soles", String.valueOf(soles));

        } else if (soles == 0 && cent != 0) {
            // Céntimos
            template = ssml_remoto_template.get("c");
            template = template.replace("V_centimos", String.valueOf(cent));

        } else if (soles > 1 && cent != 0) {
            // Soles y céntimos
            template = ssml_remoto_template.get("ssc");
            template = template.replace("V_soles", String.valueOf(soles));
            template = template.replace("V_centimos", String.valueOf(cent));

        } else if (soles == 1 && cent != 0) {
            // Sol y céntimos
            template = ssml_remoto_template.get("sc");
            template = template.replace("V_centimos", String.valueOf(cent));

        } else if (soles == 1 && cent == 0) {
            // Sol
            template = ssml_remoto_template.get("s");
        }

        template = template.replace("V_nombres", nombres);

        return template;

    }

    public static String applyRateSum(String ssml, int sumaRate) {
        // Regular expression to match rate attributes
        String ratePattern = "rate=\"([\\d.]+)%\"";
        Pattern pattern = Pattern.compile(ratePattern);
        Matcher matcher = pattern.matcher(ssml);

        // Replace rate attributes with updated values
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            double rateValue = Double.parseDouble(matcher.group(1)) + sumaRate;
            int roundedRate = (int) Math.round(rateValue);
            matcher.appendReplacement(sb, "rate=\"" + roundedRate + "%\"");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }



}
