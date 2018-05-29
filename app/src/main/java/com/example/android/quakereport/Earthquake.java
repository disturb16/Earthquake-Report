package com.example.android.quakereport;

import java.text.DecimalFormat;

public class Earthquake {

    Double magnitude;
    private String location;
    private Long timeMilliseconds;
    private String url;

    public Earthquake(Double _magnitude, String _location, Long _dateMilliseconds, String _url){
        magnitude = _magnitude;
        location = _location;
        timeMilliseconds = _dateMilliseconds;
        url = _url;
    }

    public Long getTimeMilliseconds() {
        return timeMilliseconds;
    }

    public String getLocation() {
        return location;
    }

    public String[] getPlace(){
        String[] place = new String[2];
        if(location.contains(" of ")){
             place =  location.split(" of ");
            place[0] += " of";
        }else{
            place[0] = "Near of ";
            place[1] = location;
        }

        return place;
    }


    public Double getMagnitude() {
        return magnitude;
    }

    public String getFormattedMagnitud(){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    public String getUrl() {
        return url;
    }
}
