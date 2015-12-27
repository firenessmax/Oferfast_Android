package com.tbd.tbd6.oferfas.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ListView lvPublicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvPublicaciones = (ListView) findViewById(R.id.lvMisOfertas);
        ArrayList<Oferta> al=new ArrayList<Oferta>();
        for(int i =0;i<5;i++){
            al.add(new Oferta());
        }
        OfertaAdapter oa= new OfertaAdapter(this,al,getResources());
        lvPublicaciones.setAdapter(oa);

    }
}
