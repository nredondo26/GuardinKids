package com.nestnetgroup.guardinkids;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Codigo_confirmacion extends AppCompatActivity {

   Button btn_siguiente2;
   EditText txt_code_confimacion;
   TextView txt_verificar,nun2,txt_timer,txt_enviarsms,txt_numero_equi;
   String phone,code_pais,telefono;
    ProgressDialog progressDialog;

    String codeSend;
    FirebaseAuth mAuth;

   long tiempo=0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_confirmacion);

        phone= getIntent().getExtras().getString("phone");
        code_pais=getIntent().getExtras().getString("codigo_pais");
        telefono=getIntent().getExtras().getString("telefono");

       if(code_pais==null || telefono==null ||phone==null || telefono.equals("") || code_pais.equals("") || phone.equals("")){

           Intent intent=new Intent(Codigo_confirmacion.this,Registro_numero.class);
           startActivity(intent);
           finish();
       }

        btn_siguiente2=findViewById(R.id.btn_siguiente2);
        txt_code_confimacion=findViewById(R.id.txt_code_enviado);
        txt_verificar=findViewById(R.id.txt_verificar);
        nun2=findViewById(R.id.textView5);
        txt_timer=findViewById(R.id.txt_timer);
        txt_enviarsms=findViewById(R.id.textView17);
        txt_numero_equi=findViewById(R.id.textView20);



        txt_verificar.setText("Verificar "+phone);

        nun2.setText("Esperando para detectar automáticamente el SMS enviado al "+phone+".");
        mAuth = FirebaseAuth.getInstance();

        envia_SMS(phone);

        new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                txt_timer.setText("" + l / 1000 + " Segundos");

                tiempo=l/1000;

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                txt_timer.setText("0 Segundos");

                tiempo=0;

            }
        }.start();

        btn_siguiente2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codeco=txt_code_confimacion.getText().toString().trim();

                if(!codeco.equals("")){
                    if(codeco.length()==6) {
                        verificar_codigo(codeco);
                    }else{
                        Toast.makeText(getApplicationContext(),"El código de confirmación debe tener 6 dígitos…",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Código de Confinación Requerido…",Toast.LENGTH_SHORT).show();
                }

            }
        });

        txt_enviarsms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(tiempo==0){
                    envia_SMS(phone);
                    Toast.makeText(getApplicationContext(),"Se ha enviado Un SMS nuevamente",Toast.LENGTH_SHORT).show();


                    new CountDownTimer(60000, 1000) {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTick(long l) {
                            txt_timer.setText("" + l / 1000 + " Segundos");
                            tiempo=l/1000;

                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onFinish() {
                            txt_timer.setText("0 Segundos");

                            tiempo=0;

                        }
                    }.start();
                }
            }
        });

        txt_numero_equi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Registro_numero.class);
                startActivity(intent);
                finish();
            }
        });
    }




 public  void  progresbar_sms(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando…");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }


   private void verificar_codigo(String code){
       PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend, code);
       signInWithPhoneAuthCredential(credential);
       Log.e("MENSAJE", "verificar_codigo");
   }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Log.e("MENSAJE", "BIEN");

                            progresbar_sms();
                            new CountDownTimer(3000, 1000){
                                @Override
                                public void onTick(long l) {
                                }
                                @Override
                                public void onFinish(){
                                    progressDialog.cancel();
                                    Intent intent=new Intent(getApplicationContext(),Registro.class);
                                    intent.putExtra("codigo_pais", code_pais);
                                    intent.putExtra("telefono", telefono);
                                    startActivity(intent);
                                    finish();
                                }
                            }.start();

                        } else{
                            Log.e("Codigo", "failure");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e("Codigo", task.getException().toString());

                            }
                        }
                    }
                });
    }



    private void envia_SMS(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            progresbar_sms();
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {
                }
                @Override
                public void onFinish(){
                    progressDialog.cancel();
                                    Intent intent=new Intent(getApplicationContext(),Registro.class);
                                    intent.putExtra("codigo_pais", code_pais);
                                    intent.putExtra("telefono", telefono);
                                    startActivity(intent);
                                    finish();
                }
            }.start();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("RESPUESTA","Error al verificar");

            Log.e("Error-> ", e.toString());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSend=s;

            Log.e("onCodeSent ",  codeSend);
        }
    };
}
