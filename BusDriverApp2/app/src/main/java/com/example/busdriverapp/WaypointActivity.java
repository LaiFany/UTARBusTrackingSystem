package com.example.busdriverapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WaypointActivity extends AppCompatActivity {

    String bus;
    String routeNo;
    String routeName;
    String routeText;
    Double lat;
    Double lng;
    EditText busEditText;
    EditText routeEditText;
    EditText[] latEditText = new EditText[8];
    EditText[] lngEditText = new EditText[8];
    EditText[] wpNameEditText = new EditText[8];
    TextView[] latTextView = new TextView[8];
    TextView[] lngTextView = new TextView[8];
    TextView[] wpNameTextView = new TextView[8];
    TextView[] tv = new TextView[8];
    Button[] delBtn = new Button[8];
    Button[] gpsBtn = new Button[8];
    LinearLayout mainll;
    LinearLayout[] headerll = new LinearLayout[8];
    LinearLayout[] latll = new LinearLayout[8];
    LinearLayout[] lngll = new LinearLayout[8];
    LinearLayout[] namell = new LinearLayout[8];
    public AlertDialog alert;
    private ProgressDialog pd;
    public int code = 0;
    public int wpNo = 0;
    InputStream is = null;
    String finalName;
    String waypoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint);

        // Show the ProgressDialog on this thread
        this.pd = ProgressDialog.show(this, "Working..", "Getting GPS", true, true,
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }
        );

        getLocation();

        if(getIntent().hasExtra("bus") && getIntent().hasExtra("route")){
            bus = getIntent().getStringExtra("bus");
            routeText = getIntent().getStringExtra("route");
            routeNo = routeText.substring(6, 7);
            routeName = routeText.substring(10);
            System.out.println("Route no : " + routeNo + " Route name : " + routeName);
        }
        else{
            System.out.println("No extra passed to this activity");
        }

        initializeViews();
    }

    public void initializeViews(){
        mainll = (LinearLayout) findViewById(R.id.ll);
        busEditText = (EditText) findViewById(R.id.bus);
        routeEditText = (EditText) findViewById(R.id.route);

        busEditText.setText(bus);
        routeEditText.setText(routeText);
    }

    public void addViews(final int i, WaypointActivity v){
        headerll[i] = new LinearLayout(this);
        latll[i] = new LinearLayout(this);
        lngll[i] = new LinearLayout(this);
        namell[i] = new LinearLayout(this);
        latTextView[i] = new TextView(this);
        lngTextView[i] = new TextView(this);
        wpNameTextView[i] = new TextView(this);
        latEditText[i] = new EditText(this);
        lngEditText[i] = new EditText(this);
        wpNameEditText[i] = new EditText(this);
        tv[i] = new TextView(this);
        delBtn[i] = new Button(this);
        gpsBtn[i] = new Button(this);

        //set header params
        //set layout_weight, width and height for tv
        LinearLayout.LayoutParams paramText = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0f);
        paramText.height = LinearLayout.LayoutParams.MATCH_PARENT;
        paramText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramText.setMargins(0, 40, 0, 0);

        //set layout_weight, width and height for delBtn and gpsBtn
        LinearLayout.LayoutParams paramBtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);
        paramText.height = LinearLayout.LayoutParams.MATCH_PARENT;
        paramText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        //end of set header params

        //set layout_weight, width and height for latTextView and lngTextView
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0f);
        param.height = LinearLayout.LayoutParams.MATCH_PARENT;
        param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        param.setMargins(0, 40, 0, 0);

        //set layout_weight, width and height for latEditText
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        param2.height = LinearLayout.LayoutParams.MATCH_PARENT;
        param2.width = LinearLayout.LayoutParams.WRAP_CONTENT;

        //set layout_weight, width and height for lngEditText
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        param3.height = LinearLayout.LayoutParams.MATCH_PARENT;
        param3.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        param3.setMargins(0, 0, 0, 60);

        //set orientation
        headerll[i].setOrientation(LinearLayout.HORIZONTAL);
        latll[i].setOrientation(LinearLayout.HORIZONTAL);
        lngll[i].setOrientation(LinearLayout.HORIZONTAL);
        namell[i].setOrientation(LinearLayout.HORIZONTAL);

        //set width and height of ll
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerll[i].setLayoutParams(lpView);
        latll[i].setLayoutParams(lpView);
        lngll[i].setLayoutParams(lpView);
        namell[i].setLayoutParams(lpView);

        //set text
        latTextView[i].setText("Latitude");
        lngTextView[i].setText("Longitude");
        tv[i].setText("Waypoint " + (i + 1));
        wpNameTextView[i].setText("Name");
        delBtn[i].setText("Delete Waypoint");
        gpsBtn[i].setText("GPS");

        //set btn id
        delBtn[i].setId(i);
        gpsBtn[i].setId(i);

        //set btn functions
        delBtn[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(String.valueOf(delBtn[i].getId()));
                wpNo--;
                headerll[i].removeAllViews();
                latll[i].removeAllViews();
                lngll[i].removeAllViews();
                namell[i].removeAllViews();
                delBtn[i].setVisibility(View.GONE);
            }
        });

        gpsBtn[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(String.valueOf(gpsBtn[i].getId()));
                latEditText[i].setText(String.valueOf(lat));
                lngEditText[i].setText(String.valueOf(lng));
            }
        });

        //set params
        latTextView[i].setLayoutParams(param);
        lngTextView[i].setLayoutParams(param);
        wpNameTextView[i].setLayoutParams(param);
        latEditText[i].setLayoutParams(param2);
        lngEditText[i].setLayoutParams(param3);
        wpNameEditText[i].setLayoutParams(param2);
        tv[i].setLayoutParams(paramText);
        delBtn[i].setLayoutParams(paramBtn);
        gpsBtn[i].setLayoutParams(paramBtn);

        //ll[i].addView(tv[i]);
        headerll[i].addView(tv[i]);
        headerll[i].addView(delBtn[i]);
        headerll[i].addView(gpsBtn[i]);
        namell[i].addView(wpNameTextView[i]);
        namell[i].addView(wpNameEditText[i]);
        latll[i].addView(latTextView[i]);
        latll[i].addView(latEditText[i]);
        lngll[i].addView(lngTextView[i]);
        lngll[i].addView(lngEditText[i]);
        mainll.addView(headerll[i]);
        mainll.addView(namell[i]);
        mainll.addView(latll[i]);
        mainll.addView(lngll[i]);
    }

    public void getLocation() {
        Criteria criteria = new Criteria();
        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        //check if gps is enabled
        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        final LocationListener mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {

                System.out.println("getting coordinates");
                //get coordinates
                lat = location.getLatitude();
                lng = location.getLongitude();

                checkInternetConnection();

                //check if gps is enabled
                if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }

                if(code > 0){
                    if (WaypointActivity.this.pd != null) {
                        WaypointActivity.this.pd.dismiss();
                    }
                }
                code++;
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
        if(!((Activity) WaypointActivity.this).isFinishing())
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
        if(!((Activity) WaypointActivity.this).isFinishing())
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

    public void checkInternetConnection(){
        //check if connected to internet
        if(isConnected()){
            System.out.println("Connected to internet");
        }
        else{
            System.out.println("Not connected to internet");
            buildAlertMessageNoInternet();
        }
    }

    public void addWaypoint(View v){
        //delBtn[] to indicate whether it is still in view or not
        if(wpNo < 8){
            if(delBtn[0] == null || delBtn[0].getVisibility() == View.GONE){
                addViews(0, this);
            }
            else if(delBtn[1] == null || delBtn[1].getVisibility() == View.GONE){
                addViews(1, this);
            }
            else if(delBtn[2] == null || delBtn[2].getVisibility() == View.GONE){
                addViews(2, this);
            }
            else if(delBtn[3] == null || delBtn[3].getVisibility() == View.GONE){
                addViews(3, this);
            }
            else if(delBtn[4] == null || delBtn[4].getVisibility() == View.GONE){
                addViews(4, this);
            }
            else if(delBtn[5] == null || delBtn[5].getVisibility() == View.GONE){
                addViews(5, this);
            }
            else if(delBtn[6] == null || delBtn[6].getVisibility() == View.GONE){
                addViews(6, this);
            }
            else if(delBtn[7] == null || delBtn[7].getVisibility() == View.GONE){
                addViews(7, this);
            }
            wpNo++;
        }
        else{
            System.out.println("full");
        }
    }

    public void submit(View v){
        String[] name = new String[8];
        String[] lat = new String[8];
        String[] lng = new String[8];
        String[] finalLatLng = new String[8];
        finalName = "";
        waypoint = "";

        int successCode = 0;
        int j = 0;

        for(int i = 0; i < name.length; i++){
            if(delBtn[i] != null && delBtn[i].getVisibility() != View.GONE && wpNameEditText[i].getText().toString() != null && latEditText[i].getText().toString() != null && lngEditText[i].getText().toString() != null){
                if(wpNameEditText[i].getText().toString().length() > 1 && latEditText[i].getText().toString().length() > 1 && lngEditText[i].getText().toString().length() > 1  && latEditText[i].getText().toString().contains(".") && lngEditText[i].getText().toString().contains(".")){
                    name[j] = wpNameEditText[i].getText().toString();
                    lat[j] = latEditText[i].getText().toString();
                    lng[j] = lngEditText[i].getText().toString();
                    j++;
                }else{
                    successCode++;
                }
            }
        }

        if(successCode == 0){
            for(int i = 0; i < name.length; i++){
                if(name[i] != null && lat[i] != null && lng[i] != null){
                    finalName += name[i] + "|";
                    finalLatLng[i] = lat[i] + "," + lng[i];
                    waypoint += finalLatLng[i] + "|";
                }
            }

            finalName = finalName.substring(0, finalName.length()-1);
            waypoint = waypoint.substring(0, waypoint.length()-1);
            System.out.println(finalName);

            new waypointAsyncTask().execute("");
        }else{
            System.out.println("Missing fields");
        }
    }

    //send waypoints to server
    private class waypointAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //var for inserting data into db
            List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
            nvp.add(new BasicNameValuePair("routeNo", String.valueOf(routeNo)));
            nvp.add(new BasicNameValuePair("routeName", String.valueOf(routeName)));
            nvp.add(new BasicNameValuePair("bus", String.valueOf(bus)));
            nvp.add(new BasicNameValuePair("waypoint", String.valueOf(waypoint)));
            nvp.add(new BasicNameValuePair("stopNames", String.valueOf(finalName)));
            try {
                //insert data to db
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Constant.uploadInfoURL);
                hp.setEntity(new UrlEncodedFormEntity(nvp));
                HttpResponse hr = hc.execute(hp);
                HttpEntity entity = hr.getEntity();
                is = entity.getContent();

                String msg = "data entered successfully";
                System.out.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}