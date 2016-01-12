package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tbd.tbd6.oferfas.R;
import com.tbd.tbd6.oferfas.utilities.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistroActivity extends AppCompatActivity {
    @Bind(R.id.et_username) EditText user;
    @Bind(R.id.et_pass) EditText pass;
    @Bind(R.id.et_rpass) EditText rpass;
    @Bind(R.id.et_email) EditText email;
    @Bind(R.id.pb_registro) ProgressBar cargando;
    @Bind(R.id.btn_registro) Button btnRegistro;
    Activity dis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        dis=this;
    }
    @OnClick(R.id.btn_registro)
    public void registrarse(Button button)  {

        if(user.getText().length()==0
                ||pass.getText().length()==0
                ||rpass.getText().length()==0
                ||email.getText().length()==0
                ){
            Toast.makeText(this,"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.getText().toString().compareTo(rpass.getText().toString())!=0){
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return;
        }
        onwait();
        JSONObject jo = new JSONObject();
        try {
            jo.put("username",user.getText().toString());
            jo.put("email",email.getText().toString());
            jo.put("password",pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = String.format(getResources().getString(R.string.url_s),"usuarios");
        Log.i("TBD_","registrando");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TBD_","logueado : "+ response.toString());
                        try {
                            String info = response.getString("INFO");
                            if(info.compareTo("Usuario creado exitosamente")==0){
                                JSONObject joUser= new JSONObject();
                                joUser.put("username",user.getText().toString());
                                joUser.put("email",email.getText().toString());
                                joUser.put("password",pass.getText().toString());
                                joUser.put("type",1);
                                joUser.put("reputation",1);
                                joUser.put("urlProfilePicture","http://cdn3.rd.io/user/no-user-image-square.jpg");
                                joUser.put("urlProfileThumbnail","http://cdn3.rd.io/user/no-user-image-square.jpg");
                                joUser.put("usuarioId",response.getInt("usuarioId"));

                                Sesion s = Sesion.getSession();
                                s.set("usuario",joUser);

                                Intent myIntent = new Intent(dis, MainActivity.class);
                                myIntent.putExtra("from","registro");
                                dis.startActivity(myIntent);
                            }
                            else{
                                Log.e("TBD_","error "+info);
                                Toast.makeText(dis,info,Toast.LENGTH_SHORT).show();
                                stopwait();
                            }
                        } catch (JSONException e) {
                            Log.e("TBD_","error "+e.getMessage());
                            e.printStackTrace();
                            stopwait();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TBD_","error "+error.getMessage());
                        Toast.makeText(dis,"Error al Conectar, intentelo mas tarde",Toast.LENGTH_SHORT).show();
                        stopwait();
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjReq);

    }
    private void onwait(){
        cargando.setVisibility(View.VISIBLE);
        user.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        pass.setVisibility(View.GONE);
        rpass.setVisibility(View.GONE);
        btnRegistro.setText("Registrando...");
        btnRegistro.setEnabled(false);
    }
    private void stopwait(){
        cargando.setVisibility(View.INVISIBLE);
        user.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);
        rpass.setVisibility(View.VISIBLE);
        btnRegistro.setText(R.string.registro_btn);
        btnRegistro.setEnabled(true);
    }
}
