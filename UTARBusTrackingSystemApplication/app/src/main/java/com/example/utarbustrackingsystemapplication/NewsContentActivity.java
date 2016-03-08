package com.example.utarbustrackingsystemapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class NewsContentActivity extends AppCompatActivity {

    public String newsId;
    public String newsTitle;
    public String newsDesc;
    public String newsContent;
    public String date;

    public TextView title;
    public TextView desc;
    public TextView content;
    public TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        //set background of action bar
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));
        getSupportActionBar().setElevation(25);*/
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b172b2")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.news_content_actionbar_layout);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_gradient));

        //enable navigation icon button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#b172b2"));
        }

        initializeViews();

        if(getIntent().hasExtra("newsId") && getIntent().hasExtra("newsTitle") && getIntent().hasExtra("newsContent")){
            newsId = getIntent().getStringExtra("newsId");
            newsTitle = getIntent().getStringExtra("newsTitle");
            newsDesc = getIntent().getStringExtra("newsDesc");
            newsContent = getIntent().getStringExtra("newsContent");
            date = getIntent().getStringExtra("date");

            title.setText(Html.fromHtml("<u>" + newsTitle + "</u>"));
            desc.setText(newsDesc);
            content.setText(newsContent);
            dateTextView.setText(date);

        }
        else{
            System.out.println("No extra passed to this activity");
        }
    }

    public void initializeViews(){
        title = (TextView) findViewById(R.id.newsTitle);
        desc = (TextView) findViewById(R.id.newsDesc);
        content = (TextView) findViewById(R.id.newsContent);
        dateTextView = (TextView) findViewById(R.id.date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        if(id == R.id.schedule){
            Intent i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
            finish();
        }
        if(id == R.id.map){
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
