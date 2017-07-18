package com.uclk.shani.youseelk.objects;

import java.util.List;

/**
 * Created by Shani on 23/06/2017.
 */

public class PlaceCard {
    private String placeName;
    private String placeProvince;
    private int thumbnail; //change type if you want. int because of image ID
    private List<String> tags;

    public PlaceCard(String placeName, String placeProvince, int thumbnail, List<String> tags) {
        this.placeName = placeName;
        this.placeProvince = placeProvince;
        this.thumbnail = thumbnail;
        this.tags = tags;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceProvince() {
        return placeProvince;
    }

    public void setPlaceProvince(String placeProvince) {
        this.placeProvince = placeProvince;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
