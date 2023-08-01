package com.example.interfaz.IUcomponents;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interfaz.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdapterYape extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_GRANDE = 1;
    private static final int ITEM_DEFAULT = 2;

    private List<ListItem> data;
    private List<ListItem> data_filtrada;

    private boolean state_data; // Bandera para indicar si el primer ítem será morado

    public AdapterYape() {
        this.data = new ArrayList<ListItem>();
        this.data_filtrada = new ArrayList<ListItem>();
    }

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
        ListItem item = data.get(position);
        if (holder.getItemViewType() == ITEM_GRANDE) {
            GrandeViewHolder grandeViewHolder = (GrandeViewHolder) holder;
            grandeViewHolder.nombre.setText(item.get_nombre());
            grandeViewHolder.monto.setText(item.get_monto());
            grandeViewHolder.fecha.setText(item.get_fecha());
        } else {
            DefaultViewHolder defaultViewHolder = (DefaultViewHolder) holder;
            defaultViewHolder.nombre.setText(item.get_nombre());
            defaultViewHolder.monto.setText(item.get_monto());
            defaultViewHolder.fecha.setText(item.get_fecha());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder para el primer ítem, que será morado
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

    public void set_data (List<ListItem> updatedItems) {
        data.clear(); // Borra los datos actuales en la lista
        data.addAll(updatedItems); // Agrega los nuevos datos a la lista
        this.state_data = true;

        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    public void filtrar_data (String filtro) {

        //Posible thead
        data_filtrada.clear();
        for (ListItem item: data) {
            if (item.comprobar (filtro)) {
                data_filtrada.add(item);
            };
        }
        state_data = false;

        //Posible handler
        notifyDataSetChanged();

    }


    public void default_data () {

        if (!state_data) {
            state_data = true;
            notifyDataSetChanged();
        }

    }


}
