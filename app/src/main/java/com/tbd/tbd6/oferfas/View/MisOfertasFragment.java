package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;

import java.util.ArrayList;
import java.util.List;


public class MisOfertasFragment extends Fragment {
    ListView lvPublicaciones;

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
        View v = inflater.inflate(R.layout.fragment_mis_ofertas, container, false);
        lvPublicaciones = (ListView) v.findViewById(R.id.lvMisOfertas);
        ArrayList<Oferta> al=new ArrayList<Oferta>();
        for(int i =0;i<10;i++){
            al.add(new Oferta());
        }
        OfertaAdapter oa= new OfertaAdapter(getActivity(),al, v.getResources());
        Log.i("TBD_", lvPublicaciones.toString());
        lvPublicaciones.setAdapter(oa);


        return v;

    }


}
