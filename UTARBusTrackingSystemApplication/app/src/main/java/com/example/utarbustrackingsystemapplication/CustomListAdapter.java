package com.example.utarbustrackingsystemapplication;

/**
 * Created by 0126190799 on 1/17/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class CustomListAdapter extends ArrayAdapter<String> {

    JSONParser jParser = new JSONParser();
    InputStream is = null;
    String status = "fail";
    public PostRegIdAsyncTask postRegIdAsyncTask = new PostRegIdAsyncTask();
    public GetNotifiedRouteAsyncTask getNotifiedRouteAsyncTask;
    public String regId;
    public String notifiedRoute;
    public String[] notifyRoute;

    private final Activity context;
    private final String[] itemRouteNo;
    private final String[] itemRouteName;
    private final int pos;

    public ImageView imageView;

    public int tempPosition;

    public CustomListAdapter(Activity context, String[] itemRouteNo, String[] itemRouteName, int pos) {
        super(context, R.layout.drawer_listview_item, itemRouteNo);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemRouteNo=itemRouteNo;
        this.itemRouteName=itemRouteName;
        this.pos=pos;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.drawer_listview_item, null, true);

        tempPosition = position;

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.et1);
        final TextView sub = (TextView) rowView.findViewById(R.id.subet);
        imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(itemRouteNo[position] == "All Routes"){
            txtTitle.setText(itemRouteNo[position]);
        }else{
            txtTitle.setText("Route " + itemRouteNo[position]);
            //imageView.setImageResource(R.drawable.notifygrey);
        }

        sub.setText(itemRouteName[position]);
        if(position == pos){
            txtTitle.setTextColor(Color.WHITE);
            sub.setTextColor(Color.WHITE);
            rowView.setBackgroundColor(Color.parseColor("#e94167"));
            //rowView.setBackgroundResource(R.drawable.white_navbar_bg_gradient);
        }

        //execute get asynctask
        getNotifiedRouteAsyncTask = new GetNotifiedRouteAsyncTask();
        getNotifiedRouteAsyncTask.execute();

        //try to set click listener for notify imagebutton
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hereherehere");
                notifiedRoute = notifiedRoute + "|" + itemRouteNo[tempPosition];
                postRegIdAsyncTask.execute();
            }
        });
        return rowView;
    }

    //get regId to server
    private class GetNotifiedRouteAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(!isCancelled()){
                Log.i("MyApp", "Background thread starting");

                try{
                    status = "fail";
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

                        //initialize size of arrays
                        /*regId = new String[data.length()];
                        notifiedRoute = new String[data.length()];*/

                        // looping through All Products
                        //for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(0);

                            // Storing each json item in variable
                            regId = c.getString("regId");
                            notifiedRoute = c.getString("notifyRouteNo");
                        //}
                        status = "success";
                    }
                    else{
                    }
                }catch(Exception e){
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
                    if(!regId.equals("")){
                        if(!notifiedRoute.equals("")){
                            notifyRoute = notifiedRoute.split("\\|");
                            for(int i = 0; i < notifyRoute.length; i++){
                                System.out.println("here" + " " +notifyRoute[i]);
                                if(itemRouteNo[tempPosition].equals(notifyRoute[i])){
                                    imageView.setImageResource(R.drawable.notifyyellow);
                                    break;
                                }else{
                                    imageView.setImageResource(R.drawable.notifygrey);
                                }
                            }
                        }
                    }
                }else{
                    getNotifiedRouteAsyncTask = new GetNotifiedRouteAsyncTask();
                    getNotifiedRouteAsyncTask.execute();
                }
            }
        }
    }

    //send regId to server
    private class PostRegIdAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(!isCancelled()) {
                status = "fail";
                //var for inserting data into db
                List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
                nvp.add(new BasicNameValuePair("regId", Constant.regId));
                nvp.add(new BasicNameValuePair("notifyRouteNo", notifiedRoute));
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

                }else{
                    postRegIdAsyncTask = new PostRegIdAsyncTask();
                    postRegIdAsyncTask.execute();
                }
            }
        }
    }
}
