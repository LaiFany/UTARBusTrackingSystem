package com.example.systemadminapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    JSONParser jParser = new JSONParser();
    private ProgressDialog pd = null;
    private Object data = null;

    Spinner busSpinner;
    Spinner routeSpinner;
    ArrayAdapter<String> busAdapter;
    ArrayAdapter<String> routeAdapter;

    public int noOfRoutesInTable;

    public String[] routeId;
    public String[] routeNo;
    public String[] routeName;
    public String[] bus;
    public String[] stopNames;
    public String[] coordinates;
    public AlertDialog alert;

    String busText;
    String routeText;

    final DownloadTask dt = new DownloadTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the ProgressDialog on this thread
        this.pd = ProgressDialog.show(this, "", "Initializing Content", true, true,
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dt.cancel(true);
                        finish();
                    }
                }
        );

        // Start a new thread that will download all the data
        dt.execute("");

        checkInternetConnection();

        //set background of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e94167")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#e94167"));
        }

        //prevent phone from sleeping e.g. keeping display on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initializeViews();

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
                checkInternetConnection();

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
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
                    checkInternetConnection();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("info", String.valueOf("1"))); //simply put a value for parameter purposes

                    // getting JSON string from URL
                    JSONObject json = jParser.makeHttpRequest(Constant.getOperationsURL, "GET", param);

                    // Check your log cat for JSON reponse
                    //Log.d("All Products: ", json.toString());
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        JSONArray data = json.getJSONArray("data");

                        noOfRoutesInTable = data.length();
                        routeId = new String[noOfRoutesInTable];
                        routeNo = new String[noOfRoutesInTable];
                        routeName = new String[noOfRoutesInTable];
                        bus = new String[noOfRoutesInTable];
                        stopNames = new String[noOfRoutesInTable];
                        coordinates = new String[noOfRoutesInTable];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            routeId[i] = c.getString("id");
                            routeNo[i] = c.getString("routeNo");
                            routeName[i] = c.getString("routeName");
                            bus[i] = c.getString("bus");
                            stopNames[i] = c.getString("stopNames");
                            coordinates[i] = c.getString("waypoint");
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
            // Pass the result data back to the main activity
            if(result == "Done"){
                if(routeNo == null || routeName == null || bus == null){
                    // Start a new thread that will download all the data
                    new DownloadTask().execute("");
                }else{
                    //update spinner
                    for(int i = 0; i < routeNo.length; i++) {
                        if(routeNo[i] != null && routeName[i] != null){
                            routeAdapter.add("Route " + routeNo[i] + " : " + routeName[i]);
                        }
                        if(bus[i] != null) {
                            busAdapter.add("Bus " + bus[i]);
                        }
                    }
                    busSpinner.setAdapter(busAdapter);
                    routeSpinner.setAdapter(routeAdapter);

                    if (MainActivity.this.pd != null) {
                        MainActivity.this.pd.dismiss();
                    }
                }
            }
        }
    }

    public void initializeViews(){
        busSpinner = (Spinner) findViewById(R.id.busNo);
        routeSpinner = (Spinner) findViewById(R.id.routeNo);

        //adding dynamic data into spinner
        busAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
        routeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>());
    }

    public void initializeIntent(){
        if(getIntent().hasExtra("defaultRoute") && getIntent().hasExtra("defaultBus")){
            for(int i = 0; i < routeSpinner.getCount(); i++){
                if(routeSpinner.getItemAtPosition(i).toString().equals(getIntent().getStringExtra("defaultRoute"))){
                    routeSpinner.setSelection(i);
                    break;
                }
            }
            for(int i = 0; i < busSpinner.getCount(); i++) {
                if (busSpinner.getItemAtPosition(i).toString().equals(getIntent().getStringExtra("defaultBus"))){
                    busSpinner.setSelection(i);
                    break;
                }
            }
        }
        else{
            System.out.println("No extra passed to this activity");
        }
    }

    /*public void start(View v){
        SharedPreferences sp = getSharedPreferences("abc", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        busText = busSpinner.getSelectedItem().toString();
        routeText = routeSpinner.getSelectedItem().toString();

        e.putString("bus", busText);
        e.putString("route", routeText);
        e.commit();

        Toast.makeText(this, "Saved sucessfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, JourneyActivity.class);
        startActivity(intent);
    }*/

    public void setWayPoint(View v){
        busText = busSpinner.getSelectedItem().toString();
        routeText = routeSpinner.getSelectedItem().toString();
        Intent i = new Intent(this, WaypointActivity.class);
        String busNo;
        busNo = busText.substring(4);

        //find selected index
        int index = busSpinner.getSelectedItemPosition();

        i.putExtra("routeId", routeId[index]);
        i.putExtra("bus", bus[index]);
        i.putExtra("routeNo", routeNo[index]);
        i.putExtra("routeName", routeName[index]);
        i.putExtra("stopNames", stopNames[index]);
        i.putExtra("coordinates", coordinates[index]);
        startActivity(i);
        finish();
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
        if(!((Activity) MainActivity.this).isFinishing())
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
        if(!((Activity) MainActivity.this).isFinishing())
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
}
