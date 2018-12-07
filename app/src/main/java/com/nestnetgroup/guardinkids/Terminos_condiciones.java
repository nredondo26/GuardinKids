package com.nestnetgroup.guardinkids;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Terminos_condiciones extends AppCompatActivity {

    Button btn_terminos;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos_condiciones);

        btn_terminos=findViewById(R.id.btn_registrar);
        textView4=findViewById(R.id.textView4);

        String text = "He leído y acepto los <font color=#1584f1>términos y condiciones.</font>";
        textView4.setText(Html.fromHtml(text));

        btn_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Song_detail = new Intent(getApplicationContext(),Registro_numero.class);
                startActivity(Song_detail);
                finish();

            }
        });

        textView4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Uri uri = Uri.parse("http://monitorguardian.com/co/Terminos-condiciones-android.php");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });


    }
}
