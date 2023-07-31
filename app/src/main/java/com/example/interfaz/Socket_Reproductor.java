package com.example.interfaz;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.media.AudioAttributes;
import android.media.MediaPlayer;

import android.os.IBinder;

import android.util.Base64;
import android.util.Log;


import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Socket_Reproductor extends Service {
    private final static String TAG = "MediaPlayer audios12345";
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public ArrayList<String> sounds = new ArrayList();

    public Thread socket, notif;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("SERVICIONE123", "ONSTARCOMAND");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mediaPlayer.setAudioAttributes(audioAttributes);

        escuchador();
        socket();
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        Log.d("SERVICIONE123", "ONCREATE");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void play() {


        try {
            byte[] decodedByteArray = Base64.decode(sounds.get(0), Base64.DEFAULT);

            File file = new File(getExternalCacheDir(), "audio.mp3");
            FileOutputStream os = new FileOutputStream(file);
            os.write(decodedByteArray);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            sounds.remove(0);

        }
        catch (Exception e) {
            Log.e(TAG, "IllegalArgumentException Unable to play audio : " + e.getMessage());
            sounds.remove(0);
        }
    }



    public void escuchador (){

         notif = new Thread(new Runnable(){
            public void run() {
                while(true) {
                    if (sounds.size() != 0 && !(mediaPlayer.isPlaying())){
                        Log.d("En Repro SAMPERAA", String.valueOf(sounds.get(0)));
                        play();
                    }
                    else {
                        try {
                            Thread.sleep(300); // Pausar el hilo durante 300 milisegundos
                        } catch (InterruptedException e) {

                        }
                    }
                }
            }
        });
        notif.start();
    }

    public void socket () {

        socket = new Thread(new Runnable(){
            public void run() {

                Socket sc;
                ServerSocket Server;
                final int Puerto = 3780;
                DataInputStream in;

                try {
                    Server = new ServerSocket(Puerto);

                    while (true	) {
                        Log.d("SOCKETINTERNET","SOCKET ABIERTO");
                        sc = Server.accept();
                        Log.d("SOCKETINTERNET","SOCKET ACEPTADO");
                        in = new DataInputStream(sc.getInputStream());


                        BufferedReader entrada = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                        String audio = entrada.readLine();
                        //Log.d("SOCKETINTERNET",audio);
                        sounds.add(audio);

                        /*
                        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        */

                        sc.close();
                    }

                } catch (Exception e) {
                    Log.d("SOCKETINTERNET",e.getMessage());

                }

            }


        });

        socket.start();

    }
}