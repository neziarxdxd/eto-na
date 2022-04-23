package com.example.pisocharge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AmountAdaptor extends BaseAdapter {

        Context context;
        ArrayList<AmountTally> arrayList;

        public AmountAdaptor(Context context, ArrayList<AmountTally> arrayList) {
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
        public  View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_amounttally, parent, false);
            }
            TextView station, amountTally, date;
            station = (TextView) convertView.findViewById(R.id.textViewStation);
            amountTally = (TextView) convertView.findViewById(R.id.textViewAmountTally);
            date = (TextView) convertView.findViewById(R.id.textViewDate);
            station.setText(arrayList.get(position).getUnit());
            amountTally.setText(arrayList.get(position).getAmountTally());
            date.setText(arrayList.get(position).getDate());

            return convertView;
        }
}