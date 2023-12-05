package com.novacoder.looptransaction.actividades;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.gson.reflect.TypeToken;
import com.novacoder.looptransaction.Auth.Router;
import com.novacoder.looptransaction.ConfigApp;
import com.novacoder.looptransaction.IUcomponents.app.Cuerpo;
import com.novacoder.looptransaction.IUcomponents.app.Estado;
import com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape.ItemDataYape;
import com.novacoder.looptransaction.MainActivity;
import com.novacoder.looptransaction.R;
import com.novacoder.looptransaction.servicios.Call_Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class App extends AppCompatActivity {
    private String KeyData = "DataList";
    private List<ItemDataYape> Data;
    Router router;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        router = new Router(this);
        setContentView(R.layout.activity_app);
        getSupportActionBar().hide();
        inicializar_data(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KeyData, ListToString(Data));
    }

    public void Logout() {
        Log.d("LogOut", "LOGOUT DESDE APP");
        try {
            FirebaseAuth.getInstance().signOut();
            GoogleSignInClient mSignInClient = GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
            mSignInClient.signOut().addOnCompleteListener(runnable -> {
                clearSesion(this);
            });
        } catch (Exception e) {
            clearSesion(this);
        }
    }

    private void clearSesion(Context context){
        String token = ConfigApp.get(ConfigApp.KEY_TOKEN);
        ConfigApp.set(ConfigApp.KEY_TOKEN, null);
        ConfigApp.set(ConfigApp.KEY_G_CORREO, "");

        if (token != null) {
            Router logout = new Router(context);
            logout.setLogout();
            logout.setResponse(response -> {
                try {
                    String msg = ((JSONObject) response).getString("msg");
                    //Log.d("Call AUTHa", msg);
                } catch (JSONException e) {
                }
            });
            logout.send();
        }

        Intent Main = new Intent(context, MainActivity.class);
        startActivity(Main);

    }

    public void inicializar_data (Bundle savedInstanceState) {
        Cuerpo Lista = findViewById(R.id.lista);
        SwipeRefreshLayout Refresh = findViewById(R.id.swipe_refresh_layout);
        Estado estado = findViewById(R.id.loading_progress_bar);

        router.setResponse(response -> {
            //Log.d("HTTP REQUEST", "RECIBIDO");
            Data = StringToListDefault((String) response);
            estado.hiddenEstado();
            //Log.d("HTTP REQUEST", "DESERIALIZADO");
            Lista.set_data(Data);
            Lista.visible(true);
        });

        router.setError(error -> {
            String message = error.getMessage();
            Call_Response.error_response(error, null, this);
            if (Data != null) {
                estado.post(()->{
                    String mensaje = "Revisa tu conexión a internet";
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                    Lista.visible(true);
                    estado.hiddenEstado();
                });
            } else {
                estado.post(()->{
                    estado.setRefreshActive("Revisa tu conexión a internet");
                });
            }

        });
        router.getData();


        Refresh.setOnRefreshListener(() -> {
            estado.post(() -> {
                estado.refresh();
            });
            Refresh.setRefreshing(false);
        });

        estado.setCallBackRefresh(() -> {
            if (router.send()) {
                //Log.d("HTTP REQUEST", "ENVIADO");
                Lista.visible(false);
                estado.setLoadIndeterminate();
            }
        });

        if (savedInstanceState != null) {
            //Log.d("Instanciaa: ", "NO NULL: " + String.valueOf(savedInstanceState));
            String DataString = savedInstanceState.getString(KeyData, null);
            if (!DataString.equals("null")){
                //Log.d("Instanciaa: ", "NO NULL: " + DataString);
                Lista.post(() -> {
                    Data = StringToListInstance(DataString);
                    Lista.set_data(Data);
                });
            } else {
                estado.post(()->{
                    estado.refresh();
                });
            }
        } else {
            //Log.d("Instanciaa: ", " NULL: " );
            estado.post(()->{
                estado.refresh();
            });
        }
    }

    public List<ItemDataYape> StringToListInstance(String data) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ItemDataYape>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    public List<ItemDataYape> StringToListDefault(String dataString) {

        JSONArray data;
        List<ItemDataYape> data_lista =  new ArrayList<>();
        ItemDataYape listItem;
        try {
            data = new JSONArray(dataString);
            for (int i = 0; i < data.length() ; i++) {

                JSONObject item = data.getJSONObject(i);
                String nombre = (String) item.get("nombre");
                String fecha = (String) item.get("fecha_visual");
                String monto = "S/ " + (String) item.get("monto");
                listItem = new ItemDataYape(nombre, monto, fecha);
                data_lista.add(listItem);
            }
        }
        catch (Exception e) {}
        return data_lista;
    }

    public String ListToString(List<ItemDataYape> data) {
        return new Gson().toJson(data);
    }

}
