package com.tbd.tbd6.oferfas.View;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.JSONOferta;
import com.tbd.tbd6.oferfas.utilities.TagExtractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class EditarOfertaActivity extends AppCompatActivity {
    Oferta of;
    Activity activity;
    EditText etTitulo;
    EditText etPrecio;
    EditText etDesc;
    Button btnAceptar;
    Button btnCancelar;
    ImageButton btnAdd;
    ImageButton btnDelete;
    ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 3;
    private int PICK_IMAGE_REQUEST = 4;


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
        of= new Oferta();
        activity= this;
        /** Control de Acciones **/


        etTitulo = (EditText) findViewById(R.id.etTitulo);
        //etTitulo.setText(of.getTitle());
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        //etPrecio.setText(Integer.toString(of.getPrice()));
        etDesc = (EditText) findViewById(R.id.etDesc);
        //etDesc.setText(of.getDescription());

        getAndSetDatos(bundle.getInt("oferta_id"));
        //botones
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnAdd = (ImageButton) findViewById(R.id.btnAddImage);
        btnDelete = (ImageButton) findViewById(R.id.btnDeleteImage);

        btnAceptar.setOnClickListener(new onClickListenerEO());
        btnCancelar.setOnClickListener(new onClickListenerEO());
        btnAdd.setOnClickListener(new onClickListenerEO());
        btnDelete.setOnClickListener(new onClickListenerEO());



        /** bloque para "slideview" **/
        imageView= (ImageView) findViewById(R.id.ivSlider);
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

    private void getAndSetDatos(int oferta_id) {
        String url = String.format(getResources().getString(R.string.url_s_d),"ofertas",oferta_id);
        of.setOferta_id(oferta_id);
        Log.i("TBD_","url : "+url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            etTitulo.setText(response.getString("title"));
                            of.setTitle(response.getString("title"));
                            etPrecio.setText("" + response.getInt("price"));
                            of.setPrice(response.getInt("price"));
                            etDesc.setText(response.getString("description"));
                            of.setDescription(response.getString("description"));
                        } catch (JSONException e) {
                            Log.e("TBD_", "154 : error al obtener los datos", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TBD_","164 : error al obtener los datos",error);
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjReq);
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
                    if(etTitulo.getText().length()>0&&
                            etDesc.getText().length()>0&&
                            etPrecio.getText().length()>0) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("title",etTitulo.getText().toString());
                            jo.put("description",etDesc.getText().toString());
                            jo.put("price",Integer.parseInt(etPrecio.getText().toString()));

                            Log.i("TBD_","json of edit:"+jo.toString());
                            Toast.makeText(v.getContext()
                                    ,"Realizando cambios..."
                                    ,Toast.LENGTH_SHORT).show();
                            //TODO: se debe realizar el llamado PUT con los cambios
                            String url=String.format(getResources().getString(R.string.url_s_d),
                                    "ofertas",of.getOferta_id());
                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                                    url,
                                    jo,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.i("TBD_","json : "+response.toString());
                                            onBackPressed();
                                        }
                                    },new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("TBD_","error al conectar",error);
                                        }

                                });
                            Log.i("TBD_","json [213]"+url);
                            Volley.newRequestQueue(activity).add(jsonObjReq);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(v.getContext()
                            ,"No puedes dejar campos vacíos"
                            ,Toast.LENGTH_SHORT).show();
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
                    dispatchGalleryImageIntent();

                    Log.i("TBD_","agregando imagen");
                    break;
                case R.id.btnDeleteImage:
                    //TODO: se debe realizar los pasos necesarios REST para eliminar una imagen
                    Log.i("TBD_","Eliminar Imagen");

                    if(counter>0){
                        Glide.with(v.getContext()).load(urlImages.get(counter-1)).into(imageView);
                        urlImages.remove(counter--);
                        Toast.makeText(v.getContext()
                                ,"Eliminando Imagen..."
                                ,Toast.LENGTH_SHORT).show();
                    }else{
                        if(urlImages.size()>1) {
                            Glide.with(v.getContext()).load(urlImages.get(1)).into(imageView);
                            urlImages.remove(0);
                            Toast.makeText(v.getContext()
                                    ,"Eliminando Imagen..."
                                    ,Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(v.getContext()
                                    ,"La Publicación debe tener al menos una imagen"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void dispatchGalleryImageIntent() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Seleciona una Imagen"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("TBD_", "size_x:" + imageBitmap.getWidth() + " size_y:" + imageBitmap.getHeight());*/
            /** Para Usar Camara**/

        }else if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null) {
            Uri uri = data.getData();
            urlImages.add(uri.toString());
            Glide.with(this).load(urlImages.get(urlImages.size()-1)).into(imageView);
            findViewById(R.id.btnPrev).setVisibility(View.VISIBLE);
            findViewById(R.id.btnNext).setVisibility(View.GONE);
            counter=urlImages.size()-1;
            //TODO: Usar servicio REST para subir imagen nueva. y ver forma de detectar cambios de las imagenes

        }
    }

}
