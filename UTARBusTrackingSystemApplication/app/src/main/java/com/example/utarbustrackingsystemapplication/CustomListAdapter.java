package com.example.utarbustrackingsystemapplication;

/**
 * Created by 0126190799 on 1/17/2016.
 */
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemRouteNo;
    private final String[] itemRouteName;
    private final int pos;

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

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.et1);
        final TextView sub = (TextView) rowView.findViewById(R.id.subet);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if(itemRouteNo[position] == "All Routes"){
            txtTitle.setText(itemRouteNo[position]);
        }else{
            txtTitle.setText("Route " + itemRouteNo[position]);
        }

        sub.setText(itemRouteName[position]);
        //imageView.setImageResource(imgid[position]);
        if(position == pos){
            txtTitle.setTextColor(Color.WHITE);
            sub.setTextColor(Color.WHITE);
            rowView.setBackgroundColor(Color.parseColor("#e94167"));
            //rowView.setBackgroundResource(R.drawable.white_navbar_bg_gradient);
        }

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                return false;
            }
        });
        return rowView;
    };
}
