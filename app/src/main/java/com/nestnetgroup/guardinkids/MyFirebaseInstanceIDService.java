package com.nestnetgroup.guardinkids;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NestNet on 31/05/2018.
 */

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String URL_MENSAJE = "http://appguardian.co/newsite/android/lib/guarda-tokenappadmin.php";
    private RequestQueue rq;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        String id_usaurio=Mostrarpreferencia_id_usuario();
        rq = Volley.newRequestQueue(getApplicationContext());
        guarda_toquen(id_usaurio,token);
    }


    public void guarda_toquen(final String id_usuario, final String toquen){

        StringRequest str = new StringRequest(Request.Method.POST, URL_MENSAJE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

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
                parametros.put("id_usuario", id_usuario);
                parametros.put("toquen", toquen);
                return parametros;
            }
        };
        rq.add(str);
    }


    public String Mostrarpreferencia_id_usuario(){
        SharedPreferences prefs = getSharedPreferences("Cuenta", MODE_PRIVATE);
        String valor = prefs.getString("Id_usuario", "Not_config");
        return valor;
    }

}