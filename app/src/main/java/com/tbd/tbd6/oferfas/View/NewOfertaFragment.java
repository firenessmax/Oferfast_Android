package com.tbd.tbd6.oferfas.View;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.JSONOferta;
import com.tbd.tbd6.oferfas.utilities.Sesion;
import com.tbd.tbd6.oferfas.utilities.SystemUtilities;
import com.tbd.tbd6.oferfas.utilities.UploadCouldinary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOfertaFragment extends Fragment {

    View v;
    Context c;
    GridLayout capturas;
    ArrayList<Uri> imagenes;
    Location last;
    EditText titulo;
    EditText precio;
    EditText desc;
    @Bind(R.id.btnAceptar) Button aceptar;
    Button cancelar;
    ImageButton camera, gallery;
    JSONArray jsonImagenes;

    String resultado;
    private Uri imageUri;

    int imageCount=0;
    //era parte del turorial
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int PICK_IMAGE_REQUEST = 2;



    public NewOfertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imagenes = new ArrayList<Uri>();
        jsonImagenes = new JSONArray();
        ReactiveLocationProvider lp = new ReactiveLocationProvider(getContext());
        lp.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                last = location;
            }
        });
        SystemUtilities.verifyStoragePermissions(getActivity());
        v=inflater.inflate(R.layout.fragment_new_oferta, container, false);
        c = getContext();
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment_new_oferta
        capturas =(GridLayout) v.findViewById(R.id.glCapturas);
        //capture = (Button) v.findViewById(R.id.btnCapture);
        camera = (ImageButton) v.findViewById(R.id.ibFromCamera);
        gallery = (ImageButton) v.findViewById(R.id.ibFromGallery);
        titulo = (EditText) v.findViewById(R.id.etTitulo);
        precio = (EditText) v.findViewById(R.id.etPrecio);
        desc = (EditText) v.findViewById(R.id.etDesc);
        cancelar =(Button) v.findViewById(R.id.btnCancelar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Oferta Descartada",Toast.LENGTH_LONG).show();
                destroyAll();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageCount<4)
                    dispatchTakePictureIntent();
                else
                    Toast.makeText(getContext(),R.string.max_image,Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageCount<4)
                    dispatchGalleryImageIntent();
                else
                    Toast.makeText(getContext(),R.string.max_image,Toast.LENGTH_SHORT).show();
            }
        });
        return this.v;
    }
    private void destroyAll(){
        for(;imageCount>0;imageCount--)
            imagenes.remove(0);
        Log.i("TBD_","image.size:"+imagenes.size());
        titulo.setText("");precio.setText("");desc.setText("");
        capturas.removeAllViews();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File folder = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/OferfastPics");
            if(!folder.exists()){
                folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            File photo = new File(Environment.getExternalStorageDirectory(),  "/OferfastPics/Pic"+currentDateandTime+".jpg");
            imageUri = Uri.fromFile(photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            Log.i("TBD_", "uri : " + imageUri.toString());
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void dispatchGalleryImageIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleciona una Imagen"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ImageView imageView = new ImageView(getContext());
            // Se muestra en pantalla como un bitmap
            Glide.with(getContext())
                    .load(imageUri)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(300);
            imageView.setOnLongClickListener(new OnImageLongClickListener());
            capturas.addView(imageView);
            imagenes.add(Uri.parse(imageUri.toString()));
            imageCount++;
        }else if (requestCode == PICK_IMAGE_REQUEST &&
                    resultCode == Activity.RESULT_OK &&
                        data != null && data.getData() != null) {
            Uri uri = data.getData();

            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext())
                    .load(uri)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(300);
            imageView.setOnLongClickListener(new OnImageLongClickListener());
            capturas.addView(imageView);
            imagenes.add(uri);
            imageCount++;
            Log.w("TBD_","file : "+(new File(uri.toString())));
        }else{
            Log.e("TBD_","error al cargar imagen");
        }
    }
    private class OnImageLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(final View viu) {
            new AlertDialog.Builder(getContext(),4)
                    .setTitle("Quitar Foto")
                    .setMessage("¿Seguro que no quieres agregar esta imagen?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int index=( (GridLayout) viu.getParent()).indexOfChild(viu);
                            ( (GridLayout) viu.getParent()).removeView(viu);
                            imagenes.remove(index);
                            imageCount--;
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
    }
    @OnClick(R.id.btnAceptar)
    public void publicar(Button viu){
        if(titulo.getText().length()>0
                &&precio.getText().length()>0 &&desc.getText().length()>0)            {
            if(imageCount==0){
                Toast.makeText(getContext(),
                        "Debes seleccionar al menos una foto",Toast.LENGTH_SHORT).show();
                return;
            }
            int isize = imagenes.size()-1;
            int i=0;
            for(Uri imagen:imagenes) {
                if(isize<i++) {
                    AsyncTask<String, Void, String> execute = new UploadCouldinary(getActivity().getApplicationContext(),
                            new UploadCouldinary.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    try {
                                        resultado = output;
                                        JSONObject jo = new JSONObject();
                                        String name = resultado.split("/")[7];
                                        jo.put("urlNormal", ("http://res.cloudinary.com/fireness/image/upload/" + name));
                                        jo.put("urlThumbnail","http://res.cloudinary.com/fireness/image/upload/w_300,h_200,c_fill/"+name );
                                        jsonImagenes.put(jo);
                                    }catch (JSONException je){
                                        Log.e("TBD_","error al crear JSON");
                                    }

                                }
                            }, imagen).execute();
                }else{
                    AsyncTask<String, Void, String> execute = new UploadCouldinary(getActivity().getApplicationContext(),
                            new UploadCouldinary.AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    try {
                                        resultado = output;
                                        JSONObject jo = new JSONObject();
                                        String name = resultado.split("/")[7];
                                        jo.put("urlNormal", ("http://res.cloudinary.com/fireness/image/upload/" + name));
                                        jo.put("urlThumbnail","http://res.cloudinary.com/fireness/image/upload/w_300,h_200,c_fill/"+name );
                                        jsonImagenes.put(jo);
                                        adjuntarImagenes();
                                    }catch (JSONException je){
                                        Log.e("TBD_","error al crear JSON");
                                    }
                                }
                            }, imagen).execute();
                }

            }
        }

            }
    public void adjuntarImagenes(){

    try {
        JSONOferta jo = new JSONOferta(
                desc.getText().toString(),
                titulo.getText().toString(),
                precio.getText().toString(),
                last.getLatitude(),
                last.getLongitude());
        Toast.makeText(getContext(),"Cargando publicacion...",Toast.LENGTH_LONG).show();

        JSONObject usuario = Sesion.getSession().getJSON("usuario");

        String url = String.format(getResources().getString(R.string.url_s),
                "ofertas");
        JSONObject json = jo.getJo();
        json.put("usuarioId",usuario.getInt("usuarioId"));
        json.put("imagesNumber",jsonImagenes.length());
        json.put("imagenes",jsonImagenes);
        json.put("visibleOferta", 1);
        Log.i("TBD_","json:"+json);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TBD_", response.toString());
                        final JSONObject resp= response;


                        destroyAll();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c,"Error al conectar al servidor",Toast.LENGTH_SHORT).show();
                        Log.e("TBD_","Error ",error);
                    }
                });
        Volley.newRequestQueue(c).add(jsonObjReq);
    } catch (JSONException e) {
        Toast.makeText(getContext(),"Error al cargar Oferta",Toast.LENGTH_LONG).show();
        Log.e("TBD_","error:",e);
        e.printStackTrace();
    }
}}
