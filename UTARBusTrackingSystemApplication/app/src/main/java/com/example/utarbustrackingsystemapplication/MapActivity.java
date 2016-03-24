package com.example.utarbustrackingsystemapplication;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.graphics.Point;
        import android.graphics.drawable.ColorDrawable;
        import android.location.*;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.SystemClock;
        import android.provider.Settings;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.view.animation.Interpolator;
        import android.view.animation.LinearInterpolator;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.github.florent37.materialtextfield.MaterialTextField;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.Projection;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;

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
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class MapActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    Context cont = this;

    // Progress Dialog
    private ProgressDialog pd;
    private Object data = null;

    JSONParser jParser = new JSONParser();
    private int success;//to determine JSON signal insert success/fail

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    double lat = 0;
    double lng = 0;
    long time = 0;

    List<Marker[]> mWayPointList = new ArrayList<Marker[]>();
    List<String[]> mWayPointLocations = new ArrayList<String[]>();
    List<String[]> stopNamesUnconvertedList = new ArrayList<String[]>();

    String[] asyncId;
    String[] asyncRouteNo;
    String[] asyncRouteName;
    String[] asyncBus;
    String[] asyncLat;
    String[] asyncLng;
    //String[] asyncPassengers;
    String[] waypoint;
    String[] stopNamesUnconverted;
    float[] userETA;
    Marker[] m;
    Marker[] mWayPoint;
    Location[] busLoc;
    Location utarLoc;

    public AlertDialog alert;

    public EditText idView;
    public EditText route;
    public EditText bus;
    public EditText distanceToUser;
    public EditText distanceToUtar;
    public EditText etaToUser;
    public EditText status;
    //public EditText noOfPassengers;

    public MaterialTextField mtfId;
    public MaterialTextField mtfRoute;
    public MaterialTextField mtfBus;
    public MaterialTextField mtfDistanceToUser;
    public MaterialTextField mtfDistanceToUtar;
    public MaterialTextField mtfEtaToUser;
    public MaterialTextField mtfStatus;
    //public MaterialTextField mtfNoOfPassengers;

    public Location currentLocation;
    public int selectedRoute = 100;
    public int noOfRouteInTable;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    CustomListAdapter adapter1;
    ListView list;

    Polyline[] line;

    String[] itemRouteNo;
    String[] itemRouteName;

    ArrayList<Integer> imgid = new ArrayList<Integer>();

    private static final LatLng PRIMO = new LatLng(3.06891,101.78851);
    private static final LatLng UTAR = new LatLng(3.0408043087568917,101.79446781107264);
    private static final LatLng TBS = new LatLng(3.076111,101.710556);
    public DownloadTask dt = new DownloadTask();
    public busAsyncTask busAsyncTask;
    public int activeBusMarker = 100;
    public int activeMarkerClickCount = 0;
    public int markerVisibilityCount = 0;

    public String[] cancelledRoute;
    public String[] cancelledBus;
    public String[] fromDate;
    public String[] toDate;
    public String[] fromTime;
    public String[] toTime;

    public Date currentDate;
    public Date currentTime;

    public String[] busStatus;

    public String taskStatus = "fail";
    public String regId;
    public String notifiedRoute;
    public String[] notifyRoute;

    public ImageView notifyBell;
    InputStream is = null;
    String statusCode = "fail";
    public PostRegIdAsyncTask postRegIdAsyncTask = new PostRegIdAsyncTask();
    String gcmResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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

        // Inflate the "decor.xml"
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.navdrawer_layout, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        FrameLayout container = (FrameLayout) drawer.findViewById(R.id.container); // This is the container we defined just now.
        container.addView(child);

        // Make the drawer replace the first child
        decor.addView(drawer);

        //initialize views in layout
        initializeViews();

        //delay for 300ms
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //click materialtextfield
                mtfClick();
            }
        }, 300);

        ActionBar bar = getSupportActionBar();

        //bar.setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));
        //bar().setElevation(25);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e94167")));
        bar.setElevation(0);

        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(R.layout.map_actionbar_layout);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#e94167"));
        }

        //set background of action bar
        //getSupportActionBar().setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.ab_gradient));

        //enable navigation icon button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setTitle("All Routes");
        //set app icon
        //getSupportActionBar().setIcon(R.drawable.ic_launcher_web);

        checkInternetConnection();

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
            LatLng prevPosition = null;
            int code = 0;

            @Override
            public void onLocationChanged(final Location location) {
                currentLocation = location;

                checkInternetConnection();

                //check if gps is enabled
                if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }

                //get coordinates
                lat = location.getLatitude();
                lng = location.getLongitude();
                time = location.getTime();

                //store coordinates into var LatLng
                LatLng newPosition = new LatLng(lat, lng);
                Marker marker;
                //centers and zooms camera to updated location
                //CameraUpdate center = CameraUpdateFactory.newLatLng(newPosition);

                //only allow camera to center to updated location when it changes
                if(map != null){
                    while (code != 2) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(newPosition)      // Sets the center of the map to Mountain View
                                .zoom(15)                   // Sets the zoom
                                //.bearing(90)                // Sets the orientation of the camera to east
                                //.tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        //map.moveCamera(center);
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 800, null);
                        prevPosition = newPosition;
                        code++;
                    }
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

    public void initializeMap(){
        //position of UTAR Sungai Long
        final LatLng UtarPos = new LatLng(3.0408043087568917,101.79446781107264);

        //initialize google map fragment
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);
        map.setTrafficEnabled(false);
        map.setIndoorEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        //set marker click listener
        map.setOnMarkerClickListener(this);
        //change marker info window layout
        /*map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            public View getInfoWindow(Marker arg0)
            {
                View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                return v;
            }

            public View getInfoContents(Marker arg0)
            {
                return null;
            }
        });*/

        //set myLocationButton position to bottom right of map fragment
        // Get the button view
        View mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getView();
        View btnMyLocation = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) btnMyLocation.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);

        //add marker to position of UTAR
        final Marker marker = map.addMarker(new MarkerOptions().position(UtarPos).title("UTAR").icon(BitmapDescriptorFactory.fromResource(R.mipmap.unimarker)));

        //set position of marker // originally is R.drawable.busicon4
        MarkerOptions mo = new MarkerOptions().position(UtarPos).icon(BitmapDescriptorFactory.fromResource(R.mipmap.greenmarker));
        for(int i = 0; i < noOfRouteInTable; i++){
            m[i] = map.addMarker(mo);
        }

        //initialize polyline
        line = new Polyline[mWayPointLocations.size()];

        //get markers for respective routes
        for(int i = 0; i < mWayPointLocations.size(); i++){
            //get number of stops
            String[] wayPointStops = mWayPointLocations.get(i);
            String[] wayPointStopNames = stopNamesUnconvertedList.get(i);
            if(wayPointStops != null){
                //initialize waypoint markers
                mWayPoint = new Marker[wayPointStops.length];
                for(int j = 0; j < wayPointStops.length; j++){
                    String[] coordinates = wayPointStops[j].split(",");
                    Double lat = Double.parseDouble(coordinates[0]);
                    Double lng = Double.parseDouble(coordinates[1]);
                    MarkerOptions wmo = new MarkerOptions().position(new LatLng(lat, lng)).title(wayPointStopNames[j]).icon(BitmapDescriptorFactory.fromResource(R.mipmap.stopmarker));
                    mWayPoint[j] = map.addMarker(wmo);
                }
                mWayPointList.add(mWayPoint);
            }
        }

        //get routes and plot onto map
        for(int i = 0; i < mWayPointLocations.size(); i++){
            //check if theres content if waypoint array
            if(waypoint[i].length() > 2){
                //draw route (polyline)
                new routeTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer(i));
            }
        }
    }

    private String getMapsApiDirectionsUrl(String waypoint) {
        /*String waypoints = "waypoints=optimize:true|"
                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
                + "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
                + BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
                + WALL_STREET.longitude;*/

        String sensor = "sensor=false";
        //String params = waypoints + "&" + sensor;
        String output = "json";
        String url = Constant.googleMapDirectionsURL + output + "?origin=" + UTAR.latitude + "," + UTAR.longitude + "&destination=" + UTAR.latitude + "," + UTAR.longitude + "&waypoints=" + waypoint + "&key=" + Constant.waypointAPIKey;
        return url;
    }

    private class routeTask extends AsyncTask<Integer, Void, List<List<HashMap<String, String>>>> {

        int number;

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(Integer... no) {
            String data = "";
            number = no[0].intValue();

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(getMapsApiDirectionsUrl(waypoint[number]));
                try {
                    jObject = new JSONObject(data);
                    PathJSONParser parser = new PathJSONParser();
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.parseColor("#e94167"));
            }

            //assigned to a var, so that it can be hidden or shown
            line[number] = map.addPolyline(polyLineOptions);
        }
    }
    //lck

    public void initializeNavDrawer(ArrayList<Integer> imageId, final int position, String[] routeNo, String[] routeName){
        //navigation drawer
        // get list items
        adapter1=new CustomListAdapter(this, routeNo, routeName, position-1, imageId);
        list=(ListView)findViewById(R.id.drawer);
        list.setAdapter(adapter1);

        // App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                System.out.println("Drawer close");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                System.out.println("Drawer open");
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //bake the navigation drawer menu icon
        actionBarDrawerToggle.syncState();

        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        //set navigation icon if needed
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        // just styling option add shadow the right edge of the drawer
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

    public void initializeViews(){
        //idView = (EditText) findViewById(R.id.id);
        route = (EditText) findViewById(R.id.route);
        bus = (EditText) findViewById(R.id.bus);
        distanceToUser = (EditText) findViewById(R.id.distanceToUser);
        distanceToUtar = (EditText) findViewById(R.id.distanceToUtar);
        etaToUser = (EditText) findViewById(R.id.etaToUser);
        status = (EditText) findViewById(R.id.status);
        //noOfPassengers = (EditText) findViewById(R.id.noOfPassengers);

        //mtfId = (MaterialTextField) findViewById(R.id.mtfId);
        mtfRoute = (MaterialTextField) findViewById(R.id.mtfRoute);
        mtfBus = (MaterialTextField) findViewById(R.id.mtfBus);
        mtfDistanceToUser = (MaterialTextField) findViewById(R.id.mtfDistanceToUser);
        mtfDistanceToUtar = (MaterialTextField) findViewById(R.id.mtfDistanceToUtar);
        mtfEtaToUser = (MaterialTextField) findViewById(R.id.mtfEtaToUser);
        mtfStatus = (MaterialTextField) findViewById(R.id.mtfStatus);
        //mtfNoOfPassengers = (MaterialTextField) findViewById(R.id.mtfNoOfPassengers);

        //bell
        notifyBell = (ImageView) findViewById(R.id.notifyBell);
    }

    public void mtfClick(){

        //click on the mtf
        //mtfId.performClick();
        mtfRoute.performClick();
        mtfBus.performClick();
        mtfDistanceToUser.performClick();
        mtfDistanceToUtar.performClick();
        mtfEtaToUser.performClick();
        mtfStatus.performClick();
        //mtfNoOfPassengers.performClick();

        //disable click
        mtfRoute.setClickable(false);
        mtfBus.setClickable(false);
        mtfDistanceToUser.setClickable(false);
        mtfDistanceToUtar.setClickable(false);
        mtfEtaToUser.setClickable(false);
        mtfStatus.setClickable(false);
        //mtfNoOfPassengers.setClickable(false);
    }

    //async tasks to obtain the last position of all registered bus in db
    private class busAsyncTask extends AsyncTask<Void, Void, String[]> {

        public String[] routeNumber;
        public boolean first;

        public busAsyncTask(String[] routeNumber, boolean first){
            this.routeNumber = routeNumber;
            this.first = first;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            if(!isCancelled()){
                //String[] routeNumber = params[0];

                System.out.println("No. of parameters passed into busAsyncTask is : " + routeNumber.length);

                for(int i = 0; i < routeNumber.length; i++) {
                    System.out.println("Obtaining last position of " + routeNumber[i]);
                    try {
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair("routeNo", String.valueOf(routeNumber[i])));
                        //String param = String.valueOf(number[i]);
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

                            // looping through All Products
                            for (int j = 0; j < data.length(); j++) {
                                JSONObject c = data.getJSONObject(j);

                                // Storing each json item in variable
                                asyncId[i] = c.getString("id");
                                asyncRouteNo[i] = c.getString("routeNo");
                                asyncRouteName[i] = c.getString("routeName");
                                asyncBus[i] = c.getString("bus");
                                asyncLat[i] = c.getString("lat");
                                asyncLng[i] = c.getString("lng");
                                //asyncPassengers[i] = c.getString("passengers");
                            }

                            utarLoc = new Location("utarCoordinates");
                            utarLoc.setLatitude(3.0408043087568917);
                            utarLoc.setLongitude(101.79446781107264);

                            busLoc[i] = new Location("busCoordinates");
                            busLoc[i].setLatitude(Double.parseDouble(asyncLat[i]));
                            busLoc[i].setLongitude(Double.parseDouble(asyncLng[i]));

                            //get ETA
                            if (busLoc[i].hasSpeed()) {
                                userETA[i] = (float) roundTwoDecimals(busLoc[i].distanceTo(currentLocation) / busLoc[i].getSpeed());
                                System.out.println("ETA : " + userETA[i] + " seconds");
                            } else {
                                System.out.println("ETA not available");
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "No products found", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //trying to combine another asynctask here
                try{
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("news", String.valueOf("1"))); //simply put a value for parameter purposes

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

                        //initialize size of arrays
                        cancelledRoute = new String[data.length()];
                        cancelledBus = new String[data.length()];
                        fromDate = new String[data.length()];
                        toDate = new String[data.length()];
                        fromTime = new String[data.length()];
                        toTime = new String[data.length()];
                        busStatus = new String[data.length()];

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

                // nav drawer notify bells
                try{
                    taskStatus = "fail";
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("regId", Constant.regId));

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

                        // looping through All Products
                        //for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(0);

                        // Storing each json item in variable
                        regId = c.getString("regId");
                        notifiedRoute = c.getString("notifyRouteNo");
                        Constant.notifiedRoute = c.getString("notifyRouteNo");
                        //}
                        taskStatus = "success";
                    }
                    else{
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    taskStatus = "fail";
                }
                return routeNumber;
            }else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] number) {

            /*//notify route bell image
            if(taskStatus == "success"){
                if(!regId.equals("")){
                    if(!notifiedRoute.equals("")){
                        notifyRoute = notifiedRoute.split("\\|");
                        for(int i = 0; i < itemRouteNo.length; i++){
                            //System.out.println("hereherehere " + notifiedRoute + " " + notifyRoute[i]);
                            for(int j = 0; j < notifyRoute.length; j++){
                                //System.out.println("hereherehere " + notifiedRoute + " " + notifyRoute[j] + " i = " + i + " j = " + j);
                                if(itemRouteNo[i].equals(notifyRoute[j]) && !itemRouteNo[i].equals("All Routes")){
                                    //imgid.add(R.drawable.notifyyellow);
                                    break;
                                }else if(j + 1 == notifyRoute.length){
                                    //imgid.add(R.drawable.notifygrey);
                                }
                            }
                        }
                        //System.out.println("hereherehere " + imgid.size());
                    }else{
                        for(int i = 0; i < itemRouteNo.length; i++){
                            //imgid.add(R.drawable.notifygrey);
                        }
                    }

                    //initialize navigation drawer
                    //initializeNavDrawer(imgid, 1);
                }
            }*/

            if(number != null){
                for(int i = 0; i < number.length; i++) {

                    try {
                        LatLng busPos = new LatLng(Double.parseDouble(asyncLat[i]), Double.parseDouble(asyncLng[i]));
                        //m[i].setPosition(busPos);
                        animateMarker(m[i], busPos);
                        m[i].setTitle("Route " + asyncRouteNo[i] + " : " + asyncRouteName[i]);
                        m[i].setSnippet(asyncBus[i]);

                        //set data to textviews
                        if(selectedRoute != 100) {
                            route.setText("Route " + asyncRouteNo[selectedRoute - 1] + " : " + asyncRouteName[selectedRoute - 1]);
                            bus.setText("Bus " + asyncBus[selectedRoute - 1]);
                            if(currentLocation == null || busLoc == null){
                                distanceToUser.setText("GPS Initializing");
                                etaToUser.setText("GPS Initializing");
                            }else{
                                distanceToUser.setText(String.valueOf(roundTwoDecimals(currentLocation.distanceTo(busLoc[selectedRoute - 1]) / 1000)) + " km");
                                if (busLoc[selectedRoute - 1].hasSpeed()) {
                                    etaToUser.setText(String.valueOf(userETA[selectedRoute - 1]) + " seconds");
                                }else{
                                    etaToUser.setText("Not available");
                                }
                            }
                            if(currentLocation == null || utarLoc == null){
                                distanceToUtar.setText("GPS Initializing");
                            }else{
                                distanceToUtar.setText(String.valueOf(roundTwoDecimals(currentLocation.distanceTo(utarLoc) / 1000)) + " km");
                            }
                            status.setText(busStatus[selectedRoute - 1]);
                            //noOfPassengers.setText(asyncPassengers[selectedRoute - 1]);
                            if (busLoc[selectedRoute - 1].hasSpeed()) {
                                etaToUser.setText(String.valueOf(userETA[selectedRoute - 1]) + " seconds");
                            }else{
                                etaToUser.setText("Not available");
                            }

                            //notify bell change colour
                            /*if(checkNotifiedRouteNo()){
                                notifyBell.setImageResource(R.mipmap.notifyyellow);
                            }else{
                                notifyBell.setImageResource(R.mipmap.notifygrey);
                            }*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try{
                        //check if route cancelled today

                        getDateTime();

                        for(int j = 0; j < cancelledRoute.length; j++) {
                            //if(cancelledRoute[j].equals("Route " + asyncRouteNo[i] + " : " + asyncRouteName[i]) && cancelledBus[j].equals("Bus " + asyncBus[i]) && (parseDate(fromDate[j]).equals(currentDate) || parseDate(toDate[j]).equals(currentDate) || (currentDate.after(parseDate(fromDate[j])) && currentDate.before(parseDate(toDate[j])))) && (currentTime.equals(parseTime(toTime[j])) || currentTime.equals(parseTime(fromTime[j])) || (currentTime.before(parseTime(toTime[j])) && currentTime.after(parseTime(fromTime[j]))))){
                            if (cancelledRoute[j].equals("Route " + asyncRouteNo[i] + " : " + asyncRouteName[i]) && cancelledBus[j].equals("Bus " + asyncBus[i])) {
                                if (parseDate(fromDate[j]).equals(currentDate) || parseDate(toDate[j]).equals(currentDate) || (currentDate.after(parseDate(fromDate[j])) && currentDate.before(parseDate(toDate[j])))) {
                                    if (!fromTime[j].equals("") && !toTime[j].equals("")) {
                                        if (currentTime.equals(parseTime(toTime[j])) || currentTime.equals(parseTime(fromTime[j])) || (currentTime.before(parseTime(toTime[j])) && currentTime.after(parseTime(fromTime[j])))) {
                                            m[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.redmarker));
                                            busStatus[i] = "Cancelled";
                                            break;
                                        } else {
                                            m[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.greenmarker));
                                            busStatus[i] = "Available";
                                        }
                                    }else{
                                        m[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.redmarker));
                                        busStatus[i] = "Cancelled";
                                        break;
                                    }
                                }else{
                                    m[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.greenmarker));
                                    busStatus[i] = "Available";
                                }
                            }else{
                                m[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.greenmarker));
                                busStatus[i] = "Available";
                            }
                        }
                    }catch(Exception e){

                    }
                }
                //lck
                //new busAsyncTask().execute(number);
                busAsyncTask = new busAsyncTask(number, false);
                busAsyncTask.execute();
                //lck
            }else{
                System.out.println("Cancelled");
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    //async task to download routes, bus, waypoints, news, and schedules from db
    private class DownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
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

                        //initialize listview array so that scrolling out of index will not crash
                        //also initialize size of other array var, so that its fully dynamic
                        itemRouteNo = new String[data.length()+1];
                        itemRouteName = new String[data.length()+1];
                        asyncId = new String[data.length()+1];
                        asyncRouteNo = new String[data.length()+1];
                        asyncRouteName = new String[data.length()+1];
                        asyncBus = new String[data.length()+1];
                        asyncLat = new String[data.length()+1];
                        asyncLng = new String[data.length()+1];
                        //asyncPassengers = new String[data.length()+1];
                        userETA = new float[data.length()+1];
                        m = new Marker[data.length()+1];
                        busLoc = new Location[data.length()+1];
                        waypoint = new String[data.length()+1];
                        stopNamesUnconverted = new String[data.length()+1];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            itemRouteNo[i+1] = c.getString("routeNo");
                            itemRouteName[i+1] = c.getString("routeName");
                            waypoint[i] = c.getString("waypoint");
                            stopNamesUnconverted[i] = c.getString("stopNames");

                            //store each waypoint marker locations
                            if(waypoint[i].contains(".") && waypoint[i].length() > 2) {
                                mWayPointLocations.add(waypoint[i].split("\\|"));
                                System.out.println("Length of mWayPointLocations : " + mWayPointLocations.get(i).length);
                            }
                            if(stopNamesUnconverted[i].length() > 2){
                                stopNamesUnconvertedList.add(stopNamesUnconverted[i].split("\\|"));
                                System.out.println("Length of stopNamesUnconvertedList : " + stopNamesUnconvertedList.get(i).length);
                            }
                        }

                        //store no. of routes found in table into a var
                        noOfRouteInTable = data.length();
                        System.out.println("No. of Routes in Table : " + String.valueOf(noOfRouteInTable));
                    }
                    else{
                        Toast.makeText(getBaseContext(), "No products found", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                try{
                    taskStatus = "fail";
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("regId", Constant.regId));

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

                        // looping through All Products
                        //for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(0);

                        // Storing each json item in variable
                        regId = c.getString("regId");
                        notifiedRoute = c.getString("notifyRouteNo");
                        Constant.notifiedRoute = c.getString("notifyRouteNo");
                        //}
                        taskStatus = "success";
                    }
                    else{
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    taskStatus = "fail";
                }

                return "Done";
            }else{
                return "Cancelled";
            }
        }

        protected void onPostExecute(String result) {
            if(result == "Done"){
                notifyBell.setImageResource(android.R.color.transparent);
                //if cannot get data
                if(itemRouteNo== null){
                    // Start a new thread that will download all the data
                   new DownloadTask().execute("");
                }else{

                    itemRouteNo[0] = "All Routes";

                    //notify route bell image
                    if(taskStatus == "success"){
                        if(!regId.equals("")){
                            if(!notifiedRoute.equals("")){
                                notifyRoute = notifiedRoute.split("\\|");
                                for(int i = 0; i < itemRouteNo.length; i++){
                                    //System.out.println("hereherehere " + notifiedRoute + " " + notifyRoute[i]);
                                    for(int j = 0; j < notifyRoute.length; j++){
                                        //System.out.println("hereherehere " + notifiedRoute + " " + notifyRoute[j] + " i = " + i + " j = " + j);
                                        if(itemRouteNo[i].equals(notifyRoute[j]) && !itemRouteNo[i].equals("All Routes")){
                                            //imgid.add(R.drawable.notifyyellow);
                                            break;
                                        }else if(j + 1 == notifyRoute.length){
                                            //imgid.add(R.drawable.notifygrey);
                                        }
                                    }
                                }
                                //System.out.println("hereherehere " + imgid.size());
                            }else{
                                for(int i = 0; i < itemRouteNo.length; i++){
                                    //imgid.add(R.drawable.notifygrey);
                                }
                            }
                        }
                    }

                    //initialize map
                    initializeMap();

                    //initialize navigation drawer
                    initializeNavDrawer(imgid, 1, itemRouteNo, itemRouteName);

                    //add header to nav drawer
                    View headerView = View.inflate(getBaseContext(), R.layout.drawer_header, null);
                    list.addHeaderView(headerView);

                    //set up header for nav drawer
                    ImageView iv = (ImageView) findViewById(R.id.header);
                    iv.setEnabled(false);

                    list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView txtTitle = (TextView) view.findViewById(R.id.et1);
                            TextView sub = (TextView) view.findViewById(R.id.subet);
                            //make textview scroll horizaontally
                            if (txtTitle.isSelected()) {
                                txtTitle.setSelected(false);
                            } else {
                                txtTitle.setSelected(true);
                            }
                            if (sub.isSelected()) {
                                sub.setSelected(false);
                            } else {
                                sub.setSelected(true);
                            }
                            return true;
                        }
                    });

                    //set item click listener to navigation drawer items
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // TODO Auto-generated method stub
                            //not using these icons at the moment
                            //change icon of listview item selected
                            /*Integer[] imageId = {
                                    R.drawable.allwhite,
                                    R.drawable.onewhite,
                                    R.drawable.twowhite,
                                    R.drawable.threewhite,
                                    R.drawable.fourwhite,
                                    R.drawable.fivewhite,
                                    R.drawable.sixwhite,
                                    R.drawable.sevenwhite
                            };*/
                            try {
                                String selecteditem = itemRouteNo[position - 1];

                                selectedRoute = position - 1;

                                //get no. of visible markers on map
                                markerVisibilityCount = 0;
                                for(int i = 0; i < noOfRouteInTable; i++){
                                    if(m[i] != null && m[i].isVisible()){
                                        markerVisibilityCount++;
                                    }
                                }

                                //hide all markers and routes
                                for(int i = 0; i < mWayPointList.size(); i++){
                                    Marker[] markers = mWayPointList.get(i);
                                    for(int j = 0; j < markers.length; j++){
                                        markers[j].setVisible(false);
                                    }
                                    if(line[i] != null){
                                        line[i].setVisible(false);
                                        System.out.println("line["+i+"] not null");
                                    }
                                }

                                getSupportActionBar().setTitle(selecteditem);
                                if (selecteditem == "All Routes") {
                                    //show all bus markers
                                    for (int i = 0; i < noOfRouteInTable; i++) {
                                        m[i].setVisible(true);
                                    }
                                    //show all stop markers and route paths
                                    for(int i = 0; i < mWayPointList.size(); i++){
                                        Marker[] markers = mWayPointList.get(i);
                                        for(int j = 0; j < markers.length; j++){
                                            markers[j].setVisible(true);
                                        }
                                        if(line[i] != null){
                                            line[i].setVisible(true);
                                            //System.out.println("line["+i+"] not null");
                                        }
                                    }
                                    /*for(int i = 0; i < line.length; i++){
                                        if(line[i] != null){
                                            line[i].setVisible(true);
                                        }
                                    }*/

                                    //centers and zooms camera to utar
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(UTAR)      // Sets the center of the map to Mountain View
                                            .zoom(15)                   // Sets the zoom
                                                    // .bearing(90)                // Sets the orientation of the camera to east
                                                    // .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                            .build();                   // Creates a CameraPosition from the builder
                                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 400, null);

                                    //set data to textviews
                                    selectedRoute = 100;
                                    //idView.setText("");
                                    route.setText("");
                                    bus.setText("");
                                    distanceToUser.setText("");
                                    distanceToUtar.setText("");
                                    etaToUser.setText("");
                                    status.setText("");
                                    //noOfPassengers.setText("");
                                    //imageId[position - 1] = R.drawable.all;

                                    notifyBell.setImageResource(android.R.color.transparent);

                                }else if (selecteditem != "All Routes") {
                                    //show and hide markers
                                    for (int i = 0; i < noOfRouteInTable; i++) {
                                        m[i].setVisible(false);
                                    }
                                    m[selectedRoute - 1].setVisible(true);
                                    if (asyncLat[selectedRoute - 1] != null || asyncLng[selectedRoute - 1] != null) {
                                        //centers and zooms camera to selected bus and route
                                        LatLng busPosition = new LatLng(Double.parseDouble(asyncLat[selectedRoute - 1]), Double.parseDouble(asyncLng[selectedRoute - 1]));

                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(busPosition)      // Sets the center of the map to Mountain View
                                                .zoom(15)                   // Sets the zoom
                                                // .bearing(90)                // Sets the orientation of the camera to east
                                                // .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                                .build();                   // Creates a CameraPosition from the builder
                                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 400, null);

                                        //set data to textviews
                                        if(selectedRoute != 100) {
                                            route.setText("Route " + asyncRouteNo[selectedRoute - 1] + " : " + asyncRouteName[selectedRoute - 1]);
                                            bus.setText("Bus " + asyncBus[selectedRoute - 1]);
                                            if(currentLocation == null || busLoc == null){
                                                distanceToUser.setText("GPS Initializing");
                                                etaToUser.setText("GPS Initializing");
                                            }else{
                                                distanceToUser.setText(String.valueOf(roundTwoDecimals(currentLocation.distanceTo(busLoc[selectedRoute - 1]) / 1000)) + " km");
                                                if (busLoc[selectedRoute - 1].hasSpeed()) {
                                                    etaToUser.setText(String.valueOf(userETA[selectedRoute - 1]) + " seconds");
                                                }else{
                                                    etaToUser.setText("Not available");
                                                }
                                            }
                                            if(currentLocation == null || utarLoc == null){
                                                distanceToUtar.setText("GPS Initializing");
                                            }else{
                                                distanceToUtar.setText(String.valueOf(roundTwoDecimals(currentLocation.distanceTo(utarLoc) / 1000)) + " km");
                                            }
                                            //noOfPassengers.setText(asyncPassengers[selectedRoute-1]);
                                            if (busLoc[selectedRoute - 1].hasSpeed()) {
                                                etaToUser.setText(String.valueOf(userETA[selectedRoute - 1]) + " seconds");
                                            }else{
                                                etaToUser.setText("Not available");
                                            }

                                            //notify bell change colour
                                            if(checkNotifiedRouteNo()){
                                                notifyBell.setImageResource(R.mipmap.notifyyellow);
                                            }else{
                                                notifyBell.setImageResource(R.mipmap.notifygrey);
                                            }
                                        }
                                    }
                                    //show relevant waypoint markers and route
                                    if(mWayPointList.size() >= selectedRoute){
                                        Marker[] markers = mWayPointList.get(selectedRoute - 1);
                                        for(int i = 0; i < markers.length; i++){
                                            markers[i].setVisible(true);
                                        }
                                        line[selectedRoute - 1].setVisible(true);
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {

                            }
                            drawerLayout.closeDrawer(list);
                            initializeNavDrawer(imgid, position, itemRouteNo, itemRouteName);
                        }
                    });

                    //prepare to call busAsyncTask
                    String[] asyncParam = new String[noOfRouteInTable];
                    for(int i = 0; i < noOfRouteInTable; i++){
                        asyncParam[i] = itemRouteNo[i+1];
                    }

                    //calling 2 asynctask in parallel
                    busAsyncTask = new busAsyncTask(asyncParam, true);
                    busAsyncTask.execute();

                    //busAsyncTask.execute();
                    //crdt.execute();

                    if (MapActivity.this.pd != null) {
                        MapActivity.this.pd.dismiss();
                    }
                }
            }
        }
    }

    //send regId to server
    private class PostRegIdAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(!isCancelled()) {
                statusCode = "fail";
                //var for inserting data into db
                List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
                nvp.add(new BasicNameValuePair("regId", Constant.regId));
                nvp.add(new BasicNameValuePair("notifyRouteNo", gcmResult));
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
                    statusCode = "success";
                } catch (Exception e) {
                    e.printStackTrace();
                    statusCode = "fail";
                }
                return "Done";
            }else{
                return "Cancelled";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == "Done"){
                if(statusCode == "success"){
                    /*MapActivity m = new MapActivity();
                    m.initializeNavDrawer(imgid, pos);*/
                    /*getNotifiedRouteAsyncTask = new GetNotifiedRouteAsyncTask();
                    getNotifiedRouteAsyncTask.execute();*/
                }else{
                    postRegIdAsyncTask = new PostRegIdAsyncTask();
                    postRegIdAsyncTask.execute();
                }
            }
        }
    }

    //async task to download cancelled routes from db ------ joined with busAsyncTask
    /*private class CancelledRouteDownloadTask extends AsyncTask<String, Void, String> {
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
                crdt = new CancelledRouteDownloadTask();
                crdt.execute("");
            }
        }
    }*/

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

    public void animateMarker(final Marker marker, final LatLng toPosition) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    /*if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }*/
                }
            }
        });
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
        if(!((Activity) MapActivity.this).isFinishing())
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
        if(!((Activity) MapActivity.this).isFinishing())
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

    public double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(id == R.id.schedule){
            Intent i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
            cancelAsyncTask();
        }
        if(id == R.id.news){
            Intent i = new Intent(this, NewsActivity.class);
            startActivity(i);
            cancelAsyncTask();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        boolean bool = false;
        int j = 0;
        String code = "fail";
        String text = marker.getTitle();
        if(text.contains("Route ")){
            if(asyncRouteNo != null){
                int firstSpaceIndex = text.indexOf(" ");
                int secondSpaceIndex = text.indexOf(" ", firstSpaceIndex + 1);
                String routeNo = text.substring(firstSpaceIndex + 1, secondSpaceIndex);
                for(int i = 0; i < asyncRouteNo.length; i++){
                    if(asyncRouteNo[i] != null){
                        if(asyncRouteNo[i].equals(routeNo)){
                            j = i;
                        }
                    }
                }
                list.performItemClick(list.getChildAt(j + 2), j + 2, j + 2);

                //do not show all marker if clicked through nav bar first, else show all
                if(markerVisibilityCount > 1){
                    for(int i = 0; i < noOfRouteInTable; i++) {
                        m[i].setVisible(true);
                    }
                }
                for(int i = 0; i < line.length; i++){
                    if(line[i] != null && j != i){
                        line[i].setVisible(false);
                    }else if(line[i] != null && j == i){
                        line[j].setVisible(true);
                    }
                }
                code = "success";
                if(activeBusMarker != j){
                    activeMarkerClickCount = 0;
                }
                activeMarkerClickCount++;
            }
            //trying to just disable infowindow from showing.
            bool = true;
        }

        //if clicked 2 times on the same marker, hide the info window.
        /*if(code == "success"){
            if(activeBusMarker != 100 && j == activeBusMarker && activeMarkerClickCount == 2){
                bool = true;
                activeMarkerClickCount = 0;
                System.out.println("markerClickTest activeBusMarker = " + activeBusMarker + " j = " + j + " activeMarkerClickCount = " + activeMarkerClickCount);
            }
            System.out.println("markerClickTest out activeBusMarker = " + activeBusMarker + " j = " + j + " activeMarkerClickCount = " + activeMarkerClickCount);
        }else{
            bool = false;
        }*/

        activeBusMarker = j;

        return bool;
    }

    public boolean checkNotifiedRouteNo(){
        boolean result = false;
        if(notifiedRoute.contains("|")){
            String[] tempNotifyRoute = notifiedRoute.split("\\|");
            for(int i = 0; i < tempNotifyRoute.length; i++){
                try{
                    if(tempNotifyRoute[i].equals(asyncRouteNo[selectedRoute - 1])){
                        result = true;
                        break;
                    }else if(i + 1 == tempNotifyRoute.length){
                        result = false;
                    }
                }catch(Exception e){

                }
            }
        }else if(notifiedRoute.equals(asyncRouteNo[selectedRoute - 1])){
            result = true;
        }

        return result;
    }

    //
    public String changeNotifiedRouteNo(String result){
        if(result.equals("")){
            result = asyncRouteNo[selectedRoute - 1];
            notifyBell.setImageResource(R.mipmap.notifyyellow);
        }else{
            if(result.contains("|")){
                String[] tempNotifyRoute = result.split("\\|");
                List<String> notifyRouteArrayList = new ArrayList<String>(Arrays.asList(tempNotifyRoute));
                result = "";
                for(int i = 0; i < tempNotifyRoute.length; i++){
                    if(tempNotifyRoute[i].equals(asyncRouteNo[selectedRoute - 1])){
                        notifyRouteArrayList.remove(i);
                        notifyBell.setImageResource(R.mipmap.notifygrey);
                        break;
                    }else if(i + 1 == tempNotifyRoute.length){
                        notifyRouteArrayList.add(asyncRouteNo[selectedRoute - 1]);
                        notifyBell.setImageResource(R.mipmap.notifyyellow);
                    }
                }
                for(int i = 0; i < notifyRouteArrayList.size(); i++){
                    if(result.equals("")){
                        result += notifyRouteArrayList.get(i);
                    }else{
                        result += "|" + notifyRouteArrayList.get(i);
                    }
                }
            }else{
                if(result.equals(asyncRouteNo[selectedRoute - 1])){
                    result = "";
                    notifyBell.setImageResource(R.mipmap.notifygrey);
                }else{
                    result += "|" + asyncRouteNo[selectedRoute - 1];
                    notifyBell.setImageResource(R.mipmap.notifyyellow);
                }
            }
        }

        System.out.println("abcabc size" + result);

        return result;
    }

    //notify button method
    public void notifyRoute(View v){

        gcmResult = changeNotifiedRouteNo(notifiedRoute);

        postRegIdAsyncTask = new PostRegIdAsyncTask();
        postRegIdAsyncTask.execute();
    }

    @Override
    public void onBackPressed() {
        cancelAsyncTask();
    }

    public void cancelAsyncTask(){
        if (busAsyncTask != null) {
            busAsyncTask.cancel(true);
        }
        finish();
    }
}
