package com.nestnetgroup.guardinkids.frame;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nestnetgroup.guardinkids.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class ajustes extends Fragment {

    String ID_USER;
    private  static final String URL="http://plataformagk.com/consulta_info_user";
    private  static final String URL_ACTUALIZA_NOMBRE="http://plataformagk.com/actualizar_datos_android";
    private  static final String URL_ACTUALIZA_CONTRA="https://appguardian.co/newsite/android/lib/edita-passw.php";
    private RequestQueue rq;
    EditText nombre,email,telefono,passw;
    ImageButton edit1,edit2,edit3,edit4;
    Button actulizar;
    int  flagnombre=0,flagemail=0,flagpassword=0,flagtelefono=0;
    Dialog img_ampliad;
    String nombrer,emailr,Passwr,telefonor;
    EditText contraca,newcontraca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_ajustes,container, false);

        ID_USER=Mostrarpreferencia_id_usuario();
        rq = Volley.newRequestQueue(getActivity());

        if(!ID_USER.equals("Not_config")){
            enviar();
        }

        nombre=v.findViewById(R.id.nombre);
        email=v.findViewById(R.id.email);
        telefono=v.findViewById(R.id.telefono);
        passw=v.findViewById(R.id.contra);

        edit1=v.findViewById(R.id.edit1);
        edit2=v.findViewById(R.id.edit2);
        //edit3=v.findViewById(R.id.edit3);
        edit4=v.findViewById(R.id.edit4);
        actulizar=v.findViewById(R.id.actulizar);

        nombre.setEnabled(false);
        email.setEnabled(false);
        telefono.setEnabled(false);
        passw.setEnabled(false);


        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(flagnombre==0){
                   nombre.setEnabled(true);
                   nombre.requestFocus();
                   nombre.setSelection(nombre.getText().length());
                   flagnombre=1;
                }else{
                    nombre.setEnabled(false);
                   flagnombre=0;
                }
            }
        });

        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flagemail==0){
                    email.setEnabled(true);
                    email.requestFocus();
                    email.setSelection(email.getText().length());
                    flagemail=1;

                }else{
                    email.setEnabled(false);
                    flagemail=0;
                }

            }
        });

       /* edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flagpassword==0){
                    passw.setEnabled(true);
                    passw.requestFocus();
                    passw.setSelection(passw.getText().length());
                    flagpassword=1;

                }else{
                    passw.setEnabled(false);
                    flagpassword=0;
                }

            }
        });*/

        edit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flagtelefono==0){
                    telefono.setEnabled(true);
                    telefono.requestFocus();
                    telefono.setSelection(telefono.getText().length());
                    flagtelefono=1;

                }else{
                    telefono.setEnabled(false);
                    flagtelefono=0;
                }

            }
        });

        actulizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n=nombre.getText().toString().trim();
                String e=email.getText().toString().trim();
                String p=passw.getText().toString().trim();
                String t=telefono.getText().toString().trim();

                if(!(n.equals(nombrer) && e.equals(emailr) && p.equals(Passwr))){
                    if(!n.equals("") && !e.equals("") && !p.equals("")){

                       boolean balda_email=validarEmail(e);

                       if(balda_email){
                           enviar_actualizar_nombre(n,e,p,t);

                       }else{
                           Toast.makeText(getActivity(),"Introduzca un correo electrónico valido",Toast.LENGTH_LONG).show();
                       }
                    }
                }else{
                    nombre.setEnabled(false);
                    email.setEnabled(false);
                    telefono.setEnabled(false);
                    passw.setEnabled(false);

                }
            }
        });

        return v;
    }

    public String Mostrarpreferencia_id_usuario(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
        String valor = prefs.getString("Id_usuario", "Not_config");
        return valor;
    }


    private void enviar(){
        StringRequest str = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        Log.e("respuesta:", response );

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean estado=jsonObject.getBoolean("success");
                            if(estado){

                                nombrer = jsonObject.getString("nombre");
                                emailr=jsonObject.getString("email");
                                Passwr=jsonObject.getString("Passw");
                                telefonor=jsonObject.getString("tel");
                                nombre.setText(nombrer);
                                email.setText(emailr);
                                passw.setText(Passwr);
                                telefono.setText(telefonor);
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
                parametros.put("id", ID_USER);
                return parametros;
            }
        };
        rq.add(str);
    }


    private void enviar_actualizar_nombre(final String nombreactu, final String emailactua,final String passwordactual,final String telefonoactual){
        StringRequest str = new StringRequest(Request.Method.POST, URL_ACTUALIZA_NOMBRE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            if (estado) {
                                enviar();
                                nombre.setEnabled(false);
                                email.setEnabled(false);
                                telefono.setEnabled(false);
                                passw.setEnabled(false);
                                Toast.makeText(getActivity(),"Se actualizaron los datos exitosamente",Toast.LENGTH_LONG).show();
                            }else  Toast.makeText(getActivity(),"Se produjo un error al actualizar los datos",Toast.LENGTH_LONG).show();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Se produjo un error al actualizar los datos",Toast.LENGTH_LONG).show();
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
                parametros.put("id", ID_USER);
                parametros.put("nombre", nombreactu);
                parametros.put("email", emailactua);
                parametros.put("telefono", telefonoactual);
                parametros.put("password", passwordactual);
                return parametros;
            }
        };
        rq.add(str);
    }

    private void enviar_actualizar_passw(final String passwr){
        StringRequest str = new StringRequest(Request.Method.POST, URL_ACTUALIZA_CONTRA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d("respuesta: ",response);
                       try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean estado = jsonObject.getBoolean("success");
                            if (estado) {

                                img_ampliad.cancel();

                                enviar();
                                nombre.setEnabled(false);
                                email.setEnabled(false);
                                telefono.setEnabled(false);
                                passw.setEnabled(false);
                                Toast.makeText(getActivity(),"Se actualizaron los datos exitosamente",Toast.LENGTH_LONG).show();

                            }else{

                                Toast.makeText(getActivity(),"Se produjo un error al actualizar los datos",Toast.LENGTH_LONG).show();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Se produjo un error al actualizar los datos",Toast.LENGTH_LONG).show();
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
                parametros.put("id", ID_USER);
                parametros.put("contra", passwr);


                return parametros;
            }
        };
        rq.add(str);
    }


    public  void dialog_amplia(){

        img_ampliad=new Dialog(getActivity());
        img_ampliad.setContentView(R.layout.dialo_cambio_passw);

        Button   cancel=img_ampliad.findViewById(R.id.btn_cancelar);
        Button  actualizar=img_ampliad.findViewById(R.id.enviar);

         contraca=img_ampliad.findViewById(R.id.contra);
         newcontraca=img_ampliad.findViewById(R.id.newcontra);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                img_ampliad.cancel();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String contra=contraca.getText().toString().trim();
                String newcontra=newcontraca.getText().toString().trim();

                String contraactual=passw.getText().toString().trim();

                if(contra.equals("") || newcontra.equals("")){
                    Toast.makeText(getActivity(),"Faltan parámetros ",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!contraactual.equals(contra)){
                    Toast.makeText(getActivity(),"La contraseña es incorrecta ",Toast.LENGTH_LONG).show();
                    return;
                }

                enviar_actualizar_passw(newcontra);
            }
        });

        img_ampliad.show();
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
