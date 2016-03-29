package com.example.utarbustrackingsystemapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends Activity {

    public AlertDialog alert;
    public PostRegIdAsyncTask postRegIdAsyncTask = new PostRegIdAsyncTask();
    private ProgressDialog pd;
    InputStream is = null;
    String regId = "aaa";
    String status = "fail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        checkInternet();

        postRegIdAsyncTask.execute();
    }

    public void getRegId(){
        GCMClientManager pushClientManager = new GCMClientManager(this, Constant.PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                regId = registrationId;
                Constant.regId = regId;
                Log.d("Registration id", regId);
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    //send regId to server
    private class PostRegIdAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(!isCancelled()) {
                //var for inserting data into db
                List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
                nvp.add(new BasicNameValuePair("regId", regId));
                //nvp.add(new BasicNameValuePair("notifyRouteNo", "1|2|3|4|5"));
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
                    status = "success";
                } catch (Exception e) {
                    e.printStackTrace();
                    status = "fail";
                }
                return "Done";
            }else{
                return "Cancelled";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == "Done"){
                if(status == "success"){
                    Intent i = new Intent(SplashScreenActivity.this, MapActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    postRegIdAsyncTask = new PostRegIdAsyncTask();
                    postRegIdAsyncTask.execute();
                }
            }
        }
    }

    public void checkInternet(){
        if(isConnected()){
            getRegId();
        }
        else{
            System.out.println("Not connected to internet");
            buildAlertMessageNoInternet();
            checkInternet();
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
        if(!((Activity) SplashScreenActivity.this).isFinishing())
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

    @Override
    public void onBackPressed() {
        cancelAsyncTask();
    }

    public void cancelAsyncTask(){
        if (postRegIdAsyncTask != null) {
            postRegIdAsyncTask.cancel(true);
        }
        finish();
    }
}
