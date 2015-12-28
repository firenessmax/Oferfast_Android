package com.tbd.tbd6.oferfas.View;


import android.app.Activity;
import android.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.JSONOferta;

import org.json.JSONException;

import java.util.ArrayList;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOfertaFragment extends Fragment {

    View v;
    GridLayout capturas;
    ArrayList<Uri> imagenes;
    Location last;
    EditText titulo;
    EditText precio;
    EditText desc;
    Button aceptar;
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
        this.v=inflater.inflate(R.layout.fragment_new_oferta, container, false);
        // Inflate the layout for this fragment_new_oferta
        capturas =(GridLayout) v.findViewById(R.id.glCapturas);
        capture = (Button) v.findViewById(R.id.btnCapture);
        titulo = (EditText) v.findViewById(R.id.etTitulo);
        precio = (EditText) v.findViewById(R.id.etPrecio);
        desc = (EditText) v.findViewById(R.id.etDesc);
        aceptar = (Button) v.findViewById(R.id.btnAceptar);
        cancelar =(Button) v.findViewById(R.id.btnCancelar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viu) {
                if(titulo.getText().length()>0
                        &&precio.getText().length()>0 &&desc.getText().length()>0){
                    if(imageCount==0){
                        Toast.makeText(getContext(),"Debes seleccionar al menos una foto",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //TODO: llamada REST para crear una publicacion
                    try {

                        Log.i("TBD_", "location:" + last);
                        JSONOferta jo = new JSONOferta(
                                desc.getText().toString(),
                                titulo.getText().toString(),
                                precio.getText().toString(),
                                last.getLatitude(),
                                last.getLongitude());
                        Toast.makeText(getContext(),"Cargando publicacion...",Toast.LENGTH_LONG).show();
                        Log.i("TBD_","json:"+jo.toString());
                        //TODO: llamada REST para subir imagenes
                        destroyAll();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),"Error al cargar Oferta",Toast.LENGTH_LONG).show();
                        Log.e("TBD_","error:"+e.getMessage());
                        e.printStackTrace();
                    }



                }

            }
        });
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

}
