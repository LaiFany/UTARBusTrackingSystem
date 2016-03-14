package com.example.busdriverapp;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

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

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class JourneyActivity extends AppCompatActivity {

    JSONParser jParser = new JSONParser();
    InputStream is = null;

    String id1 = null;
    String routeNo1 = null;
    String routeName1 = null;
    String bus1 = null;
    String lat1 = null;
    String lng1 = null;
    String passengers1 = null;

    String routeNo;
    String routeName;
    String busNo;
    double lat = 0;
    double lng = 0;
    double time = 0;
    int noOfPassengers = 0;
    Location currentLocation;
    Location dest;
    TextView id;
    TextView route;
    TextView bus;
    TextView latt;
    TextView lngt;
    /*TextView passengers;
    TextView no;
    Button add;
    Button subtract;*/

    String connErrMsg = "Could not connect to Internet";
    int connStatus;
    public AlertDialog alert;
    private ProgressDialog pd;
    public int code = 0;
    public busAsyncTask busAsyncTask = new busAsyncTask();

    String defaultRoute;
    String defaultBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        // Show the ProgressDialog on this thread
        this.pd = ProgressDialog.show(this, "", "Initializing GPS", true, true,
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }
        );

        checkInternetConnection();

        //prevent phone from sleeping e.g. keeping display on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //set background of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2fb144")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.journey_actionbar_layout);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2fb144"));
        }

        initializeIntent();
        initializeViews();
        sendAndGetGPS();
    }

    public void initializeViews(){
        id = (TextView) findViewById(R.id.id);
        route = (TextView) findViewById(R.id.route);
        bus = (TextView) findViewById(R.id.bus);
        latt = (TextView) findViewById(R.id.lat);
        lngt = (TextView) findViewById(R.id.lng);
        /*passengers = (TextView) findViewById(R.id.passengers);
        no = (TextView) findViewById(R.id.no);
        add = (Button) findViewById(R.id.add);
        subtract = (Button) findViewById(R.id.subtract);*/
    }

    public void sendAndGetGPS() {
        Criteria criteria = new Criteria();
        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(provider);
        dest = new Location("utarCoordinates");
        dest.setLatitude(3.043831);
        dest.setLongitude(101.792268);

        System.out.println("sendAndGetGPS()");

        //check if gps is enabled
        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        final LocationListener mLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {

                //System.out.println("getting coordinates (lat, lng) and timestamp");
                //get coordinates
                lat = location.getLatitude();
                lng = location.getLongitude();
                time = location.getTime();
                currentLocation = location;

                checkInternetConnection();

                //check if gps is enabled
                if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }

                if(code > 0){
                    if (JourneyActivity.this.pd != null) {
                        JourneyActivity.this.pd.dismiss();
                    }
                    if(busAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                        //System.out.println("busAsyncTask running");
                    }else if(busAsyncTask.getStatus() == AsyncTask.Status.PENDING){
                        busAsyncTask.execute("");
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

    public double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    //async tasks to send gps data and obtain the last position of the bus
    private class busAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
           if(!isCancelled()){
               //var for inserting data into db
               List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
               nvp.add(new BasicNameValuePair("routeNo", String.valueOf(routeNo)));
               nvp.add(new BasicNameValuePair("routeName", String.valueOf(routeName)));
               nvp.add(new BasicNameValuePair("bus", String.valueOf(busNo)));
               nvp.add(new BasicNameValuePair("lat", String.valueOf(lat)));
               nvp.add(new BasicNameValuePair("lng", String.valueOf(lng)));
               //nvp.add(new BasicNameValuePair("time", String.valueOf(time)));
               //nvp.add(new BasicNameValuePair("passengers", String.valueOf(noOfPassengers)));
               try {
                   //insert data to db
                   HttpClient hc = new DefaultHttpClient();
                   HttpPost hp = new HttpPost(Constant.postOperationsURL);
                   hp.setEntity(new UrlEncodedFormEntity(nvp));
                   HttpResponse hr = hc.execute(hp);
                   HttpEntity entity = hr.getEntity();
                   is = entity.getContent();

                   String msg = "data entered successfully";
                   System.out.println(msg);
                   System.out.println("displaying data");
                   System.out.println("Latitude : " + String.valueOf(lat));
                   System.out.println("Longitude : " + String.valueOf(lng));
                   System.out.println("Time : " + String.valueOf(time));
                   //get distance to another location
                   System.out.println("Distance to UTAR : " + String.valueOf(roundTwoDecimals(currentLocation.distanceTo(dest) / 1000)) + " km");
                   //get accuracy down to meters
                   System.out.println("Accurate to : " + String.valueOf(currentLocation.getAccuracy()) + " m");
                   //get speed
                   System.out.println("Speed : " + String.valueOf(currentLocation.getSpeed()) + " m/s");
               } catch (Exception e) {
                   e.printStackTrace();
               }

               try{
                   List<NameValuePair> param = new ArrayList<NameValuePair>();
                   param.add(new BasicNameValuePair("routeNo", String.valueOf(routeNo)));

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

                       connStatus = data.length();

                       // looping through All Products
                       for (int i = 0; i < data.length(); i++) {
                           JSONObject c = data.getJSONObject(i);

                           // Storing each json item in variable
                           id1 = c.getString("id");
                           routeNo1 = c.getString("routeNo");
                           routeName1 = c.getString("routeName");
                           bus1 = c.getString("bus");
                           lat1 = c.getString("lat");
                           lng1 = c.getString("lng");
                           passengers1 = c.getString("passengers");
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

        @Override
        protected void onPostExecute(String result) {
            if(result == "Done"){
                if(connStatus == 0){
                    id.setText(connErrMsg);
                    route.setText(connErrMsg);
                    bus.setText(connErrMsg);
                    latt.setText(connErrMsg);
                    lngt.setText(connErrMsg);
                    //passengers.setText(connErrMsg);
                }else{
                    id.setText(id1);
                    route.setText("Route " + routeNo1 + " : " + routeName1);
                    bus.setText("Bus " + bus1);
                    latt.setText(lat1);
                    lngt.setText(lng1);
                    //passengers.setText(passengers1);
                }
                busAsyncTask = new busAsyncTask();
                busAsyncTask.execute("");
            }
        }
    }

    public void initializeIntent(){
        if(getIntent().hasExtra("route") && getIntent().hasExtra("bus") && getIntent().hasExtra("defaultRoute") && getIntent().hasExtra("defaultBus")){
            defaultRoute = getIntent().getStringExtra("defaultRoute");
            defaultBus = getIntent().getStringExtra("defaultBus");

            String busDisplay = getIntent().getStringExtra("bus");
            String routeDisplay = getIntent().getStringExtra("route");
            routeNo = routeDisplay.substring(6, 7);
            routeName = routeDisplay.substring(10);
            busNo = busDisplay.substring(4);
            System.out.println(routeNo);
        }
        else{
            System.out.println("No extra passed to this activity");
        }
    }

    //passengers operations
    /*public void add(View v){
        noOfPassengers++;
        no.setText(String.valueOf(noOfPassengers));
    }

    public void subtract(View v){
        if(noOfPassengers <= 0){
            noOfPassengers = 0;
        }else{
            noOfPassengers--;
        }
        no.setText(String.valueOf(noOfPassengers));
    }*/

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
        if(!((Activity) JourneyActivity.this).isFinishing())
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
        if(!((Activity) JourneyActivity.this).isFinishing())
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
            //System.out.println("Connected to internet");
        }
        else{
            System.out.println("Not connected to internet");
            buildAlertMessageNoInternet();
        }
    }

    public void completeJourney(View v){
        Intent i = new Intent(this, CompleteActivity.class);
        i.putExtra("defaultRoute", defaultRoute);
        i.putExtra("defaultBus", defaultBus);
        startActivity(i);
        cancelAsyncTask();
    }

    public void cancelAsyncTask(){
        if (busAsyncTask != null) {
            busAsyncTask.cancel(true);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelAsyncTask();
    }
}
