package com.nestnetgroup.guardinkids;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class manual extends MaterialIntroActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorBlanco)
                        .buttonsColor(R.color.colorGris)
                        .image(R.drawable.family)
                .buttonsColor(R.color.colorPrimary)
                        .title("Tome el teléfono inteligente o la  Tableta del niño.")
                        .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso3)
                .buttonsColor(R.color.colorPrimary)
                .title("Abra el navegador Chrome u otro navegador en el dispositivo del niño")
                .description("En la barra de direcciones, ingrese la siguiente dirección. \n\n appguardian.co/gk.apk")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso4)
                .buttonsColor(R.color.colorPrimary)
                .title("Ejecutar o abrir el archivo descargado.")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso5)
                .buttonsColor(R.color.colorPrimary)
                .title("Dar CLIC EN CONFIGURACIÓN para activar la instalación de aplicaciones de origen desconocido.")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso6)
                .buttonsColor(R.color.colorPrimary)
                .title("Ubíquese en ORÍGENES DESCONOCIDOS y active la casilla.")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso7)
                .buttonsColor(R.color.colorPrimary)
                .title("Clic en INSTALAR y luego Clic en FINALIZADO")
                .description(" Ya casi terminamos, ahora…")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso8)
                .buttonsColor(R.color.colorPrimary)
                .title("Entrar a, Conﬁguración y Luego Clic en ACCESIBILIDAD ")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso9)
                .buttonsColor(R.color.colorPrimary)
                .title("Clic en GuardianKids Y ACTIVAR la casilla")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.imgpaso10)
                .buttonsColor(R.color.colorPrimary)
                .title("Debe registrar el NÚMERO TELEFÓNICO DEL ADMINISTRADOR de la cuenta, y dar clic en REGISTRAR")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorBlanco)
                .buttonsColor(R.color.colorGris)
                .image(R.drawable.logoguardiankids)
                .buttonsColor(R.color.colorPrimary)
                .title("FELICITACIONES EN ESTE PUNTO YA TIENES CONFIGURADA LA APLICACIÓN")
                .build(),new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                whatsapp();
            }
        }, "SOPORTE TÉCNICO"));
    }


    private void whatsapp(){

        if(estaInstaladaAplicacion(this)){
            AbrirWhatsApp();
        }else{
            Toast.makeText(getApplicationContext(),"No tiene instalado whatsapp en su dispositivo..", Toast.LENGTH_SHORT).show();
        }
    }

    private void AbrirWhatsApp()
    {
        Intent _intencion = new Intent("android.intent.action.MAIN");
        _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("57" + "3202790068")+"@s.whatsapp.net");
        startActivity(_intencion);
    }


    private boolean estaInstaladaAplicacion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



}
