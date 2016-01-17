package com.tbd.tbd6.oferfas.utilities;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadCouldinary extends AsyncTask<String,Void,String> {

    Map<String,String> Result;
    String resultado;
    private Context context;
    Cloudinary cloudinary;
    public  AsyncResponse delegate = null;
    Uri imagen;
    public interface  AsyncResponse{
        void processFinish(String output);
    }

    public UploadCouldinary(Context context, AsyncResponse delegate, Uri img) {
        this.imagen=img;
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Map config = new HashMap();
        config.put("cloud_name", "fireness");
        config.put("api_key", "777251731144235");
        config.put("api_secret", "3wr39mtCA7zMAeTXytks4kjmlCQ");
        cloudinary = new Cloudinary(config);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            InputStream fis = context.getContentResolver().openInputStream(imagen);
            Result =   cloudinary.uploader().upload(fis, new HashMap());
            resultado = Result.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(s);
    }
}

