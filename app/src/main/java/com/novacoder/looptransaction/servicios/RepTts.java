package com.novacoder.looptransaction.servicios;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class RepTts implements TextToSpeech.OnInitListener {

    static private Context contexto;
    static private TextToSpeech textToSpeech;
    private RepTts (){
    }

    static public void inicializar (Context context) {
        contexto = context;
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(contexto, new RepTts());
        }
    }

    static public void Speak (String texto){

        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(contexto, new RepTts());
        }
        textToSpeech.speak(texto, TextToSpeech.QUEUE_ADD, null, null);
        textToSpeech.stop();
        textToSpeech.shutdown();
        textToSpeech = null;
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS se ha inicializado correctamente
            int result = textToSpeech.setLanguage(Locale.getDefault());

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
    }
}
