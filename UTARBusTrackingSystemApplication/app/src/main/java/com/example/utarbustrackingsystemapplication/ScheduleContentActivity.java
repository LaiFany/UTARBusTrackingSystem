package com.example.utarbustrackingsystemapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public String cancelledRoute = "";
    public String cancelledBus = "";
    public String fromDate = "";
    public String toDate = "";
    public String fromTime = "";
    public String toTime = "";

    public TextView routeTv;
    public TextView busTv;
    public TextView topNoteTv;
    public TextView bottomNoteTv;

    public Date currentDate;
    public Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_content);

        //set background of action bar
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));
        getSupportActionBar().setElevation(25);*/
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD800")));
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
            window.setStatusBarColor(Color.parseColor("#FFD800"));
        }

        initializeView();

        if(getIntent().hasExtra("route") && getIntent().hasExtra("bus") && getIntent().hasExtra("topNote") && getIntent().hasExtra("bottomNote") && getIntent().hasExtra("timetable") && getIntent().hasExtra("date")){

            if(getIntent().hasExtra("cancelledRoute") && getIntent().hasExtra("cancelledBus") && getIntent().hasExtra("fromDate") && getIntent().hasExtra("toDate") && getIntent().hasExtra("fromTime") && getIntent().hasExtra("toTime")){
                cancelledRoute = getIntent().getStringExtra("cancelledRoute");
                cancelledBus = getIntent().getStringExtra("cancelledBus");
                fromDate = getIntent().getStringExtra("fromDate");
                toDate = getIntent().getStringExtra("toDate");
                fromTime = getIntent().getStringExtra("fromTime");
                toTime = getIntent().getStringExtra("toTime");

                System.out.println(cancelledRoute + "  " + cancelledBus + "  " + fromDate + "  " + toDate + "  " + fromTime + "  " + toTime);
            }

            route = getIntent().getStringExtra("route");
            busNo = getIntent().getStringExtra("bus");
            topNote = getIntent().getStringExtra("topNote");
            bottomNote = getIntent().getStringExtra("bottomNote");
            timetable = getIntent().getStringExtra("timetable");
            date = getIntent().getStringExtra("date");

            routeTv.setText(route);
            busTv.setText(busNo);
            topNoteTv.setText(topNote + "\n\nNote: Greyed out columns indicate cancelled trips at that specific time.");
            bottomNoteTv.setText(bottomNote);

            boolean th = false;

            TableLayout ll = (TableLayout) findViewById(R.id.displayLinear);
                    //find out how many rows
            row = timetable.split("/");
            for(int i = 0; i < row.length; i++){
                String cancelled = "unchecked";
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

                    if(getIntent().hasExtra("cancelledRoute") && getIntent().hasExtra("cancelledBus") && getIntent().hasExtra("fromDate") && getIntent().hasExtra("toDate")){
                        getDateTime();

                        if(currentDate.compareTo(parseDate(fromDate)) >= 0 && currentDate.compareTo(parseDate(toDate)) <= 0){
                            if(getIntent().hasExtra("fromTime") && getIntent().hasExtra("toTime")){
                                if(!fromTime.equals("") && !toTime.equals("")){
                                    if(tv[j].getText().toString().contains("AM") || tv[j].getText().toString().contains("PM")){
                                        if(cancelled.equals("unchecked")){
                                            String time = tv[j].getText().toString();
                                            if(currentDate.compareTo(parseDate(fromDate)) >= 0 && parseTime(time).compareTo(parseTime(fromTime)) >= 0 && currentDate.compareTo(parseDate(toDate)) <= 0){
                                                if(currentDate.compareTo(parseDate(toDate)) == 0){
                                                    if(parseTime(time).compareTo(parseTime(toTime)) == 0 || parseTime(time).compareTo(parseTime(toTime)) < 0){
                                                        cancelled = "yes";
                                                    }else{
                                                        cancelled = "no";
                                                    }
                                                }else{
                                                    cancelled = "yes";
                                                }
                                            }else{
                                                cancelled = "no";
                                            }
                                        }
                                    }
                                }else{
                                    cancelled = "wholeday";
                                }
                            }
                        }
                        if(cancelled.equals("yes")){
                            for(int k = 0; k < tv.length; k++){
                                tv[k].setBackgroundResource(R.drawable.bordercancelled);
                                tv[k].setTextColor(Color.WHITE);
                            }
                        }
                        if(cancelled.equals("wholeday")){
                            tv[j].setBackgroundResource(R.drawable.bordercancelled);
                            tv[j].setTextColor(Color.WHITE);
                        }
                    }

                    if(tv[j].getText().equals("LUNCH")){
                        tv[j].setBackgroundResource(R.drawable.borderlunch);
                        tv[j].setTextColor(Color.WHITE);
                    }
                    if(i == 0){
                        tv[j].setBackgroundResource(R.drawable.borderheader);
                        tv[j].setTextColor(Color.WHITE);
                    }
                    tableRow.addView(tv[j]);
                }
                ll.addView(tableRow,i);
            }
        } else {
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

    public void getDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String strDate = date.format(c.getTime());
        String strTime = time.format(c.getTime());

        currentDate = parseDate(strDate);
        currentTime = parseTime(strTime);

        System.out.println(strDate + "/" + convertTime(currentTime));
    }

    public Date parseDate(String date){
        Date d = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("dd MMMM yyyy");
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public Date parseTime(String time){
        Date t = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("HH:mm");
        try {
            if(time.contains("AM") || time.contains("PM")){
                if(time.contains(".")){
                    dateFormat = new SimpleDateFormat ("hh.mma");
                    if(time.contains(" ")){
                        dateFormat = new SimpleDateFormat("hh.mm a");
                    }
                }else if(time.contains(":")){
                    dateFormat = new SimpleDateFormat ("hh:mma");
                    if(time.contains(" ")){
                        dateFormat = new SimpleDateFormat("hh:mm a");
                    }
                }
            }else{
                if(time.contains(".")){
                    dateFormat = new SimpleDateFormat ("HH.mm");
                }else if(time.contains(":")){
                    dateFormat = new SimpleDateFormat ("HH:mm");
                }
            }
            t = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    public String convertTime(Date time){
        return new SimpleDateFormat("h:mma").format(time);
    }

    //return day of week in int, sunday = 1, saturday = 7.
    public int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
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
                Intent i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                finish();
        }

        if (id == R.id.map) {
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.news) {
            Intent i = new Intent(this, NewsActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
