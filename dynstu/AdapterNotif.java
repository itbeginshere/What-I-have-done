package com.example.dynstu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterNotif extends BaseAdapter{

    Context context;
    ArrayList<objNotif>  arrayList;

    public AdapterNotif(Context context,ArrayList<objNotif> arrayList){
        this.context=context;
        this.arrayList = arrayList;
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
        convertView = inflater.inflate(R.layout.list_notifications,null);
        TextView code = (TextView)convertView.findViewById(R.id.notif_code);
        TextView message = (TextView)convertView.findViewById(R.id.notif_short_msg);

        objNotif objnotif = arrayList.get(position);

        code.setText(String.valueOf(objnotif.getCode()));
        message.setText(String.valueOf(objnotif.getMessage()));

        return convertView;
    }


}
