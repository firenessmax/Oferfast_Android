package com.tbd.tbd6.oferfas.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.Sesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MisOfertasFragment extends Fragment {
    ListView lvPublicaciones;
    View v;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MisOfertasFragment.
     */
    public static MisOfertasFragment newInstance() {
        MisOfertasFragment fragment = new MisOfertasFragment();
        return fragment;
    }

    public MisOfertasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mis_ofertas, container, false);
        lvPublicaciones = (ListView) v.findViewById(R.id.lvMisOfertas);
        String url = null;
        try {
            url = String.format(getResources().getString(R.string.url_s_d_s),"usuarios",
                    Sesion.getSession().getJSON("usuario").getInt("usuarioId"),"ofertas");

        } catch (JSONException e) {
            Log.e("TBD_", "Error al obtener id de usuario", e);
        }
        Log.i("TBD_", "url : "+url);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int max=response.length()>20?20:response.length();
                        ArrayList<Oferta> al=new ArrayList<Oferta>();

                        try {

                            for(int i =0;i<max;i++){
                                JSONObject jo = response.getJSONObject(response.length()-i-1);
                                Oferta oferta = new Oferta();
                                oferta.setTitle(jo.getString("title"));
                                oferta.setDescription(jo.getString("description"));
                                oferta.setPrice(jo.getInt("price"));
                                oferta.setLoc_lat(jo.getDouble("ubicationLat"));
                                oferta.setLoc_lon(jo.getDouble("ubicationLon"));
                                oferta.setUser_id(jo.getInt("usuarioId"));
                                oferta.setOferta_id(jo.getInt("ofertaId"));
                                al.add(oferta);
                            }
                        } catch (JSONException e) {
                            Log.e("TBD_", "Error al obtener ofertas", e);
                        }
                        MisOfertaAdapter oa= new MisOfertaAdapter(getActivity(),al, v.getResources());
                        Log.i("TBD_", lvPublicaciones.toString());
                        lvPublicaciones.setAdapter(oa);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TBD_","Error al obtener ofertas",error);
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonObjReq);
        return v;

    }


}