package com.tbd.tbd6.oferfas.View;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class HomeActivity extends AppCompatActivity {
    Button login;
    Activity activity;
    String urlLogin;
    EditText etUser,etPass;
    JSONObject jo;
    @Bind(R.id.btnNoAccount)
        TextView registro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity=this;
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        urlLogin = String.format(getResources().getString(R.string.url_s_s),"usuarios","login");
        setContentView(R.layout.activity__);
        ButterKnife.bind(this);
        etUser = (EditText) findViewById(R.id.etUsername_login);
        etPass = (EditText) findViewById(R.id.etPass_login);
        login =(Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUser.getText().length()==0||etPass.getText().length()==0){
                    Toast.makeText(activity,"Los campos no pueden estar vacíos",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                jo= new JSONObject();
                try {
                    jo.put("username",etUser.getText().toString());
                    jo.put("password",etPass.getText().toString());
                } catch (JSONException e){e.printStackTrace();}

                etUser.setVisibility(View.GONE);
                etPass.setVisibility(View.GONE);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                login.setText(R.string.title_activity_logining);
                login.setEnabled(false);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        urlLogin,
                        jo,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.i("TBD_", "Logueado : " + response.getString("INFO"));

                                    if(response.getString("INFO").compareTo("Loggeado")==0) {
                                        response.put("password", etPass.getText().toString());
                                        Sesion.getSession().set("usuario", response);
                                        Intent myIntent = new Intent(activity, MainActivity.class);
                                        activity.startActivity(myIntent);
                                    }else{
                                        Log.e("TBD_", "Fallo al loguear : " + response.getString("INFO"));
                                        Toast.makeText(activity,response.getString("INFO"),
                                                Toast.LENGTH_LONG).show();
                                        etUser.setVisibility(View.VISIBLE);
                                        etPass.setVisibility(View.VISIBLE);
                                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                                        login.setText(R.string.title_activity_login);
                                        login.setEnabled(true);
                                    }

                                } catch (JSONException e) {
                                    Log.e("TBD_",
                                        "no contiene ni info ni error, debe ser error del servidor",e);
                                    etUser.setVisibility(View.VISIBLE);
                                    etPass.setVisibility(View.VISIBLE);
                                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                                    login.setText(R.string.title_activity_login);
                                    login.setEnabled(true);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("TBD_", "Error : " + error);
                                Snackbar.make(activity.findViewById(R.id.mainLayout),R.string.error_conect,Snackbar.LENGTH_LONG).show();
                                etUser.setVisibility(View.VISIBLE);
                                etPass.setVisibility(View.VISIBLE);
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                login.setText(R.string.title_activity_login);
                                login.setEnabled(true);
                            }
                        }
                );
                Volley.newRequestQueue(activity).add(jsonObjReq);
            }
        });
        //
    }
    @OnClick(R.id.btnNoAccount)
    public void registrarse(View button){
        Intent myIntent = new Intent(this, RegistroActivity.class);
        this.startActivity(myIntent);
    }
}
