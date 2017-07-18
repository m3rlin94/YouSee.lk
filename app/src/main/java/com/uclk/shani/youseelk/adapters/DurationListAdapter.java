package com.uclk.shani.youseelk.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uclk.shani.youseelk.R;

/**
 * Created by Shani on 20/06/2017.
 */

public class DurationListAdapter extends BaseAdapter {

    private String[] arrayLocs;
    private String[] arrayTemp;
    private Context context;

    public DurationListAdapter(String[] arrayLocs, String[] arrayTemp, Context context) {
        super();
        this.arrayLocs = arrayLocs;
        this.arrayTemp = arrayTemp;
        this.context = context;
    }

    @Override
    public int getCount(){
        if (arrayLocs != null && arrayLocs.length !=0){
            return arrayLocs.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position){
        return arrayLocs[position];

    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.duration_row,parent,false);

            holder.tv = (TextView)convertView.findViewById(R.id.tvAddedLocation);
            holder.et = (EditText) convertView.findViewById(R.id.etDuration);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.ref = position;

        holder.tv.setText(arrayLocs[position]);
        holder.et.setText(arrayTemp[position]);

        holder.et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                arrayTemp[holder.ref] = s.toString();
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tv;
        EditText et;
        int ref;
    }
}
