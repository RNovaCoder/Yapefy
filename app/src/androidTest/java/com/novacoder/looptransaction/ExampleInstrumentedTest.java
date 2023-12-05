package com.novacoder.looptransaction;


import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape.ItemDataYape;
import com.novacoder.looptransaction.servicios.RepTts;
import com.novacoder.looptransaction.servicios.repository.RepositoryLocal;
import com.novacoder.looptransaction.servicios.repository.implementaciones.RepositoryLocal1;
import com.novacoder.looptransaction.servicios.transacciones.Transaccion;
import com.novacoder.looptransaction.servicios.transacciones.yape.Yape;
import com.novacoder.looptransaction.servicios.transacciones.yape.YapeOperaciones;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    static public TextToSpeech ts;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("com.example.interfaz", appContext.getPackageName());
    }

    //@Test
    public void TtsRepro () throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RepTts.inicializar(appContext);

        Yape yape = new Yape();
        yape.monto = "12.50";
        yape.nombre = "Ricardo Daniel Villa";

        ts = new TextToSpeech(appContext, i -> {
            if (i == TextToSpeech.SUCCESS) {
                // TTS se ha inicializado correctamente
                int result = ts.setLanguage(Locale.getDefault());

                ts.setSpeechRate(0.1F);
                Bundle params = new Bundle();
                params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 5.0F);

                String ssml = yape.ssml_local();
                Log.d("String" , ssml);
                ts.speak(ssml , TextToSpeech.QUEUE_ADD, params, null);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // El idioma no es compatible o faltan datos de idioma
                    Log.e("TTS", "Language not supported");
                } else {
                    // TTS está listo para su uso
                    // Ahora puedes utilizar textToSpeech.speak() para leer el texto en voz alta
                }
            } else {
                // Error al inicializar TextToSpeech
                Log.e("TTS", "Initialization failed");
            }
        });


        Thread.sleep(10000);

    }

    @Test
    public void Leer() {
        // Context of the app under test.
        //RepositoryLocal1.leer_tabla("Yape");
        //assertEquals("com.example.interfaz", appContext.getPackageName());
    }

    @Test
    public void Diferent() {
        long difer = new RepositoryLocal() {
            @Override
            public void agregar_registros(ArrayList<Transaccion> transacciones) {}
            @Override
            public ArrayList<Transaccion> transacciones_a_enviar() {return null;}

            @Override
            public void limpiarRegistro() {

            }

            @Override
            public void transaction_set_estado(Transaccion transaction, String estado) {}
        }.dateDifference("2023-08-22 22:00:44");

    }

    @Test
    public void fechaFormat () {
        //ItemDataYape dat = new ItemDataYape("Ricardo", "20.50", "2023-08-25 13:26:28");
        //Log.d("FECHA FOMRATEADA", dat.get_fecha());
    }


}