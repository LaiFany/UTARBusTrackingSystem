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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;


public class LoginActivity extends AppCompatActivity {

    JSONParser jParser = new JSONParser();
    private ProgressDialog pd = null;
    public AlertDialog alert;
    LoginTask lt;

    TextView errorTV;
    EditText usernameET;
    EditText passwordET;
    MaterialTextField mtfUsername;
    MaterialTextField mtfPassword;

    String[] username;
    String[] password;
    String[] privilege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set background of action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e94167")));
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        // enable status bar tint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#e94167"));
        }

        //delay for 300ms
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                initializeViews();
                getLoginSP();
            }
        }, 300);
    }

    public void initializeViews(){
        errorTV = (TextView) findViewById(R.id.error);
        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        mtfUsername = (MaterialTextField) findViewById(R.id.mtfUsername);
        mtfPassword = (MaterialTextField) findViewById(R.id.mtfPassword);


        mtfUsername.performClick();
        mtfPassword.performClick();
        mtfUsername.setClickable(false);
        mtfPassword.setClickable(false);
    }

    public void getLoginSP(){
        String usernameSP = "";
        String passwordSP = "";
        //get shared preferences
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(sp.contains("username") && sp.contains("password")) {
            usernameSP = sp.getString("username", "");
            passwordSP = sp.getString("password", "");
            usernameET.setText(usernameSP);
            passwordET.setText(passwordSP);
        }
    }

    public void setLoginSP(String u, String p){
        //set shared preferences for number of times logged into the app
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putString("username", u);
        e.putString("password", p);
        e.commit();
    }

    public void login(View v){
        if(usernameET.getText().toString().equals("") || passwordET.getText().toString().equals("")){
            errorTV.setText("Please enter Username and Password");
        }else{
            errorTV.setText("");
            lt = new LoginTask();
            lt.execute("");
            // Show the ProgressDialog on this thread
            this.pd = ProgressDialog.show(this, "", "Logging In", true, true,
                    new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            lt.cancel(true);
                            finish();
                        }
                    });
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
                    checkInternetConnection();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("route", String.valueOf("1"))); //simply put a value for parameter purposes

                    // getting JSON string from URL
                    JSONObject json = jParser.makeHttpRequest(Constant.retrieveUserURL, "GET", param);

                    // Check your log cat for JSON reponse
                    // Checking for SUCCESS TAG
                    int success = json.getInt("success");

                    if (success == 1) {
                        JSONArray data = json.getJSONArray("data");

                        username = new String[data.length()];
                        password = new String[data.length()];
                        privilege = new String[data.length()];

                        // looping through All Products
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);

                            // Storing each json item in variable
                            username[i] = c.getString("username");
                            password[i] = c.getString("password");
                            privilege[i] = c.getString("privilege");
                        }
                    }
                    else{
                        System.out.println("Table empty");
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
                if(username == null || password == null || privilege == null){
                    // Start a new thread that will download all the data
                    lt = new LoginTask();
                    lt.execute("");
                }else{
                    if(validCredential()){
                        setLoginSP(usernameET.getText().toString(), passwordET.getText().toString());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        errorTV.setText("Invalid Username or Password");
                    }
                    if (LoginActivity.this.pd != null) {
                        LoginActivity.this.pd.dismiss();
                    }
                }
            }
        }
    }

    public boolean validCredential(){
        int length = 0;
        int[] temp = new int[privilege.length];
        int[] j;
        for(int i = 0; i < privilege.length; i++){
            if(privilege[i].equals("admin")){
                temp[length] = i;
                length++;
            }
        }

        j = new int[length];

        for(int i = 0; i < length; i++){
            j[i] = temp[i];
        }

        boolean valid = false;

        //compare credentials
        for(int i = 0; i < j.length; i++){
            if(usernameET.getText().toString().equals(username[j[i]])){
                if(passwordET.getText().toString().equals(password[j[i]])){
                    valid = true;
                    break;
                }
            }else if(i + 1 == j.length){
                valid = false;
            }
        }

        return valid;
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
        if(!((Activity) LoginActivity.this).isFinishing())
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
        if(!((Activity) LoginActivity.this).isFinishing())
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
}

