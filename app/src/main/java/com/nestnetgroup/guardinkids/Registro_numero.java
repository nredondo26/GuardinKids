package com.nestnetgroup.guardinkids;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class Registro_numero extends AppCompatActivity {


    Button btn_siguiente;
    String code_pais,telefono;
    private static final String URL_IP = "http://appguardian.co/newsite/android/lib/octenercodigopais.php";
    private AppCompatEditText phoneNumber;
    private RequestQueue rq;
    ProgressDialog progressDialog;

    String publicIPAddress;
    private CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_numero);


        rq = Volley.newRequestQueue(getApplicationContext());
        publicIPAddress = getPublicIPAddress(this);
        enviar();
        ccp =findViewById(R.id.ccp);
        btn_siguiente=findViewById(R.id.btn_siguiente);
        phoneNumber =findViewById(R.id.txt_telefono);

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                code_pais=ccp.getSelectedCountryCode();
                telefono=phoneNumber.getText().toString();

                phoneNumber.setError(null);
                ccp.registerPhoneNumberTextView(phoneNumber);
                String  phone = ccp.getFullNumber();

                boolean cancel= false;
                View focusView = null;

                if(!isPhoneValid(phone)){
                    focusView=phoneNumber;
                    cancel=true;
                }

                if (cancel){
                    focusView.requestFocus();
                    Toast.makeText(getApplicationContext(),"Numero no valido",Toast.LENGTH_SHORT).show();
                }else{

                    dialogo_confirmar("+"+phone);
                   Log.e("numero",phone);
                }

            }
        });






      //  progresbar_code();

        //txt_code=findViewById(R.id.txt_code_pais);
     /*   txt_number=findViewById(R.id.txt_telefono);
        btn_siguiente=findViewById(R.id.btn_siguiente);
        rq = Volley.newRequestQueue(getApplicationContext());


       publicIPAddress = getPublicIPAddress(this);


        enviar();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               code_pais=txt_code.getText().toString().trim();
                telefono=txt_number.getText().toString().trim();

              if(code_pais.equalsIgnoreCase("")){
                  Toast.makeText(getApplicationContext(),"Introduce el código de  tu país",Toast.LENGTH_LONG).show();
                  return;
              }
                if(telefono.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Introduce el número de teléfono",Toast.LENGTH_LONG).show();
                    return;
              }

                telefono_completo=code_pais+telefono;

                dialogo_confirmar(telefono_completo);


            }
        });*/

    }



    void dialogo_confirmar(final String numbre){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vamos a verificar el número de teléfono")
          .setMessage('\n'+numbre+'\n'+'\n'+"¿Está correcto o quieres modificarlo?")
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();

                        consulta_usuario(numbre);
                        progresbar_consulta();


                       /* Intent intent=new Intent(getApplicationContext(),Codigo_confirmacion.class);
                        intent.putExtra("phone", numbre);
                        intent.putExtra("codigo_pais", code_pais);
                        intent.putExtra("telefono", telefono);
                        startActivity(intent);
                        finish();*/

                    }
                })
                .setNegativeButton("EDITAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        builder.show();
    }


    private void consulta_usuario(final String numbre){
        StringRequest str = new StringRequest(Request.Method.POST, "http://appguardian.co/newsite/android/lib/consulta-numero.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        progressDialog.cancel();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            boolean estado = jsonObject.getBoolean("success");
                            String mensaje = jsonObject.getString("detalle");
                            if (estado) {

                                Intent intent=new Intent(getApplicationContext(),Codigo_confirmacion.class);
                                intent.putExtra("phone", numbre);
                                intent.putExtra("codigo_pais", code_pais);
                                intent.putExtra("telefono", telefono);
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(),mensaje ,Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error->", error.toString());
                progressDialog.cancel();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("code", code_pais);
                parametros.put("tele", telefono);
                return parametros;

            }
        };
        rq.add(str);
    }




  /* public String rand_code(String chars){
       String code = "";
        for (int x=0; x < 4; x++)
        {
          double rand = Math.floor(Math.random()*chars.length());
            code += chars.charAt((int) rand);
        }
        return code;
   }*/

  public  void  progresbar_consulta(){
       progressDialog = new ProgressDialog(this);
       progressDialog.setMessage("Comprobando usuario....");
       progressDialog.setIndeterminate(false);
       progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       progressDialog.setCancelable(false);
       progressDialog.show();

   }


   /* public  void  progresbar_code(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando....");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }*/


    public static String getPublicIPAddress(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if ((info != null && info.isAvailable()) && (info.isConnected())) {
                    StringBuilder response = new StringBuilder();

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) (
                                new URL("http://checkip.amazonaws.com/").openConnection());
                        urlConnection.setRequestProperty("User-Agent", "Android-device");
                        urlConnection.setReadTimeout(15000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-type", "application/json");
                        urlConnection.connect();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                        }
                        urlConnection.disconnect();
                        return response.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return null;
                }
                return null;
            }
        });

        new Thread(futureRun).start();
        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void enviar(){
        StringRequest str = new StringRequest(Request.Method.POST, URL_IP,new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                String codigo=response.trim();
                ccp.setDefaultCountryUsingNameCode(codigo);
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
                parametros.put("ip", publicIPAddress);
                return parametros;

            }
        };
        rq.add(str);
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() > 8;
    }


}
