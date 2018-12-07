package com.nestnetgroup.guardinkids;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by NestNet on 4/05/2018.
 */

public class Adapter_reportes  extends RecyclerView.Adapter<Adapter_reportes.ViewHolder>  {

    private List<Atributos_reportes> atributosList;
    private Context context;

    public Adapter_reportes(List<Atributos_reportes> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.car_reportes,parent,false);
        return new Adapter_reportes.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre_dispositivo;
        TextView fecha_reporte;
        ImageView image_repote;




        public ViewHolder(View item){
            super(item);
            nombre_dispositivo=item.findViewById(R.id.txt_nombre_dispo);
            fecha_reporte=item.findViewById(R.id.tvFromDate);
            image_repote=item.findViewById(R.id.image_reporte);

        }
    }
}
