package com.nestnetgroup.guardinkids;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Adapter_dispositivo extends RecyclerView.Adapter<Adapter_dispositivo.ViewHolder> {


    private List<Atributos_dispositivo> atributosList;
    private Context context;

    Dialog renombre;
    EditText txt_renombre;
    private static final String URL_RENOMBRE = "https://appguardian.co/newsite/android/lib/renombrar-dispositivo.php";
    private RequestQueue rq;

    public Adapter_dispositivo(List<Atributos_dispositivo> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_new, parent, false);
        return new ViewHolder(vista);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.nombre_dispositivo.setText(atributosList.get(position).nombre_dispositivo);
        holder.fecha_instalacion.setText(atributosList.get(position).restante);


        boolean estado=atributosList.get(position).estado;


        if(!estado){

            int numEntero = Integer.parseInt(atributosList.get(position).restante) * (-1);

            holder.fecha_instalacion.setText("Días de caducado: "+numEntero);
            holder.btn_estado.setBackgroundResource(R.drawable.style_button_vencido);
            holder.btn_estado.setTextColor(holder.nombre_dispositivo.getContext().getResources().getColor(R.color.colorBlanco));
            holder.tvestado.setText("CADUCADO");
            holder.tvestado.setTextColor(holder.nombre_dispositivo.getContext().getResources().getColor(R.color.colorrojo));
        }else{
            holder.fecha_instalacion.setText("Días de protección: "+atributosList.get(position).restante);
        }

        holder.btn_renombrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_renombre(atributosList.get(position).id_dispositivo, atributosList.get(position).getNombre_dispositivo(),position);
            }
        });

        holder.btn_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://appguardian.co/newsite/android/lib/pago-dispositivo.php?id_dispositivo="+atributosList.get(position).id_dispositivo);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });


        holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Eliminar")
                        .setContentText("¿Seguro que desea eliminar el dispositivo "+ atributosList.get(position).getNombre_dispositivo()+" ?")
                        .setCancelText("Cancelar")
                        .setConfirmText("Eliminar")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                rq = Volley.newRequestQueue(context);
                                editadispositivo(atributosList.get(position).getId_dispositivo(),position);
                                sDialog.dismissWithAnimation();
                        }
                        })
                        .show();


        }
        });
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre_dispositivo;
        TextView fecha_instalacion;
        TextView tvestado;
        Button btn_renombrar,btn_estado;
        ImageView btn_eliminar;

        public ViewHolder(View item){
            super(item);
            nombre_dispositivo = item.findViewById(R.id.tvUserName);
            fecha_instalacion = item.findViewById(R.id.tvFromDate);
            btn_renombrar = item.findViewById(R.id.btn_renombrar);
            btn_estado=item.findViewById(R.id.btnGoToReports);
            tvestado=item.findViewById(R.id.tvestado);
            btn_eliminar=item.findViewById(R.id.btn_eliminar);
        }
    }

    public void dialog_renombre(final String id_d, String nombre_dispo, final int position){

        renombre = new Dialog(context);
        renombre.setContentView(R.layout.dialog_renombrar);
        Button cancel = renombre.findViewById(R.id.btn_cancelar);
        Button actualizar = renombre.findViewById(R.id.enviar);
        TextView nombre_d = renombre.findViewById(R.id.textView8);
        nombre_d.setText(nombre_dispo);
        txt_renombre = renombre.findViewById(R.id.txt_renombre);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renombre.cancel();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String renombre_new = txt_renombre.getText().toString().trim();
                if (renombre_new.equals("")) {
                    Toast.makeText(context, "Faltan parámetros ", Toast.LENGTH_LONG).show();
                    return;
                }
                rq = Volley.newRequestQueue(context);
                enviar_actualizar_renombre(id_d, renombre_new,position);
            }
        });
        renombre.show();
    }


    private void enviar_actualizar_renombre(final String id, final String renombre3, final int positiion){
        StringRequest str = new StringRequest(Request.Method.POST, URL_RENOMBRE,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d("respuesta:", response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            if (estado){
                                atributosList.get(positiion).setNombre_dispositivo(renombre3);
                                notifyDataSetChanged();
                                renombre.cancel();
                            }else{
                               Toast.makeText(context,"Se produjo un error al Renombrar el dispositivo",Toast.LENGTH_LONG).show();
                                renombre.cancel();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                           Toast.makeText(context,"Se produjo un error al Renombrar el dispositivo",Toast.LENGTH_LONG).show();
                            renombre.cancel();
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Error->", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", id);
                parametros.put("renombre", renombre3);
                return parametros;
            }
        };
        rq.add(str);
    }




    public void editadispositivo(final String id_dispositivo,final int position){

        StringRequest str = new StringRequest(Request.Method.POST,"http://appguardian.co/newsite/android/lib/elimina-dispositivo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            boolean estado = jsonObject.getBoolean("success");
                            if (estado){

                                atributosList.remove(position);
                                notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error->", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_dispositivo", id_dispositivo);

                return parametros;
            }
        };
        rq.add(str);
    }
}
