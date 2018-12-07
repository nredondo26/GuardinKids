package com.nestnetgroup.guardinkids;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    String code_pais,telefono,nombre,email,contrasena,recontra;
    EditText txt_nombre,txt_email,txt_contra,txt_recontra,txt_code_dis;
    Button btn_registro;
    private static final String URL_MENSAJE = "http://plataformagk.com/regitroandroid";
    private RequestQueue rq;
    ProgressDialog progressDialog;
    String token,code_distribuidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txt_nombre=findViewById(R.id.txt_nombre);
        txt_email=findViewById(R.id.txt_email);
        txt_contra=findViewById(R.id.txt_contra);
        txt_recontra=findViewById(R.id.txt_recontra);
        btn_registro=findViewById(R.id.btn_registrar);
        txt_code_dis=findViewById(R.id.txt_code_dis);
        rq = Volley.newRequestQueue(getApplicationContext());

        token = FirebaseInstanceId.getInstance().getToken();

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                nombre=txt_nombre.getText().toString().trim();
                email=txt_email.getText().toString().trim();
                contrasena=txt_contra.getText().toString().trim();
                recontra=txt_recontra.getText().toString().trim();

                String code_distribidor=txt_code_dis.getText().toString().trim();

                if(nombre.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Introduce tu Nombre y Apellidos",Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Introduce tu  Correo Electrónico",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!validarEmail(email)){
                    Toast.makeText(getApplicationContext(),"Introduzca un correo electrónico valido",Toast.LENGTH_LONG).show();
                    return;
                }
                if(contrasena.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Introduce una Contraseña",Toast.LENGTH_LONG).show();
                    return;
                }
                if( recontra.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Repita la Contraseña",Toast.LENGTH_LONG).show();
                    return;
                }
                if( !recontra.equals(contrasena)){
                    Toast.makeText(getApplicationContext(),"La contraseña no coincide",Toast.LENGTH_LONG).show();
                    return;
                }
                progresbar();
                if(code_distribidor.equalsIgnoreCase("")){
                    code_distribuidor="";
                  enviar();
                }else{
                    consulta_codigo(code_distribidor);
                }
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio0:
                if (checked)
                    txt_code_dis.setVisibility(View.VISIBLE);

                    break;
            case R.id.radio2:
                if (checked)
                    txt_code_dis.setVisibility(View.INVISIBLE);

                    break;
        }
    }

    private void enviar(){
        StringRequest str = new StringRequest(Request.Method.POST, URL_MENSAJE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObject = null;
                        Log.e("Repuesta:",response);
                        try {
                            jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            String mensaje = jsonObject.getString("detalle");
                      //      {"success":"true","detalle":"Registro exitoso","id":277}
                            if (estado) {
                                String id_usuario=jsonObject.getString("id");
                                crear_preferencia(id_usuario);
                                Intent intent=new Intent(getApplicationContext(),Principal.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }else{
                                alert_dialog(mensaje);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error->", error.toString());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("email", email);
                parametros.put("password", contrasena);
                parametros.put("codaliado", code_distribuidor);

                return parametros;
            }
        };
        rq.add(str);
    }


    private void consulta_codigo(final String code){
        StringRequest str = new StringRequest(Request.Method.POST, "http://plataformagk.com/consultacode",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            String mensaje = jsonObject.getString("detalle");
                            if (estado) {
                                code_distribuidor=code;
                                enviar();

                            }else{
                                progressDialog.dismiss();
                                alert_dialog(mensaje);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error->", error.toString());
                //progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("cod", code);
                return parametros;
            }
        };
        rq.add(str);
    }

    public  void  progresbar(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando usuario....");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void crear_preferencia(String id_usuario){

        SharedPreferences prefs = getSharedPreferences("Cuenta", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Id_usuario", id_usuario);
        editor.commit();
    }

    private void alert_dialog(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(mensaje)
                .setTitle("GUARDIANKIDS")
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

}
