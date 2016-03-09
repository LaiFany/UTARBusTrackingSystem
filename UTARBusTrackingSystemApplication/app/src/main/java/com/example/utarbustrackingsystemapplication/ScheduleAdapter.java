package com.example.utarbustrackingsystemapplication;

/**
 * Created by LaiFany on 2/16/2016.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ScheduleAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] routeText;
    private final String[] busNo;
    private final String[] date;

    public ScheduleAdapter(Activity context, String[] routeText, String[] busNo, String[] date) {
        super(context, R.layout.schedule_items, routeText);

        this.context = context;
        this.routeText = routeText;
        this.busNo = busNo;
        this.date = date;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.schedule_items, parent, false);

        //TextView idTv = (TextView) rowView.findViewById(R.id.no);
        final TextView route = (TextView) rowView.findViewById(R.id.route);
        TextView bus = (TextView) rowView.findViewById(R.id.bus);
        TextView dateTv = (TextView) rowView.findViewById(R.id.date);

        route.setText(routeText[position]);
        bus.setText(busNo[position]);
        dateTv.setText(date[position]);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //make textview scroll horizaontally
                if (route.isSelected()) {
                    route.setSelected(false);
                } else {
                    route.setSelected(true);
                }
                return false;
            }
        });

        return rowView;
    }
}

