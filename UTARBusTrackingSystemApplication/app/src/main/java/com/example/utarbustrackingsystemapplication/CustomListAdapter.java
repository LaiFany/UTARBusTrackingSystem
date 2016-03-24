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
import java.util.Arrays;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemRouteNo;
    private final String[] itemRouteName;
    private final int pos;
    private ArrayList<Integer> imgid;

    LayoutInflater inflater;

    public CustomListAdapter(Activity context, String[] itemRouteNo, String[] itemRouteName, int pos, ArrayList<Integer> imgid) {
        super(context, R.layout.drawer_listview_item, itemRouteNo);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemRouteNo=itemRouteNo;
        this.itemRouteName=itemRouteName;
        this.pos=pos;
        this.imgid=imgid;
    }

    public View getView(final int position,View view,ViewGroup parent) {
        inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.drawer_listview_item, null, true);

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.et1);
        final TextView sub = (TextView) rowView.findViewById(R.id.subet);
        //imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(itemRouteNo[position] == "All Routes"){
            txtTitle.setText(itemRouteNo[position]);
        }else{
            txtTitle.setText("Route " + itemRouteNo[position]);
        }
        sub.setText(itemRouteName[position]);

        if(position == pos){
            txtTitle.setTextColor(Color.WHITE);
            sub.setTextColor(Color.WHITE);
            rowView.setBackgroundColor(Color.parseColor("#660099"));
            //rowView.setBackgroundResource(R.drawable.white_navbar_bg_gradient);
        }
        return rowView;
    }
}
