package com.nestnetgroup.guardinkids;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nestnetgroup.guardinkids.adapter_frame.Pager_Adapter;

public class Principal extends AppCompatActivity {

    TabLayout tabLayout;

    String ID_USUARIOS;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        tabLayout = findViewById(R.id.tabs);

        ID_USUARIOS=Mostrarpreferencia_id_usuario();
        Toolbar tolbar=findViewById(R.id.toolbar);
        setSupportActionBar(tolbar);

        if(ID_USUARIOS.equals("Not_config")){
            Intent intent=new Intent(getApplicationContext(),inicio.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        tabLayout.addTab(tabLayout.newTab().setText("Dispositivos"));
        tabLayout.addTab(tabLayout.newTab().setText("Reportes"));
        tabLayout.addTab(tabLayout.newTab().setText("Ajustes"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager =  findViewById(R.id.viewpager);

        final PagerAdapter adapter = new Pager_Adapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                crear_preferencia();
                return true;

            case R.id.actualizar:
                recreateActivityCompat(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String Mostrarpreferencia_id_usuario(){
        SharedPreferences prefs = getSharedPreferences("Cuenta", MODE_PRIVATE);
        String valor = prefs.getString("Id_usuario", "Not_config");
        return valor;
    }


    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }



    private void crear_preferencia(){

        SharedPreferences prefs = getSharedPreferences("Cuenta", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Id_usuario", "Not_config");
        editor.commit();
        Intent intent=new Intent(getApplicationContext(),inicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }


    public void finalizar(){
        finish();
    }




}
