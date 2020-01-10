package com.example.dynstu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPrivate extends BaseAdapter {

    Context context;
    ArrayList<objSchedule> arrayList;

    public AdapterPrivate(Context context,ArrayList<objSchedule> arrayList){
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
        convertView = inflater.inflate(R.layout.list_private_request,null);
        TextView code = (TextView)convertView.findViewById(R.id.priv_code);
        TextView date = (TextView)convertView.findViewById(R.id.priv_date);
        TextView time = (TextView)convertView.findViewById(R.id.priv_time);
        TextView cap = (TextView)convertView.findViewById(R.id.priv_cap);

        objSchedule objschedule = arrayList.get(position);

        date.setText(String.valueOf(objschedule.getDate()));
        time.setText((String.valueOf(objschedule.getStart())) + "-" + String.valueOf(objschedule.getEnd()));
        code.setText(String.valueOf(objschedule.getCode()));
        cap.setText(String.valueOf(objschedule.getCap()) + " seats");

        return convertView;
    }
}
