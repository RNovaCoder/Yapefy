package com.novacoder.looptransaction.IUcomponents.app.cuerpoViews.listaItems.yape;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.novacoder.looptransaction.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class AdapterYape extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_GRANDE = 1;
    private static final int ITEM_DEFAULT = 2;
    private List<ItemDataYape> data = new ArrayList<>();
    private List<ItemDataYape> data_filtrada = new ArrayList<>();;

    private Thread th_filtro = new Thread();
    private Handler hiloUI = new Handler(Looper.getMainLooper());
    private boolean state_data; // Bandera para indicar si el primer ítem será morado

    public AdapterYape() {}

    // Este método determina el tipo de vista (diseño) para un ítem en la lista en función de su posición
    @Override
    public int getItemViewType(int position) {
        if (state_data && position == 0) {
            return ITEM_GRANDE; // El primer ítem será grande
        } else {
            return ITEM_DEFAULT; // Los demás ítems tendrán el diseño por defecto
        }
    }

    // Este método crea una nueva vista para cada ítem en la lista
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ITEM_GRANDE) {
            View view = inflater.inflate(R.layout.itemgrande, parent, false);
            return new GrandeViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.itemnormal, parent, false);
            return new DefaultViewHolder(view);
        }
    }

    // Este método se encarga de asociar los datos a cada vista (ViewHolder) en la lista
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemDataYape item = (state_data) ? data.get(position) : data_filtrada.get(position);

        if (holder.getItemViewType() == ITEM_GRANDE) {
            GrandeViewHolder grandeViewHolder = (GrandeViewHolder) holder;
            setFadeAnimation(grandeViewHolder.itemView);
            grandeViewHolder.nombre.setText(item.get_nombre());
            grandeViewHolder.monto.setText(item.get_monto());
            grandeViewHolder.fecha.setText(item.get_fecha());
        } else {
            DefaultViewHolder defaultViewHolder = (DefaultViewHolder) holder;
            setFadeAnimation(defaultViewHolder.itemView);
            defaultViewHolder.nombre.setText(item.get_nombre());
            defaultViewHolder.monto.setText(item.get_monto());
            defaultViewHolder.fecha.setText(item.get_fecha());
        }
    }

    @Override
    public int getItemCount() {
        int size = (state_data) ? data.size() : data_filtrada.size();
        return size;
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    // ViewHolder para el primer ítem, que será Grande
    private static class GrandeViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView monto;
        TextView fecha;

        public GrandeViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            monto = itemView.findViewById(R.id.monto);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }

    // ViewHolder para los demás ítems con el diseño por defecto
    private static class DefaultViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView monto;
        TextView fecha;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            monto = itemView.findViewById(R.id.monto);
            fecha = itemView.findViewById(R.id.fecha);
        }
    }

    public void set_data (List<ItemDataYape> updatedItems) {
         // Borra los datos actuales en la lista

        try {
            data.clear();
            data.addAll(updatedItems);
        }
        catch (Exception e){}
        this.state_data = true;

        hiloUI.post(() -> {
            notifyDataSetChanged();
        }); // Notifica al adaptador que los datos han cambiado
    }

    public void detener_filter () {
        //Se asegura de detener el hilo para interrumpirlo
        th_filtro.interrupt();
        hiloUI.removeCallbacksAndMessages(null);
        try {
            //Espera a que el hilo se detenga para continuar
            th_filtro.join();
        } catch (InterruptedException e) {}
    }

    public void filtrar_data (String filtro) {

        hiloUI.removeCallbacksAndMessages(null);
        //Se asegura de detener el hilo para interrumpirlo
        th_filtro.interrupt();
        try {
            //Espera a que el hilo se detenga para continuar
            th_filtro.join();
        } catch (InterruptedException e) {}

        /*Reescribe el thread para la nueva búsqueda
        asignando un comprobación en todo momento*/
        th_filtro = new Thread(() -> {

            if(!th_filtro.isInterrupted()){
                data_filtrada.clear();
                state_data = false;
            } else {return;}

            int numDatos = data.size();
            int divisor = numDatos/2;

            for (int i = 0; i < numDatos; i++) {
                if(!th_filtro.isInterrupted()){
                    if (data.get(i).comprobar(filtro)) {
                        data_filtrada.add(data.get(i));
                    }
                    if (!th_filtro.isInterrupted() && esMultiplo(i,divisor)){
                        hiloUI.post(() -> {
                            if (!th_filtro.isInterrupted()) {
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
                else {return;}
            }

           if(!th_filtro.isInterrupted()){
               hiloUI.post(() -> {
                    notifyDataSetChanged();
                });
           }

        });

        th_filtro.start();

    }

    public boolean esMultiplo(int numero, int divisor) {
        return divisor != 0 && numero % divisor == 0;
    }


    public void default_data () {
        if (!state_data) {
            state_data = true;
            notifyDataSetChanged();
        }
    }
}
