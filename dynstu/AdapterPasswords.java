package com.example.dynstu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPasswords extends BaseAdapter {

    Context context;
    ArrayList<objPasswords> arrayList;

    public AdapterPasswords(Context context,ArrayList<objPasswords> arrayList){
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
        convertView = inflater.inflate(R.layout.list_forgotten_passwords,null);
        TextView email = (TextView)convertView.findViewById(R.id.forgot_pass_email);
        TextView name = (TextView)convertView.findViewById(R.id.forgot_pass_name);

        objPasswords objpasswords = arrayList.get(position);

        email.setText(String.valueOf(objpasswords.getEmail()));
        name.setText(String.valueOf(objpasswords.getFirstName()) + " " + objpasswords.getLastName());

        return convertView;
    }



}
