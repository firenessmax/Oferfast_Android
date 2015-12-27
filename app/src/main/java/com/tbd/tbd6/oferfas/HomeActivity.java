package com.tbd.tbd6.oferfas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

    }
}
