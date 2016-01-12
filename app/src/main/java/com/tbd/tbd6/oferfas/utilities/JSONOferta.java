package com.tbd.tbd6.oferfas.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fireness on 28-12-15.
 */
public class JSONOferta {
    private JSONObject jo;
    private String titulo;
    private String precio;
    private String descipcion;
    private Double lat;
    private Double lon;
    private int id;

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
        return this.jo.toString();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws JSONException {
        this.titulo = titulo;
        this.getJo().put("title", this.titulo);
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) throws JSONException {
        this.precio = precio;
        this.getJo().put("price", Integer.parseInt(this.precio));
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) throws JSONException {
        this.descipcion = descipcion;
        this.getJo().put("description", this.descipcion);
        TagExtractor te = new TagExtractor(descipcion);
        this.tags = te.extract();
        Log.i("TBD_","tag:"+te.extractString());
        JSONArray arTag =new JSONArray();
        for(String tag:tags) arTag.put(tag);
        this.getJo().put("tags", arTag);
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) throws JSONException {
        this.lat = lat;
        this.getJo().put("ubicationLat", lat);
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) throws JSONException {
        this.lon = lon;
        this.getJo().put("ubicationLon", lon);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws JSONException {
        this.id = id;
        this.getJo().put("ofertaId", id);
    }

    public JSONObject getJo() {
        return jo;
    }
}
