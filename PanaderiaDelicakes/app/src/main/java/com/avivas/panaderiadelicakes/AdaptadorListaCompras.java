package com.avivas.panaderiadelicakes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorListaCompras extends RecyclerView.Adapter<AdaptadorListaCompras.ViewHolderDatos> {

    ArrayList<DatosCompra> listaDatosCompra;

    public AdaptadorListaCompras(ArrayList<DatosCompra> listaDatosCompra) {
        this.listaDatosCompra = listaDatosCompra;
    }

    @NonNull
    @Override
    public AdaptadorListaCompras.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_compra, null, false);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaCompras.ViewHolderDatos holder, int position) {
        holder.fechaCompra.setText("Fecha compra: " + listaDatosCompra.get(position).getFechaCompra());
        holder.montoCompra.setText("Monto compra: S/." + listaDatosCompra.get(position).getMontoCompra());
        holder.puntosCompra.setText("Puntos acumulados: " + listaDatosCompra.get(position).getPuntosCompra());
    }

    @Override
    public int getItemCount() {
        return listaDatosCompra.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView fechaCompra, montoCompra, puntosCompra;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            fechaCompra = (TextView) itemView.findViewById(R.id.txv_fecha_compra);
            montoCompra = (TextView) itemView.findViewById(R.id.txv_monto_compra);
            puntosCompra = (TextView) itemView.findViewById(R.id.txv_puntos_compra);

        }

    }
}
