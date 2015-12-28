package com.tbd.tbd6.oferfas.utilities;

import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fireness on 28-12-15.
 */
public class JSONOferta {
    JSONObject jo;
    private String titulo;
    private String precio;
    private String descipcion;
    private Double lat;
    private Double lon;

    ArrayList<String> tags;

    public JSONOferta(String descipcion, String titulo, String precio,Double lat,Double lon) throws JSONException {
        this.jo = new JSONObject();

        this.setDescipcion(descipcion);
        this.setTitulo(titulo);
        this.setPrecio(precio);
        this.setLat(lat);
        this.setLon(lon);
    }

    @Override
    public String toString(){
        return jo.toString();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws JSONException {
        this.titulo = titulo;
        this.jo.put("title", this.titulo);
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) throws JSONException {
        this.precio = precio;
        this.jo.put("price", Integer.parseInt(this.precio));
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) throws JSONException {
        this.descipcion = descipcion;
        this.jo.put("description", this.descipcion);
        TagExtractor te = new TagExtractor(descipcion);
        this.tags = te.extract();
        Log.i("TBD_","tag:"+te.extractString());
        //if(jo.has("tags"))
        //    this.jo.remove("tags");//quitamos los tag para luego remplazarlos
        for(String tag:this.tags) {

            this.jo.accumulate("tags", tag);
        }

    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) throws JSONException {
        this.lat = lat;
        this.jo.put("ubicacionLat", lat);
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) throws JSONException {
        this.lon = lon;
        this.jo.put("ubicacionLon", lon);
    }
}
