package com.example.pisocharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PerUnitAdaptor  extends BaseAdapter {

    Context context;
    ArrayList<PerUnit> arrayList;

    public PerUnitAdaptor(Context context, ArrayList<PerUnit> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_perunit, parent, false);
        }
        TextView unitdate, unittime, unitmins;
        unitdate = (TextView) convertView.findViewById(R.id.textViewUnitdate);
        unittime = (TextView) convertView.findViewById(R.id.textViewUnittime);
        unitmins = (TextView) convertView.findViewById(R.id.textViewUnitmins);
        unitdate.setText(arrayList.get(position).getUnitdate());
        unittime.setText(arrayList.get(position).getUnittime());
        unitmins.setText(arrayList.get(position).getUnitmins());

        return convertView;
    }
}
