package com.tbd.tbd6.oferfas.View;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.TagExtractor;

import java.util.ArrayList;
import java.util.Arrays;

public class EditarOferta extends AppCompatActivity {
    Oferta of;
    EditText etTitulo;
    EditText etPrecio;
    EditText etDesc;
    Button btnAceptar;
    Button btnCancelar;
    ImageButton ButtonbtnAdd;
    ImageButton btnDelete;

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
        /** Control de Acciones **/

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etTitulo.setText(of.getTitle());
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etPrecio.setText(Integer.toString(of.getPrice()));
        etDesc = (EditText) findViewById(R.id.etDesc);
        etDesc.setText(of.getDescription());
        //botones
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        ButtonbtnAdd = (ImageButton) findViewById(R.id.btnAddImage);
        btnDelete = (ImageButton) findViewById(R.id.btnDeleteImage);
        btnAceptar.setOnClickListener(new onClickListenerEO());
        btnCancelar.setOnClickListener(new onClickListenerEO());
        ButtonbtnAdd.setOnClickListener(new onClickListenerEO());
        btnDelete.setOnClickListener(new onClickListenerEO());



        /** bloque para "slideview" **/
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
    private class onClickListenerEO implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAceptar:
                    //TODO: se debe realizar el llamado PUT con los cambios
                    Log.i("TBD_","Aceptando los cambios");
                    //si todos los cambios funcionan correctamente
                    TagExtractor te= new TagExtractor(etDesc.getText().toString());
                    for(String tag:te.extract()){
                        Log.i("TBD_","tag finded : "+tag);
                    }
                    if(false) {
                        onBackPressed();
                    }
                    break;
                case R.id.btnCancelar:
                    Log.i("TBD_","Rechazando los cambios");
                    if(true) {
                        onBackPressed();
                    }
                    break;
                case R.id.btnAddImage:
                    //TODO: se debe realizar los pasos necesarios para agregar una imagen
                    Log.i("TBD_","agregando imagen");
                    break;
                case R.id.btnDeleteImage:
                    //TODO: se debe realizar los pasos necesarios POST para eliminar una imagen
                    Log.i("TBD_","Eliminar Imagen");
                    break;
            }
        }
    }
}
