package com.uclk.shani.youseelk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.uclk.shani.youseelk.R;
import com.uclk.shani.youseelk.logger.Log;


/**
 * Created by Shani on 10/07/2017.
 */

public class FinalLocationsAdapter extends RecyclerView.Adapter<FinalLocationsAdapter.PlanViewHolder> {

    private String[] locations;
    private String[] distances;
    private Context context;
    private String[] totalTime;

    public FinalLocationsAdapter(String[] locations, String[] distances, Context context, String[] totalTime) {
        this.locations = locations;
        this.distances = distances;
        this.context = context;
        this.totalTime = totalTime;
    }

    class PlanViewHolder extends RecyclerView.ViewHolder{

        TextView loc, dist, tim;
        int ref;

        PlanViewHolder(View itemView) {
            super(itemView);
            loc = (TextView)itemView.findViewById(R.id.cityName);
            dist = (TextView) itemView.findViewById(R.id.distance);
            tim = (TextView) itemView.findViewById(R.id.textTime);
        }
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_plan_row, parent, false);

        Log.d("FinalLocationsAdapter","return row view");
        return new PlanViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        holder.ref = holder.getAdapterPosition();
        holder.loc.setText(locations[holder.ref]);
        holder.dist.setText(distances[holder.ref]);
        holder.tim.setText(totalTime[holder.ref]);
        Log.d("FinalLocationsAdapter","set texts");
    }

    @Override
    public int getItemCount(){
        return locations.length;
    }

}
