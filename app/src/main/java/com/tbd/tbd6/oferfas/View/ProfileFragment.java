package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends Fragment  {

    @Bind(R.id.tvUsername) TextView username;
    @Bind(R.id.tvOfertas) TextView nofertas;
    @Bind(R.id.tvSeguidores) TextView nseguidores;
    @Bind(R.id.tvSeguidos) TextView nseguidos;
    @Bind(R.id.ivFotoPerfil) ImageView fotoperfil;


    @Bind(R.id.opass) EditText oldpass;
    @Bind(R.id.npass) EditText newpass;
    @Bind(R.id.rnpass) EditText reppass;
    @Bind(R.id.btnChangePass) Button toggle;
    @Bind(R.id.btnConfirmChange) Button aceptar;
    @Bind(R.id.btnCancelChange) Button cancelar;

    Sesion s;
    Context c;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        s = Sesion.getSession();
        JSONObject U=null;
        try {
            U= s.getJSON("usuario");
            username.setText("@"+U.getString("username"));
            //TODO: get numero de ofertas
            nofertas.setText(""+0 +(int)(Math.random()*999) );
            nseguidores.setText("" + 0 + (int) (Math.random() * 999));
            nseguidos.setText("" + 0 + (int) (Math.random() * 999));
            String fpUrl = U.getString("urlProfileThumbnail");
            if(fpUrl.compareTo("no hay thumb")==0) fpUrl= "http://cdn3.rd.io/user/no-user-image-square.jpg";
            Glide.with(getContext())
                    .load(fpUrl)
                    .centerCrop()
                    .crossFade()
                    .into(fotoperfil);

        } catch (JSONException e) {

            Log.e("TBD_","user : "+U,e);

        }

        c=getContext();
        return view;
    }
    @OnClick(R.id.btnChangePass)
    public void toggleCHP(Button btn){
        oldpass.setVisibility(View.VISIBLE);
        newpass.setVisibility(View.VISIBLE);
        reppass.setVisibility(View.VISIBLE);

        aceptar.setVisibility(View.VISIBLE);
        cancelar.setVisibility(View.VISIBLE);

        toggle.setVisibility(View.GONE);
    }
    @OnClick(R.id.btnCancelChange)
    public void cancelarCHP(Button btn){
        oldpass.setText("");oldpass.setVisibility(View.GONE);
        newpass.setText("");newpass.setVisibility(View.GONE);
        reppass.setText("");reppass.setVisibility(View.GONE);

        aceptar.setVisibility(View.GONE);
        cancelar.setVisibility(View.GONE);

        toggle.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.btnConfirmChange)
    public void AceptarCHP(Button btn){
        oldpass.setVisibility(View.GONE);newpass.setVisibility(View.GONE);
        reppass.setVisibility(View.GONE);aceptar.setVisibility(View.GONE);
        cancelar.setVisibility(View.GONE);toggle.setVisibility(View.VISIBLE);

        try {
            JSONObject usuario = s.getJSON("usuario");
            if(oldpass.getText().toString().compareTo(usuario.getString("password"))!=0){
                Toast.makeText(c,"Contraseña actual no coincide",Toast.LENGTH_SHORT).show();
                return;
            }

            if(newpass.getText().toString().compareTo(reppass.getText().toString())!=0){
                Toast.makeText(c,"Contraseñas no Coinciden",Toast.LENGTH_SHORT).show();
                return;
            }
            String url = String.format(getResources().getString(R.string.url_s_d),"usuarios",usuario.getInt("usuarioId"));
            JSONObject jo = new JSONObject();
            jo.put("password", newpass.getText().toString());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("INFO").compareTo("Datos actualizados")==0){
                                Toast.makeText(c,"Contraseña actualizada",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                           Log.e("TBD_","error del json al actualizar contraseña",e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c,"Error al conectar al servidor",Toast.LENGTH_SHORT).show();
                    }
                });
            Volley.newRequestQueue(getActivity()).add(jsonObjReq);
        } catch (JSONException e) {
            Log.e("TBD_","Error al cambiar la contraseña",e);
        }

    }




}
