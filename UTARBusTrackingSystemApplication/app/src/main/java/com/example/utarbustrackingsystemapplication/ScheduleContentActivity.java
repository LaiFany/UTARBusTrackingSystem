package com.example.utarbustrackingsystemapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleContentActivity extends AppCompatActivity {

    WebView webView;
    public String route;
    public String busNo;
    public String topNote;
    public String bottomNote;
    public String timetable;
    public String date;
    public String[] row;
    public String[] col;

    public TextView routeTv;
    public TextView busTv;
    public TextView topNoteTv;
    public TextView bottomNoteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_content);

        //set background of action bar
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));
        getSupportActionBar().setElevation(25);*/
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fec353")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.schedule_content_actionbar_layout);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_gradient));

        //enable navigation icon button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#fec353"));
        }

        initializeView();

        if(getIntent().hasExtra("route") && getIntent().hasExtra("bus") && getIntent().hasExtra("topNote") && getIntent().hasExtra("bottomNote") && getIntent().hasExtra("timetable") && getIntent().hasExtra("date")){
            route = getIntent().getStringExtra("route");
            busNo = getIntent().getStringExtra("bus");
            topNote = getIntent().getStringExtra("topNote");
            bottomNote = getIntent().getStringExtra("bottomNote");
            timetable = getIntent().getStringExtra("timetable");
            date = getIntent().getStringExtra("date");

            routeTv.setText(route);
            busTv.setText(busNo);
            topNoteTv.setText(topNote);
            bottomNoteTv.setText(bottomNote);

            boolean th = false;

            TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);
                    //find out how many rows
            row = timetable.split("/");
            for(int i = 0; i < row.length; i++){
                TableRow tableRow= new TableRow(this);
                tableRow.setBackgroundResource(R.drawable.border);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setLayoutParams(lp);

                //find max height and width of textview and standardize all shorter heights to it
                int maxHeight = 0;
                int maxWidth = 0;

                //find out how many columns
                col = row[i].split("\\|");
                TextView[] tv = new TextView[col.length];
                for(int j = 0; j < col.length; j++){
                    tv[j] = new TextView(this);
                    tv[j].setPadding(5, 5, 5, 5);
                    tv[j].setText(col[j]);
                    tv[j].setEms(6);
                    tv[j].setBackgroundResource(R.drawable.border);
                    if(tv[j].getText().equals("LUNCH")){
                        tv[j].setBackgroundResource(R.drawable.borderlunch);
                        tv[j].setTextColor(Color.WHITE);
                    }
                    if(i == 0){
                        tv[j].setBackgroundResource(R.drawable.borderheader);
                        tv[j].setTextColor(Color.WHITE);
                    }
                    tv[j].measure(0, 0);
                    if(maxHeight < tv[j].getMeasuredHeight()){
                        maxHeight = tv[j].getMeasuredHeight();
                    }
                    if(maxWidth < tv[j].getMeasuredWidth()){
                        maxWidth = tv[j].getMeasuredWidth();
                    }
                }

                for(int j = 0; j < col.length; j++){
                    tv[j].setHeight(maxHeight);
                    tv[j].setWidth(maxWidth);
                }

                for(int j = 0; j < col.length; j++){
                    if(tv[0].getText().toString().contains("*")){
                        tv[j].setBackgroundResource(R.drawable.borderfriday);
                        tv[j].setTextColor(Color.WHITE);
                    }
                    tableRow.addView(tv[j]);
                }
                ll.addView(tableRow,i);
            }
        }
        else{
            System.out.println("No extra passed to this activity");
        }

        /*webView = (WebView) findViewById(R.id.webView);
        webView.setBackgroundColor(0x00000000);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + "https://www.utar.edu.my/media/BusSchedule/2016/SL/SL18Jan2016.pdf");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pdf")) {
                    view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });*/
    }

    public void initializeView(){
        routeTv = (TextView) findViewById(R.id.route);
        busTv = (TextView) findViewById(R.id.bus);
        topNoteTv = (TextView) findViewById(R.id.topNote);
        bottomNoteTv = (TextView) findViewById(R.id.bottomNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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

        if(id == R.id.map){
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
            finish();
        }
        if(id == R.id.news){
            Intent i = new Intent(this, NewsActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
