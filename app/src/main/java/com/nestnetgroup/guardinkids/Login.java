package com.nestnetgroup.guardinkids;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    Button BtnLogin,btnCrearcuenta;
    EditText EdtEmail,EdtContrasena;
    private ProgressDialog berprogres;
    private static final String URL_MENSAJE = "http://appguardian.co/newsite/android/lib/consulta-login.php";
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      String  ID_USUARIOS=Mostrarpreferencia_id_usuario();

        if(!ID_USUARIOS.equals("Not_config")){
            Intent intent=new Intent(getApplicationContext(),Principal.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        BtnLogin=findViewById(R.id.BtnLogin);
        EdtContrasena=findViewById(R.id.EdtContrasena);
        EdtEmail=findViewById(R.id.EdtEmail);
        berprogres=new ProgressDialog(this);
        btnCrearcuenta=findViewById(R.id.Btncrear_cuenta);
        rq = Volley.newRequestQueue(getApplicationContext());


        btnCrearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Terminos_condiciones.class);
                startActivity(i);
                finish();
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String EMAIL=EdtEmail.getText().toString().trim();
                String CONTRA=EdtContrasena.getText().toString().trim();

                if (TextUtils.isEmpty(EMAIL)) {
                    Toast.makeText(getApplicationContext(),"Introduzca un E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarEmail(EMAIL)){
                    Toast.makeText(getApplicationContext(),"Introduzca un E-mail Valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(CONTRA)) {
                    Toast.makeText(getApplicationContext(),"Introduzca una  Contraseña ", Toast.LENGTH_SHORT).show();
                    return;
                }


                berprogres.setMessage("Comprobando  Usuario…");
                berprogres.setCancelable(false);
                berprogres.show();

                enviar(EMAIL, CONTRA);


            }
        });



    }

    private void crear_preferencia(String codigo){

        SharedPreferences prefs = getSharedPreferences("Cuenta", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Id_usuario", codigo);
        editor.commit();

        String token = FirebaseInstanceId.getInstance().getToken();
        guarda_toquen(codigo,token);

        Intent intent=new Intent(getApplicationContext(),Principal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void enviar(final String usuario, final String contra){
        StringRequest str = new StringRequest(Request.Method.POST, URL_MENSAJE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        berprogres.dismiss();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            String mensaje = jsonObject.getString("detalle");
                            if (estado) {

                                crear_preferencia(mensaje);

                            }else{

                                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                berprogres.dismiss();
                Toast.makeText(getApplicationContext(),"Error al autenticarse por favor revise su conexión a internet",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", usuario);
                parametros.put("contra", contra);


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

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public void guarda_toquen(final String id_usuario, final String toquen){

        StringRequest str = new StringRequest(Request.Method.POST, "http://appguardian.co/newsite/android/lib/guarda-tokenappadmin.php",
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
}
