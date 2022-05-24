package com.example.simpleweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.simpleweather.R;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<CityResult> {

    private LayoutInflater inflater;
    private int layout;
    private List<CityResult> cityResults;

    public ResultAdapter(Context context, int resource, List<CityResult> cityResults) {
        super(context, resource, cityResults);
        this.cityResults = cityResults;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.city);
        TextView resultView = view.findViewById(R.id.temperature);

        CityResult cityResult = cityResults.get(position);

        nameView.setText(cityResult.getCity());
        resultView.setText(cityResult.getTemperature());

        return view;
    }
}