package com.example.pisocharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TallyReportAdaptor extends BaseAdapter {

    Context context;
    ArrayList<TallyReport> arrayList;

    public TallyReportAdaptor(Context context, ArrayList<TallyReport> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_tallyreport, parent, false);
        }
        TextView date, timeTally, amountTally;
        date = (TextView) convertView.findViewById(R.id.textViewReportdate);
        timeTally = (TextView) convertView.findViewById(R.id.textViewReporttime);
        amountTally = (TextView) convertView.findViewById(R.id.textViewReportamount);
        date.setText(arrayList.get(position).getReportdate());
        timeTally.setText(arrayList.get(position).getReporttime());
        amountTally.setText(arrayList.get(position).getReportamount());

        return convertView;
    }
}
