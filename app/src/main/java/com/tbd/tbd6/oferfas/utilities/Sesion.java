package com.tbd.tbd6.oferfas.utilities;

import android.content.pm.PackageInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by fireness on 03-01-16.
 */
public class Sesion {
    JSONObject session;
    private static Sesion instance=null;
    public Sesion(){
        this.session = new JSONObject();
    }
    public static Sesion getSession(){
        if(instance==null){
            instance=new Sesion();
        }
        return instance;
    }
    public String getString(String key) throws JSONException {
        return session.getString(key);
    }
    public int getInt(String key) throws JSONException {
        return session.getInt(key);
    }
    public JSONArray getArray(String key) throws JSONException {
        return session.getJSONArray(key);
    }
    public boolean getBoolean(String key) throws JSONException {
        return session.getBoolean(key);
    }
    public double getDouble(String key) throws JSONException {
        return session.getDouble(key);
    }
    public long getLong(String key) throws JSONException {
        return session.getLong(key);
    }
    public Iterator<String> getKeys(){
        return session.keys();
    }
    public Sesion set(String key,Object obj) throws JSONException {
        session.put(key,obj);
        return this;
    }
    public Sesion addIn(String key,Object obj) throws JSONException {
        session.accumulate(key, obj);
        return this;
    }

    public JSONObject getJSON(String key) throws JSONException {
        return session.getJSONObject(key);
    }



}
