package com.example.utarbustrackingsystemapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScheduleActivity extends ActionBarActivity {

    Button button;
    ImageView image;

    WebView webView;
    public AlertDialog alert;
    JSONParser jParser = new JSONParser();
    public int[] scheduleId;
    public String[] route;
    public String[] busNo;
    public String[] topNote;
    public String[] bottomNote;
    public String[] timetable;
    public String[] date;

    public String[] cancelledRoute;
    public String[] cancelledBus;
    public String[] fromDate;
    public String[] toDate;
    public String[] fromTime;
    public String[] toTime;

    public Date currentDate;
    public Date currentTime;

    public DownloadTask dt = new DownloadTask();
    private ProgressDialog pd;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Show the ProgressDialog on this thread
        this.pd = ProgressDialog.show(this, "Working..", "Download data..", true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dt.cancel(true);
                        finish();
                    }
                }
        );

        // Start a new thread that will download all the data
        dt.execute("");

        //set background of action bar
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));
        getSupportActionBar().setElevation(25);*/
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fec353")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.schedule_actionbar_layout);

        //enable navigation icon button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#fec353"));
        }


        //check if connected to internet
        if(isConnected()){
            //System.out.println("Connected to internet");
        }
        else{
            System.out.println("Not connected to internet");
            buildAlertMessageNoInternet();
        }

        Criteria criteria = new Criteria();
        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        //check if gps is enabled
        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        //methods for on update of location goes into here
        final LocationListener mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {
                //check if connected to internet
                if(isConnected()){
                    //System.out.println("Connected to internet");
                }
                else{
                    System.out.println("Not connected to internet");
                    buildAlertMessageNoInternet();
                }

                //check if gps is enabled
                if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //request location updates
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, mLocationListener);

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
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url.contains("pdf")){
                    view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });*/
        //setContentView(mWebView);

        //addListenerOnButton();

        // show The Image
        /*new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
                .execute(Constant.retrieveImageURL);*/
    }

    public void initializeListView(){

        final ListView lv = (ListView) findViewById(R.id.scheduleLV);

        final ScheduleAdapter adapter = new ScheduleAdapter(this, route, busNo, date);

        lv.post(new Runnable() {
            public void run() {
                lv.setAdapter(adapter);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView routeTextView = (TextView) view.findViewById(R.id.route);
                //make textview scroll horizaontally
                if (routeTextView.isSelected()) {
                    routeTextView.setSelected(false);
                } else {
                    routeTextView.setSelected(true);
                }
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                TextView routeTextView = (TextView) view.findViewById(R.id.route);
                String routeStr = routeTextView.getText().toString();

                int cancelledIndex = 999999999;

                getDateTime();

                for(int i = 0; i < cancelledRoute.length; i++){
                    if(cancelledRoute[i].equals(routeStr) && cancelledBus[i].equals(busNo[position]) && (parseDate(fromDate[i]).equals(currentDate) || parseDate(toDate[i]).equals(currentDate) || (currentDate.after(parseDate(fromDate[i])) && currentDate.before(parseDate(toDate[i]))))){
                        cancelledIndex = i;
                    }
                }

                Intent intent = new Intent(ScheduleActivity.this, ScheduleContentActivity.class);
                intent.putExtra("route", routeStr);
                intent.putExtra("bus", busNo[position]);
                intent.putExtra("topNote", topNote[position]);
                intent.putExtra("bottomNote", bottomNote[position]);
                intent.putExtra("timetable", timetable[position]);
                intent.putExtra("date", date[position]);
                try{
                    intent.putExtra("cancelledRoute", cancelledRoute[cancelledIndex]);
                    intent.putExtra("cancelledBus", cancelledBus[cancelledIndex]);
                    intent.putExtra("fromDate", fromDate[cancelledIndex]);
                    intent.putExtra("toDate", toDate[cancelledIndex]);
                    intent.putExtra("fromTime", fromTime[cancelledIndex]);
                    intent.putExtra("toTime", toTime[cancelledIndex]);
                }catch(Exception e){

                }
                startActivity(intent);
                finish();
            }
        });

        //swipe to refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadTask().execute("");
            }
        });
    }

    //async task to download routes, bus, waypoints, news, and schedules from db
    private class DownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("route", String.valueOf("1"))); //simply put a value for parameter purposes

                    // getting JSON string from URL
                    JSONObject json = jParser.makeHttpRequest(Constant.retrieveScheduleURL, "GET", param);

                    // Check your log cat for JSON reponse
                    //Log.d("All Products: ", json.toString());
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        JSONArray data = json.getJSONArray("data");

                        //initialize size of arrays
                        scheduleId = new int[data.length()];
                        route = new String[data.length()];
                        busNo = new String[data.length()];
                        topNote = new String[data.length()];
                        bottomNote = new String[data.length()];
                        timetable = new String[data.length()];
                        date = new String[data.length()];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            scheduleId[i] = c.getInt("id");
                            route[i] = c.getString("route");
                            busNo[i] = c.getString("bus");
                            topNote[i] = c.getString("topNote");
                            bottomNote[i] = c.getString("bottomNote");
                            timetable[i] = c.getString("timetable");
                            date[i] = c.getString("date");
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(), "No products found", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                return "Done";
            }else{
                return "Cancelled";
            }
        }

        protected void onPostExecute(String result) {
            if(result == "Done"){
                if(scheduleId == null){
                    new DownloadTask().execute("");
                }else{
                    new CancelledRouteDownloadTask().execute("");
                }
            }
        }
    }

    //async task to download cancelled routes from db
    private class CancelledRouteDownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("route", String.valueOf("1"))); //simply put a value for parameter purposes

                    // getting JSON string from URL
                    JSONObject json = jParser.makeHttpRequest(Constant.retrieveNewsURL, "GET", param);

                    // Check your log cat for JSON reponse
                    //Log.d("All Products: ", json.toString());
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        JSONArray data = json.getJSONArray("data");

                        //initialize size of arrays
                        cancelledRoute = new String[data.length()];
                        cancelledBus = new String[data.length()];
                        fromDate = new String[data.length()];
                        toDate = new String[data.length()];
                        fromTime = new String[data.length()];
                        toTime = new String[data.length()];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            cancelledRoute[i] = c.getString("cancelledRoute");
                            cancelledBus[i] = c.getString("cancelledBus");
                            fromDate[i] = c.getString("fromDate");
                            toDate[i] = c.getString("toDate");
                            fromTime[i] = c.getString("fromTime");
                            toTime[i] = c.getString("toTime");
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(), "No products found", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                return "Done";
            }else{
                return "Cancelled";
            }
        }

        protected void onPostExecute(String result) {
            if(result == "Done"){
                if(cancelledRoute == null){
                    new CancelledRouteDownloadTask().execute("");
                }else{
                    initializeListView();
                    if (ScheduleActivity.this.pd != null) {
                        ScheduleActivity.this.pd.dismiss();
                    }
                    if(swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        }
    }

    public void getDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String strDate = date.format(c.getTime());
        String strTime = time.format(c.getTime());

        currentDate = parseDate(strDate);
        currentTime = parseTime(strTime);

        System.out.println(strDate + "/" + strTime);
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
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("HH:mm");
            t = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    private void buildAlertMessageNoInternet() {
        if( alert != null && alert.isShowing() ) return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your data seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        if(!((Activity) ScheduleActivity.this).isFinishing())
        {
            alert.show();
        }
    }

    private void buildAlertMessageNoGps() {
        if( alert != null && alert.isShowing() ) return;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        if(!((Activity) ScheduleActivity.this).isFinishing())
        {
            alert.show();
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /*@Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }*/

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
