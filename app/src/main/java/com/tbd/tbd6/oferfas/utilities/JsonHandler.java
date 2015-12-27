package com.tbd.tbd6.oferfas.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: Jefferson Morales De la Parra
 * Clase que se utiliza para manipular objetos JSON
 */
public class JsonHandler {

    /**
     * Método que recibe un JSONArray en forma de String y devuelve un String[] con los actores
     */
    public String[] getActors(String actors) {
        try {
            JSONArray ja = new JSONArray(actors);
            String[] result = new String[ja.length()];
            String actor;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject row = ja.getJSONObject(i);
                actor = " " + row.getString("firstName") + " " + row.getString("lastName");
                result[i] = actor;
            }
            return result;
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }// getActors(String actors)
    public String[] getDetail(String actors,int pos){
        try {
            JSONArray ja = new JSONArray(actors);
            JSONObject row = ja.getJSONObject(pos);
            String[] result = new String[2];
            result[0]=row.getString("actorId");
            result[1]=row.getString("lastUpdate");
            return result;
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }
}// JsonHandler