package com.uclk.shani.youseelk.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uclk.shani.youseelk.activities.SavedPlanView;
import com.uclk.shani.youseelk.R;


/**
 * Created by Shani on 14/06/2017.
 */

public class SavedPlansAdapter extends BaseAdapter
{

    private String[] planNames;
    private String[] savedTimes;
    private Context context;

    public SavedPlansAdapter(String[] planNames, String[] savedTimes, Context context) {
        super();
        this.planNames = planNames;
        this.savedTimes = savedTimes;
        this.context = context;
    }

    @Override
    public int getCount(){
        return planNames.length;
    }

    @Override
    public Object getItem(int position){
        return planNames[position];
    }



    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        View rowView = LayoutInflater.from(context).inflate(R.layout.saved_plan_row,parent,false);

        TextView name = (TextView) rowView.findViewById(R.id.planName);
        TextView stime = (TextView) rowView.findViewById(R.id.savedTime);

        name.setText(planNames[position]);
        stime.setText(savedTimes[position]);

        //doesn't work
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SavedPlanView.class);
                v.getContext().startActivity(intent);
                //send the plan name and datetime
            }
        });

        return rowView;
    }




}