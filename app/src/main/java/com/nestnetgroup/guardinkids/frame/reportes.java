package com.nestnetgroup.guardinkids.frame;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.nestnetgroup.guardinkids.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.Request.Method;

public class reportes extends Fragment {


    private  static final String URL_ELIMINAR="http://plataformagk.com/deletereportandroid";
    private RequestQueue rq;
    String ID_USUARIO;
    ImageView image_reporte,imageButtoneliminar;
    TextView txt_nombre_dispo,txt_fecha,textView7;
    String id_img_consultar;
    ConstraintLayout contenedor;
    CardView cardView;
    ImageButton imageButton,imageButton2,btn_izquierda,btn_derecha;
    int posicion;
    JSONArray json=null;
    String ideliminar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_reportes,container, false);

        rq = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        ID_USUARIO= Mostrarpreferencia_id_usuario();
        imageButton=v.findViewById(R.id.imageButton);
        image_reporte=v.findViewById(R.id.image_reporte);
        txt_nombre_dispo=v.findViewById(R.id.txt_nombre_dispo);
        textView7=v.findViewById(R.id.textView7);
        imageButton2=v.findViewById(R.id.imageButton2);
        txt_fecha=v.findViewById(R.id.txt_fecha);
        btn_izquierda=v.findViewById(R.id.btn_izqui);
        btn_derecha=v.findViewById(R.id.btn_derecha);
       contenedor = v.findViewById(R.id.contenedor);
        cardView=v.findViewById(R.id.cardView);
        imageButtoneliminar=v.findViewById(R.id.imageButtoneliminar);

        enviar(ID_USUARIO);

        imageButtoneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elimina_img();
            }
        });


        btn_izquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(json!=null){
                  try {
                     int n=json.length();

                      if(posicion==0){
                          id_img_consultar=String.valueOf(json.getInt(n-1));
                          consulta_img(id_img_consultar);
                          ideliminar=id_img_consultar;
                          posicion=n-1;
                      }else {
                          id_img_consultar=String.valueOf(json.getInt(posicion-1));
                          consulta_img(id_img_consultar);
                          ideliminar=id_img_consultar;
                          posicion=posicion-1;
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
            }
        });

        btn_derecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(json!=null){
                    try {
                        int n=json.length();
                        if(posicion == n-1){
                            id_img_consultar=String.valueOf(json.getInt(0));
                            consulta_img(id_img_consultar);
                            ideliminar=id_img_consultar;
                            posicion=0;
                        }else {
                            id_img_consultar= String.valueOf(json.getInt(posicion+1));
                            consulta_img(id_img_consultar);
                            ideliminar=id_img_consultar;
                            posicion=posicion+1;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return v;
    }

    private void consulta_img(final String id){
        final String url1 = "http://plataformagk.com/consultar_reportes_imagen/"+id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url1,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rEPUESTA", String.valueOf(response));
                        if(response.length()>0){
                            try {
                                JSONObject object=new JSONObject(response.getString(0));
                                Glide.with(Objects.requireNonNull(getContext())).load("http://plataformagk.com/captures/"+object.getString("imagen")).into(image_reporte);
                                txt_nombre_dispo.setText(object.getString("nombre_equipo"));
                                txt_fecha.setText(object.getString("fecha"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            image_reporte.setImageResource(R.drawable.logo);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("response->error", error.toString());
                    }
                }
        );
        rq.add(jsonArrayRequest);
    }


    private void enviar(final String id){
        final String url = "http://plataformagk.com/consultar_reportes_android/"+id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()>0){
                            try {
                                JSONObject object=new JSONObject(response.getString(0));
                                Glide.with(Objects.requireNonNull(getContext())).load("http://plataformagk.com/captures/"+object.getString("imagen")).into(image_reporte);
                                txt_nombre_dispo.setText(object.getString("nombre_equipo"));
                                txt_fecha.setText(object.getString("fecha"));
                                ideliminar=object.getString("id");
                                json = new JSONArray();
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject jresponse = response.getJSONObject(i);
                                    String id = jresponse.getString("id");
                                    json.put(id);
                                  }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            image_reporte.setImageResource(R.drawable.logo);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("response->error", error.toString());
                    }
                }
        );
        rq.add(jsonArrayRequest);
    }


    private void elimina_img(){
        Log.e("id actual",ideliminar);
        StringRequest str = new StringRequest(Method.POST, URL_ELIMINAR,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        if(response.contains("true")){
                            Log.e("Respuesta elimina",response);
                        }else{
                            Log.e("Algo salio mal",response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error->", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", id_img_consultar);
                return parametros;
            }
        };
        rq.add(str);
    }

    public String Mostrarpreferencia_id_usuario(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
        String valor = prefs.getString("Id_usuario", "Not_config");
        return valor;
    }


}
