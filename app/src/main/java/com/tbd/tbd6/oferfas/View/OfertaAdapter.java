package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbd.tbd6.oferfas.Models.Oferta;
import com.tbd.tbd6.oferfas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fireness on 27-12-15.
 */
public class OfertaAdapter  extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Oferta tempValues=null;

    int i=0;

    public OfertaAdapter(Activity a, ArrayList d,Resources resLocal) {

        activity = a;
        data=d;
        res = resLocal;

        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public TextView titulo;
        public TextView desc;
        public TextView fecha;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            vi = inflater.inflate(R.layout.oferta_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.titulo = (TextView) vi.findViewById(R.id.titulo);
            holder.desc=(TextView)vi.findViewById(R.id.desc);
            holder.fecha=(TextView)vi.findViewById(R.id.fecha);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.titulo.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( Oferta ) data.get( position );

            /************  Set Model values in Holder elements ***********/
            SimpleDateFormat sm = new SimpleDateFormat("mm/dd");
            holder.titulo.setText(((tempValues.getTitle().length()>40)?
                    tempValues.getTitle().substring(0,40)+"...":
                    tempValues.getTitle() ));
            holder.desc.setText( ((tempValues.getDescription().length() > 80)?
                    tempValues.getDescription().substring(0,80)+"..." :
                    tempValues.getDescription()));
            holder.fecha.setText(sm.format(tempValues.getDate()) );
            //holder.text1.setText( tempValues.getUrl() );


            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
            vi.setOnLongClickListener(new OnItemLongClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("TBD_", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Log.i("TBD_","click on :"+((Oferta)getItem(mPosition)).getOferta_id());
            //TODO:Intent a edit oferta
            Intent myIntent = new Intent(activity, EditarOferta.class);
            myIntent.putExtra("oferta_id",((Oferta)getItem(mPosition)).getOferta_id());
            activity.startActivity(myIntent);
        }
    }
    private class OnItemLongClickListener  implements View.OnLongClickListener {
        private int mPosition;

        OnItemLongClickListener(int position){
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            Log.i("TBD_","mostrando Dialog...");
            //TODO:Mostrar Dialogo de editar y weas
            //((CheckBox)v.findViewById(R.id.cbSelected)).setVisibility(View.VISIBLE);
            return true;
        }
    }
}
