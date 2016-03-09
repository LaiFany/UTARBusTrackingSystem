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
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final int[] id;
    private final String[] newsTitle;
    private final String[] newsDesc;
    private final String[] date;
    private final int[] unreadId;

    public NewsAdapter(Activity context, int[] id, String[] newsTitle, String[] newsDesc, String[] date, int[] unreadId) {

        super(context, R.layout.news_items, newsTitle);

        this.context = context;
        this.id = id;
        this.newsTitle = newsTitle;
        this.newsDesc = newsDesc;
        this.date = date;
        this.unreadId = unreadId;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.news_items, parent, false);

        //TextView idTv = (TextView) rowView.findViewById(R.id.no);
        final TextView title = (TextView) rowView.findViewById(R.id.title);
        final TextView desc = (TextView) rowView.findViewById(R.id.desc);
        TextView dateTv = (TextView) rowView.findViewById(R.id.date);

        //idTv.setText(String.valueOf(id[position]));
        title.setText(newsTitle[getCount() - position - 1]);
        desc.setText(newsDesc[getCount() - position - 1]);
        dateTv.setText(date[getCount() - position - 1]);

        int pos = id.length - position - 1;

        if(unreadId.length > 0){
            for(int i = 0; i <unreadId.length; i++){
                if(id[pos] == unreadId[i]){
                    title.setTypeface(null, Typeface.BOLD_ITALIC);
                    break;
                }else if(i + 1 == unreadId.length){
                    title.setTypeface(null, Typeface.NORMAL);
                }
            }
        }

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                return false;
            }
        });

        return rowView;
    }
}

