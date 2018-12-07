package com.nestnetgroup.guardinkids.frame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nestnetgroup.guardinkids.Adapter_dispositivo;
import com.nestnetgroup.guardinkids.Atributos_dispositivo;
import com.nestnetgroup.guardinkids.R;
import com.nestnetgroup.guardinkids.manual;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dispositivos extends Fragment {

    FloatingActionButton mas;
    private RecyclerView rv;
    public List<Atributos_dispositivo> atributosList;
    public Adapter_dispositivo adapter;
    private  static final String URL="http://appguardian.co/newsite/android/lib/consulta-dispositivos.php";
    private RequestQueue rq;
    String ID_USUARIO;
    CoordinatorLayout contenedor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_dispositivos,container, false);

        mas=v.findViewById(R.id.mas);
        atributosList = new ArrayList<>();
        rv = v.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter = new Adapter_dispositivo(atributosList,getActivity());
        rv.setAdapter(adapter);
        rq = Volley.newRequestQueue(getActivity());

        contenedor=v.findViewById(R.id.contenedor);
        ID_USUARIO= Mostrarpreferencia_id_usuario();

        enviar();

        mas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),manual.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        return  v;

    }

    public void agregarDispositivo(String Nombre_dispo,String id_dispo,String restante,boolean estado){

        Atributos_dispositivo psatri= new Atributos_dispositivo(id_dispo,Nombre_dispo,restante,estado);
        psatri.setId_dispositivo(id_dispo);
        psatri.setNombre_dispositivo(Nombre_dispo);
        psatri.setRestante(restante);
        psatri.setEstado(estado);
        atributosList.add(psatri);
        adapter.notifyDataSetChanged();
    }


    public void enviar(){
        StringRequest str = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONArray json = new JSONArray(response);
                            JSONObject jsonObject = json.getJSONObject(0);
                           String Id_dispositivo= jsonObject.getString("Id_dispositivo");

                           if(Id_dispositivo.equals("No_registro")){

                               LayoutInflater inflater = LayoutInflater.from(getContext());
                               View laViewInflada = inflater.inflate(R.layout.no_dispositivo, contenedor, true);
                               rv.setVisibility(View.INVISIBLE);

                           }else{

                               if(Id_dispositivo.equals("No_parametros")){

                                   LayoutInflater inflater = LayoutInflater.from(getContext());
                                   View laViewInflada = inflater.inflate(R.layout.no_dispositivo, contenedor, true);
                                   rv.setVisibility(View.INVISIBLE);
                               }else{
                                   rv.setVisibility(View.VISIBLE);
                                   for (int i=0; i < json.length(); i++)
                                   {
                                       JSONObject oneObject = json.getJSONObject(i);
                                       String id_dispositivo=oneObject.getString("Id_dispositivo");
                                       String nombre_dispo=oneObject.getString("Marca_dispositivo");
                                       String renombre_dispo=oneObject.getString("Renombrar");
                                       String restante=oneObject.getString("Restante");
                                       boolean estado=oneObject.getBoolean("Estado");
                                       String Nombre_mostrar;
                                       if(renombre_dispo.equals("null") | renombre_dispo.equals("") ){
                                           Nombre_mostrar=nombre_dispo;
                                       }else{
                                           Nombre_mostrar=renombre_dispo;
                                       }
                                      agregarDispositivo(Nombre_mostrar,id_dispositivo,restante,estado);
                                   }
                               }
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
                parametros.put("Id_usuario", ID_USUARIO);
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
