package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;

import java.util.ArrayList;
import java.util.Arrays;

public class EditarOferta extends AppCompatActivity {
    Oferta of;
    EditText etTitulo;
    EditText etPrecio;
    EditText etDesc;
    ProgressBar cargando;
    int counter=0;
    /*
        1 - http://goo.gl/gEgYUd
        2 - https://goo.gl/lgKXnH
        3 - http://goo.gl/ue2EtA
        4 - http://goo.gl/SEYRGB
        5 - http://goo.gl/ctd66g

     */
    ArrayList<String> urlImages =new ArrayList<String>(Arrays.asList(
            "http://goo.gl/gEgYUd",
            "https://goo.gl/lgKXnH",
            "http://goo.gl/ue2EtA",
            "http://goo.gl/SEYRGB",
            "http://goo.gl/ctd66g"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_oferta);

        Bundle bundle = getIntent().getExtras();
        of= new Oferta(bundle.getInt("oferta_id"));
        final ImageView imageView = (ImageView) findViewById(R.id.ivSlider);
        cargando= (ProgressBar)findViewById(R.id.pbCargando);
        cargando.setVisibility(View.VISIBLE);
        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
        cargando.setVisibility(View.GONE);
        Button next=(Button) findViewById(R.id.btnNext);
        Button prev=(Button) findViewById(R.id.btnPrev);
        final Activity este=this;
        
        if(urlImages.size()<2)next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter==0)este.findViewById(R.id.btnPrev).setVisibility(View.VISIBLE);
                if(urlImages.size()>counter+1){
                    cargando.setVisibility(View.VISIBLE);
                    Glide.with(este).load(urlImages.get(++counter)).into(imageView);
                    cargando.setVisibility(View.GONE);
                }
                if(urlImages.size()==counter+1) este.findViewById(R.id.btnNext).setVisibility(View.GONE);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlImages.size()==counter+1) este.findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
                if(counter>0){
                    cargando.setVisibility(View.VISIBLE);
                    Glide.with(este).load(urlImages.get(--counter)).into(imageView);
                    cargando.setVisibility(View.GONE);
                }
                if(counter==0) este.findViewById(R.id.btnPrev).setVisibility(View.GONE);
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_opt, menu);
        return true;
    }
}
