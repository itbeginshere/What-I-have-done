package com.example.dynstu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSchedule extends BaseAdapter {

    Context context;
    ArrayList<objSchedule>  arrayList;

    public AdapterSchedule(Context context,ArrayList<objSchedule> arrayList){
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
        convertView = inflater.inflate(R.layout.list_admin_schedule,null);
        TextView date = (TextView)convertView.findViewById(R.id.admin_schedule_date);
        TextView type = (TextView)convertView.findViewById(R.id.admin_schedule_type);
        TextView room = (TextView)convertView.findViewById(R.id.admin_schedule_room);
        TextView time = (TextView)convertView.findViewById(R.id.admin_schedule_time);
        objSchedule objschedule = arrayList.get(position);

        date.setText(String.valueOf(objschedule.getDate()));
        type.setText(String.valueOf(objschedule.getType()));
        room.setText(String.valueOf(objschedule.getRoom()));
        time.setText((String.valueOf(objschedule.getStart()))+"-"+String.valueOf(objschedule.getEnd()));

        return convertView;
    }


}

