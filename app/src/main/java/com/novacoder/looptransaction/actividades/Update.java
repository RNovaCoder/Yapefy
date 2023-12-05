package com.novacoder.looptransaction.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.novacoder.looptransaction.IUcomponents.animations.Animator;
import com.novacoder.looptransaction.R;
import com.novacoder.looptransaction.servicios.TaskDiffer;
import com.novacoder.looptransaction.servicios.api.BuilderRouter;
import com.novacoder.looptransaction.servicios.api.Router;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Update extends AppCompatActivity {

    Button buttonUpdate;
    ConstraintLayout loader;
    String[] data = {""};
    Router<JSONObject> router = BuilderRouter.UpdateDate(data[0]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        loader = findViewById(R.id.loader);

        Intent intent = getIntent();
        Uri dataUri = intent.getData();

        new Thread(() -> {
            data[0] = (dataUri != null)? readCSV(dataUri) : "";
            Animator.visible(loader, false);
        }).start();

        router.setFinally(() -> {
            Animator.visible(loader, false);
        });

        buttonUpdate.setOnClickListener(view -> {
            Animator.visible(loader, true);
            router.send();
        });

        TaskDiffer task = new TaskDiffer();
        task.setTask((flag)->{
            return "dsa";
        });

        task.<String>setTaskUi((flag, params) -> {});

    }

    private String readCSV(Uri data) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            reader.close();
            inputStream.close();
            String contenidoCSV = stringBuilder.toString();
            return contenidoCSV;

        } catch (IOException e) {
            return "";
        }
    }
}