package com.nestnetgroup.guardinkids;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }





    public String Mostrarpreferencia_movil(){
        SharedPreferences prefs = getSharedPreferences("Cuenta", MODE_PRIVATE);
        String valor = prefs.getString("numero_movil", "Not_config");
        return valor;
    }

    public String Mostrarpreferencia_code_pais(){
        SharedPreferences prefs = getSharedPreferences("Cuenta", MODE_PRIVATE);
        String valor = prefs.getString("code_pais", "Not_config");
        return valor;
    }

    public String Mostrarpreferencia_estado(){
        SharedPreferences prefs = getSharedPreferences("Cuenta",MODE_PRIVATE);
        String valor = prefs.getString("estado", "Not_config");
        return valor;
    }


}
