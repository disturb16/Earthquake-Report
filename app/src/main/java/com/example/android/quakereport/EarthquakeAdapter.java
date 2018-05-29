package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.Utils.DateHelper;

import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null)
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.quake_list_item, parent, false);

        Earthquake currentQuake = getItem(position);

        TextView magnitud = (TextView) itemView.findViewById(R.id.txtMagnitud);
        TextView location = (TextView) itemView.findViewById(R.id.txtLocation);
        TextView locationoffset = (TextView) itemView.findViewById(R.id.txtLocationOffset);
        TextView date = (TextView) itemView.findViewById(R.id.txtDate);
        TextView time = (TextView) itemView.findViewById(R.id.txtTime);


        // location format
        String[] place = currentQuake.getPlace();
        locationoffset.setText(place[0]);
        location.setText(place[1]);


        // magnitude format
        magnitud.setText(currentQuake.getFormattedMagnitud());
        GradientDrawable circleMagnitude = (GradientDrawable) magnitud.getBackground();
        int magnitudeColor = getMagnitudColor(currentQuake.getMagnitude());
        circleMagnitude.setColor(magnitudeColor);

        // date format
        Date earthquakeDate = new Date(currentQuake.getTimeMilliseconds());
        date.setText(DateHelper.formatToDate(earthquakeDate));
        time.setText(DateHelper.formatToTime(earthquakeDate));


        return itemView;
    }


    private int getMagnitudColor(double magnitud){

        int mag;
        switch ( (int)Math.floor(magnitud) ){
            case 0:
                mag = R.color.magnitude1;
                break;

            case 1:
                mag = R.color.magnitude1;
                break;

            case 2:
                mag = R.color.magnitude2;
                break;
            case 3:
                mag = R.color.magnitude3;
                break;
            case 4:
                mag = R.color.magnitude4;
                break;
            case 5:
                mag = R.color.magnitude5;
                break;
            case 6:
                mag = R.color.magnitude6;
                break;
            case 7:
                mag = R.color.magnitude7;
                break;
            case 8:
                mag = R.color.magnitude8;
                break;
            case 9:
                mag = R.color.magnitude9;
                break;

            default:
                mag = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), mag);
    }
}
