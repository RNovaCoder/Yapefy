package com.novacoder.looptransaction.servicios;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Reproductor {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    static private ArrayList<String> sounds = new ArrayList();
    static private Thread th_notificationes;
    static private Context contexto;
    static private Reproductor instancia;

    private Reproductor() {
        //Iniciar Hilo
        escuchador();
    }

    static public void inicializar(Context context) {
        if (instancia == null) {
            contexto = context;
            instancia = new Reproductor();
        }
    }

    static public void destruir() {
        contexto = null;
        instancia = null;
    }


    static public void notificar_audio(String audio) {
        sounds.add(audio);
    }

    public void escuchador() {

        //Configura la salida de audio por el canal de Notificaciones
        AudioAttributes audioAttributes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
        }


        //Inicio del Thread o servicio
        th_notificationes = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (sounds.size() != 0 && !(mediaPlayer.isPlaying())) {
                        play();
                    } else {
                        try {
                            Thread.sleep(300); // Pausar el hilo durante 300 milisegundos
                        } catch (InterruptedException e) {

                        }
                    }
                }
            }
        });
        th_notificationes.start();
    }


    private void play() {

        try {
            byte[] decodedByteArray = Base64.decode(sounds.get(0), Base64.DEFAULT);

            File file = new File(contexto.getExternalCacheDir(), "audio.mp3");
            FileOutputStream os = new FileOutputStream(file);
            os.write(decodedByteArray);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            sounds.remove(0);
            file.delete();

        } catch (Exception e) {
            Log.e("ERROR AUDIO", "AUDIO ERROR: : " + e.getMessage());
            sounds.remove(0);
        }
    }
}
