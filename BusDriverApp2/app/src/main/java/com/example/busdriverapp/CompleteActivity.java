package com.example.busdriverapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CompleteActivity extends AppCompatActivity {

    String defaultRoute;
    String defaultBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        //set background of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2fb144")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2fb144"));
        }

    }

    public void initializeIntent(){
        if(getIntent().hasExtra("defaultRoute") && getIntent().hasExtra("defaultBus")){
            defaultRoute = getIntent().getStringExtra("defaultRoute");
            defaultBus = getIntent().getStringExtra("defaultBus");
        }
        else{
            System.out.println("No extra passed to this activity");
        }
    }

    public void exitApp(View v){
        finish();
    }

    public void startNewJourney(View v){
        initializeIntent();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("defaultRoute", defaultRoute);
        i.putExtra("defaultBus", defaultBus);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
