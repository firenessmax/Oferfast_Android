package com.tbd.tbd6.oferfas.View;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    int imageCount=0;
    //era parte del turorial
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int PICK_IMAGE_REQUEST = 2;


    Button capture;
    public NewOfertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imagenes = new ArrayList<Uri>();
        ReactiveLocationProvider lp = new ReactiveLocationProvider(getContext());
        lp.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
               last=location;
            }
        });
        v=inflater.inflate(R.layout.fragment_new_oferta, container, false);
        c = getContext();
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment_new_oferta
        capturas =(GridLayout) v.findViewById(R.id.glCapturas);
        capture = (Button) v.findViewById(R.id.btnCapture);
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
        capture.setOnClickListener(new View.OnClickListener() {
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
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("TBD_", "size_x:" + imageBitmap.getWidth() + " size_y:" + imageBitmap.getHeight());
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(imageBitmap);
            capturas.addView(imageView);
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
                &&precio.getText().length()>0 &&desc.getText().length()>0){
            if(imageCount==0){
                Toast.makeText(getContext(),
                        "Debes seleccionar al menos una foto",Toast.LENGTH_SHORT).show();
                return;
            }
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
                json.put("imagesNumber",0);
                json.put("visibleOferta",1);
                Log.i("TBD_","json:"+json);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url,
                        json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("TBD_",response.toString());
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
                //TODO: llamada REST para subir imagenes

            } catch (JSONException e) {
                Toast.makeText(getContext(),"Error al cargar Oferta",Toast.LENGTH_LONG).show();
                Log.e("TBD_","error:",e);
                e.printStackTrace();
            }



        }
    }

}
