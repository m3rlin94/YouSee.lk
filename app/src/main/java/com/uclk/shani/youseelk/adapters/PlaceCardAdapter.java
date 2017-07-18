package com.uclk.shani.youseelk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uclk.shani.youseelk.R;
import com.uclk.shani.youseelk.activities.SelectLocationsActivity;
import com.uclk.shani.youseelk.objects.PlaceCard;
import com.uclk.shani.youseelk.taglib.Tag;
import com.uclk.shani.youseelk.taglib.TagView;

import java.util.List;

/**
 * Created by Shani on 23/06/2017.
 */

public class PlaceCardAdapter extends RecyclerView.Adapter<PlaceCardAdapter.PlaceViewHolder>{

    private Context context;
    private List<PlaceCard> placeList;

    //inner class viewholder
    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView name, province;
        public ImageView thumbnail;
        public TagView tagview;
        public Button remove;

        public PlaceViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.placeName);
            province = (TextView) view.findViewById(R.id.placeProvince);
            thumbnail = (ImageView) view.findViewById(R.id.placeImage);
            tagview = (TagView)view.findViewById(R.id.placeTagGroup);
            remove = (Button)view.findViewById(R.id.btnRemoveCard);

        }
    }

    public PlaceCardAdapter(Context context, List<PlaceCard> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_cardview,parent,false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, int position){

        PlaceCard placeCard = placeList.get(position);
        holder.name.setText(placeCard.getPlaceName());
        holder.province.setText(placeCard.getPlaceProvince());

        // should prevent adding same tags to the new cards????????????????????
        /*for (int i=0;i<placeCard.getTags().size();i++){
            Tag tag = new Tag(placeCard.getTags().get(i));
            tag.tagTextSize=9;
            tag.isDeletable=false;
            holder.tagview.addTag(tag);
        }*/

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                //SelectLocationsActivity.addedLocations.remove(holder.getAdapterPosition());

            }
        });

        Glide.with(context).load(placeCard.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}


