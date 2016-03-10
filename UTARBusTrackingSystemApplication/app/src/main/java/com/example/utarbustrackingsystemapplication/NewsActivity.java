package com.example.utarbustrackingsystemapplication;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;


public class NewsActivity extends ActionBarActivity {

    WebView webView;
    public AlertDialog alert;
    ArrayList<String> news_array_list;
    JSONParser jParser = new JSONParser();
    public int[] newsId;
    public String[] newsTitle;
    public String[] newsDesc;
    public String[] newsContent;
    public String[] date;
    public DownloadTask dt = new DownloadTask();
    private ProgressDialog pd;

    public int[] unreadNewsId;
    public int[] readNewsId;
    SwipeRefreshLayout swipeRefreshLayout;

    public boolean newsClicked = false;
    public int newsClickedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b172b2")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.news_actionbar_layout);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_gradient));

        //enable navigation icon button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#b172b2"));
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
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        //webView.setInitialScale(1);
        //webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("http://www.utar.edu.my/eLink.jsp?fyear=2016&fcatid=16");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("pdf")){
                    view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });*/
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

    public void initializeSharedPreferences(){

        //get shared preferences for number of times logged in
        int n = 1;
        SharedPreferences loginSP = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(loginSP.contains("loginNumber")) {
            n = loginSP.getInt("loginNumber", 1);
        }
        //set shared preferences for unread news
        SharedPreferences unreadNewsSP = getSharedPreferences("unreadNews", Context.MODE_PRIVATE);
        SharedPreferences readNewsSP = getSharedPreferences("readNews", Context.MODE_PRIVATE);
        SharedPreferences.Editor unreadNewsE = unreadNewsSP.edit();
        SharedPreferences.Editor readNewsE = readNewsSP.edit();

        //e.putString("newsId", newsIdString);
        //e.commit();
        if(n == 1 && !(newsClicked)){
            String readNewsText = "";
            for(int i = 0; i < newsId.length; i++){
                readNewsId[i] = newsId[i];
                if(readNewsText.equals("")){
                    readNewsText += String.valueOf(newsId[i]);
                }else if(!readNewsText.substring(readNewsText.length() - 1).equals(",")){
                    readNewsText += String.valueOf("," + newsId[i]);
                }
            }
            readNewsE.putString("readNewsId", readNewsText);
            unreadNewsE.putString("unreadNewsId", "");
            readNewsE.commit();
            unreadNewsE.commit();
        }else{
            String readNewsText = "";
            String unreadNewsText = "";
            if(readNewsSP.contains("readNewsId")){
                readNewsText = readNewsSP.getString("readNewsId", "");
                String[] tempArray = readNewsText.split(",");
                int[] readNewsIdArray = new int[tempArray.length];
                for(int i = 0; i < tempArray.length; i++){
                    readNewsIdArray[i] = Integer.parseInt(tempArray[i]);
                }

                int[] indexToDelete = new int[readNewsIdArray.length];
                int index = 0;

                //eliminate deleted newsId from readNewsId array
                if(readNewsIdArray.length >= newsId.length){
                    for(int i = 0; i < readNewsIdArray.length; i++){
                        for(int j = 0; j < newsId.length; j++){
                            if(readNewsIdArray[i] == newsId[j]){
                                break;
                            }
                            if(j + 1 == newsId.length){
                                System.out.println("readNewsIdArray["+i+"] = " + readNewsIdArray[i] + " is not found in the list updated newsId array");
                                System.out.println("Removing the element");
                                indexToDelete[index] = i;
                                index++;
                            }
                        }
                    }
                }else if(newsId.length > readNewsIdArray.length){
                    for(int i = 0; i < newsId.length; i++){
                        for(int j = 0; j < readNewsIdArray.length; j++){
                            if(newsId[i] == readNewsIdArray[j]){
                                break;
                            }
                            if(j + 1 == readNewsIdArray.length && newsId[i] < readNewsIdArray[readNewsIdArray.length - 1]){
                                System.out.println("readNewsIdArray["+j+"] = " + readNewsIdArray[j] + " is not found in the list updated newsId array");
                                System.out.println("Removing the element");
                                indexToDelete[index] = j;
                                index++;
                            }
                        }
                    }
                }

                //remove invalid elements from readNewsIdArray[]
                for(int i = indexToDelete.length - 1; i >= 0; i--){
                    if(indexToDelete[i] != '\0') {
                        readNewsIdArray = ArrayUtils.remove(readNewsIdArray, indexToDelete[i]);
                    }
                }

                if(newsClicked){
                    int[] temparray = new int[readNewsIdArray.length + 1];
                    if(readNewsIdArray != null && temparray != null){
                        for(int i = 0; i < temparray.length; i++){
                            if(i + 1 == temparray.length){
                                temparray[i] = newsClickedId;
                            }else{
                                temparray[i] = readNewsIdArray[i];
                            }
                        }
                        readNewsIdArray = new int[temparray.length];
                        for(int i = 0; i < readNewsIdArray.length; i++){
                            readNewsIdArray[i] = temparray[i];
                        }

                        //sorting elements in readNewsId[] in ascending order
                        //selection sort
                        for(int i = 0; i < readNewsIdArray.length; i++){
                            int min = readNewsIdArray[i];
                            int minj = i;
                            for(int j = i; j< readNewsIdArray.length; j++){
                                if(readNewsIdArray[j] < min){
                                    min = readNewsIdArray[j];
                                    minj = j;
                                }
                            }
                            int temp = readNewsIdArray[i];
                            readNewsIdArray[i] = readNewsIdArray[minj];
                            readNewsIdArray[minj] = temp;
                        }
                    }
                    System.out.println("clicked");
                }

                //get unread news
                unreadNewsId = new int[newsId.length];
                indexToDelete = new int[unreadNewsId.length];
                index = 0;
                for(int i = 0; i < newsId.length; i++){
                     for(int j = 0; j < readNewsIdArray.length; j++){
                         if(newsId[i] == readNewsIdArray[j]){
                             break;
                         }
                         if(j + 1 == readNewsIdArray.length){
                             unreadNewsId[index] = newsId[i];
                             index++;
                         }
                     }
                }

                index = 0;
                //indicate empty array slots in unreadNewsId[]
                for(int i = 0; i < unreadNewsId.length; i++){
                    if(unreadNewsId[i] == '\0'){
                        indexToDelete[index] = i;
                        index++;
                    }
                }

                //remove empty elements from readNewsIdArray[]
                for(int i = indexToDelete.length - 1; i >= 0; i--){
                    if(indexToDelete[i] != '\0'){
                        unreadNewsId = ArrayUtils.remove(unreadNewsId, indexToDelete[i]);
                        System.out.println("unreadNewsId length = " + unreadNewsId.length);
                    }
                }

                //set shared preference for read news
                readNewsText = "";
                for(int i = 0; i < readNewsIdArray.length; i++){
                    if(readNewsText.equals("")){
                        readNewsText += String.valueOf(readNewsIdArray[i]);
                    }else if(!readNewsText.substring(readNewsText.length() - 1).equals(",")){
                        readNewsText += String.valueOf("," + readNewsIdArray[i]);
                    }
                }

                readNewsE.putString("readNewsId", readNewsText);
                readNewsE.commit();
            }

            /*if(unreadNewsSP.contains("unreadNewsId")){
                unreadNewsText = unreadNewsSP.getString("unreadNewsId", "");
                if(!unreadNewsText.equals("")){
                    String[] tempArray = unreadNewsText.split(",");
                    int[] unreadNewsIdArray = new int[tempArray.length];
                    for(int i = 0; i < tempArray.length; i++){
                        unreadNewsIdArray[i] = Integer.parseInt(tempArray[i]);
                    }

                    //eliminate deleted newsId from unreadNewsId array
                    if(unreadNewsIdArray.length > newsId.length){
                        for(int i = 0; i < unreadNewsIdArray.length; i++){
                            for(int j = 0; j < newsId.length; j++){
                                if(unreadNewsIdArray[i] == newsId[j]){
                                    break;
                                }
                                if(j + 1 == newsId.length){
                                    System.out.println("readNewsIdArray["+i+"] = " + unreadNewsIdArray[i] + " is not found in the list updated newsId array");
                                    System.out.println("Removing the element");
                                    unreadNewsIdArray = ArrayUtils.remove(unreadNewsIdArray, i);
                                }
                            }
                        }
                    }
                    if(newsId.length > unreadNewsIdArray.length){
                        for(int i = 0; i < newsId.length; i++){
                            for(int j = 0; j < unreadNewsIdArray.length; j++){
                                if(newsId[i] == unreadNewsIdArray[j]){
                                    break;
                                }
                                if(j + 1 == unreadNewsIdArray.length && newsId[i] < unreadNewsIdArray[unreadNewsIdArray.length - 1]){
                                    System.out.println("readNewsIdArray["+j+"] = " + unreadNewsIdArray[j] + " is not found in the list updated newsId array");
                                    System.out.println("Removing the element");
                                    unreadNewsIdArray = ArrayUtils.remove(unreadNewsIdArray, i);
                                }
                            }
                        }
                    }

                    //set shared preference of unread news
                    unreadNewsText = "";
                    for(int i = 0; i < unreadNewsIdArray.length; i++){
                        if(unreadNewsText.equals("")){
                            unreadNewsText += String.valueOf(unreadNewsIdArray[i]);
                        }else if(!unreadNewsText.substring(unreadNewsText.length() - 1).equals(",")){
                            unreadNewsText += String.valueOf("," + unreadNewsIdArray[i]);
                        }
                    }
                    unreadNewsE.putString("unreadNewsId", unreadNewsText);
                    unreadNewsE.commit();
                }
            }*/
        }
    }

    public void initializeListView(){

        final ListView lv = (ListView) findViewById(R.id.newsLV);

        final NewsAdapter adapter = new NewsAdapter(this, newsId, newsTitle, newsDesc, date, unreadNewsId);

        lv.post(new Runnable() {
            public void run() {
                lv.setAdapter(adapter);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView desc = (TextView) view.findViewById(R.id.desc);
                //make textview scroll horizaontally
                if (title.isSelected()) {
                    title.setSelected(false);
                } else {
                    title.setSelected(true);
                }
                if (desc.isSelected()) {
                    desc.setSelected(false);
                } else {
                    desc.setSelected(true);
                }
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                TextView txtitem = (TextView) view.findViewById(R.id.title);
                String item = txtitem.getText().toString();

                int pos = newsId.length - position - 1;

                System.out.println(pos);

                newsClicked = true;
                newsClickedId = newsId[pos];
                initializeSharedPreferences();
                initializeListView();

                Intent intent = new Intent(NewsActivity.this, NewsContentActivity.class);
                intent.putExtra("newsId", String.valueOf(newsId[pos]));
                intent.putExtra("newsTitle", newsTitle[pos]);
                intent.putExtra("newsDesc", newsDesc[pos]);
                intent.putExtra("newsContent", newsContent[pos]);
                intent.putExtra("date", date[pos]);
                startActivity(intent);
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
                        newsId = new int[data.length()];
                        newsTitle = new String[data.length()];
                        newsDesc = new String[data.length()];
                        newsContent = new String[data.length()];
                        date = new String[data.length()];
                        unreadNewsId = new int[data.length()];
                        readNewsId = new int[data.length()];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            newsId[i] = c.getInt("id");
                            newsTitle[i] = c.getString("newsTitle");
                            newsDesc[i] = c.getString("newsDesc");
                            newsContent[i] = c.getString("newsContent");
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
                if(newsId == null){
                    new DownloadTask().execute("");
                }else{
                    initializeSharedPreferences();
                    initializeListView();
                    if (NewsActivity.this.pd != null) {
                        NewsActivity.this.pd.dismiss();
                    }
                    if(swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        }
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
        if(!((Activity) NewsActivity.this).isFinishing())
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
        if(!((Activity) NewsActivity.this).isFinishing())
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
            finish();
            return;
        }
    }*/
}
