package com.example.dynstu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterClass extends BaseAdapter {

    Context context;
    ArrayList<objSchedule> arrayList;

    public AdapterClass(Context context,ArrayList<objSchedule> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_search_class,null);
        TextView date = (TextView)convertView.findViewById(R.id.class_date);
        TextView type = (TextView)convertView.findViewById(R.id.class_type);
        TextView room = (TextView)convertView.findViewById(R.id.class_room);
        TextView time = (TextView)convertView.findViewById(R.id.class_time);
        TextView price = (TextView)convertView.findViewById(R.id.class_price);
        TextView full = (TextView)convertView.findViewById(R.id.class_full);

        objSchedule objschedule = arrayList.get(position);

        date.setText(String.valueOf(objschedule.getDate()));
        type.setText(String.valueOf(objschedule.getType()));
        room.setText(String.valueOf(objschedule.getRoom()));
        time.setText((String.valueOf(objschedule.getStart()))+"-"+String.valueOf(objschedule.getEnd()));
        price.setText("R "+(String.valueOf(objschedule.getPrice())));
        full.setText(objschedule.getFull());

        return convertView;
    }
}
