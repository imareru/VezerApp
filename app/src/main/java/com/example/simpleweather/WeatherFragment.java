package com.example.simpleweather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.simpleweather.adapter.CityResult;
import com.example.simpleweather.adapter.CityResultTableActivity;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

	Typeface weatherFont;

	TextView cityField;
	TextView updatedField;
	TextView detailsField;
	TextView currentTemperatureField;
	TextView weatherIcon;

	Handler handler;

	public WeatherFragment(){
		handler = new Handler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
		cityField = (TextView)rootView.findViewById(R.id.city_field);
		updatedField = (TextView)rootView.findViewById(R.id.updated_field);
		detailsField = (TextView)rootView.findViewById(R.id.details_field);
		currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
		weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

		weatherIcon.setTypeface(weatherFont);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
		updateWeatherData(new CityPreference(getActivity()).getCity());
	}

	private void updateWeatherData(final String city){
		new Thread(){
			public void run(){
				final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
//				System.out.println("---------------"+json);
				if(json == null){
					handler.post(new Runnable(){
						public void run(){
							Toast.makeText(getActivity(),
									requireActivity().getString(R.string.place_not_found),
									Toast.LENGTH_LONG).show();
						}
					});
				} else {
					handler.post(new Runnable(){
						public void run(){
							renderWeather(json);
						}
					});
				}
			}
		}.start();
	}

	private void updateWeatherData(final String latitude,final String longitude){
		new Thread(){
			public void run(){
				final JSONObject json = RemoteFetch.getJSON(latitude, longitude);
//				System.out.println("---------------"+json);
				if(json == null){
					handler.post(new Runnable(){
						public void run(){
							Toast.makeText(getActivity(),
									requireActivity().getString(R.string.place_not_found),
									Toast.LENGTH_LONG).show();
						}
					});
				} else {
					handler.post(new Runnable(){
						public void run(){
							renderWeather(json);
						}
					});
				}
			}
		}.start();
	}

	private void renderWeather(JSONObject json){
		try {
			cityField.setText(json.getString("name").toUpperCase(Locale.US));

			JSONObject details = json.getJSONArray("weather").getJSONObject(0);
			JSONObject main = json.getJSONObject("main");
			detailsField.setText(
					details.getString("description").toUpperCase(Locale.US) +
							"\n" + "Влажность: " + main.getString("humidity") + "%" +
							"\n" + "Давление: " + main.getString("pressure") + " hPa");

			currentTemperatureField.setText(
					String.format("%.2f", main.getDouble("temp"))+ " ℃");
//			Добавляем в список найденный город
			System.out.println("-------------------------"+!CityResultTableActivity.cityResults.isEmpty());
			boolean est = false;
			if(!CityResultTableActivity.cityResults.isEmpty()) {
				for (int i = 0; i < CityResultTableActivity.cityResults.size(); i++) {
					//						System.out.println("____________________----------------" + CityResultTableActivity.cityResults.get(i).getCity());
					//						System.out.println("____________________----------------" + json.getString("name").toUpperCase(Locale.US));
					est = CityResultTableActivity.cityResults.get(i).getCity().equals(json.getString("name").toUpperCase(Locale.US));
					if (est)
						break;
					else
						est =false;
				}
				if (!est)
					CityResultTableActivity.cityResults.add(new CityResult(json.getString("name").toUpperCase(Locale.US), "Температура:" + String.format("%.2f", main.getDouble("temp"))));
			}else {
				CityResultTableActivity.cityResults.add(new CityResult(json.getString("name").toUpperCase(Locale.US), "Температура:" + String.format("%.2f", main.getDouble("temp"))));
			}
			DateFormat df = DateFormat.getDateTimeInstance();
			String updatedOn = df.format(new Date(json.getLong("dt")*1000));
			updatedField.setText("Последнее обновление: " + updatedOn);

			setWeatherIcon(details.getInt("id"),
					json.getJSONObject("sys").getLong("sunrise") * 1000,
					json.getJSONObject("sys").getLong("sunset") * 1000);

		}catch(Exception e){
			Log.e("SimpleWeather", "One or more fields not found in the JSON data");
		}
	}

	private void setWeatherIcon(int actualId, long sunrise, long sunset){
		int id = actualId / 100;
		String icon = "";
		if(actualId == 800){
			long currentTime = new Date().getTime();
			if(currentTime>=sunrise && currentTime<sunset) {
				icon = getActivity().getString(R.string.weather_sunny);
			} else {
				icon = getActivity().getString(R.string.weather_clear_night);
			}
		} else {
			switch(id) {
				case 2 : icon = getActivity().getString(R.string.weather_thunder);
					break;
				case 3 : icon = getActivity().getString(R.string.weather_drizzle);
					break;
				case 7 : icon = getActivity().getString(R.string.weather_foggy);
					break;
				case 8 : icon = getActivity().getString(R.string.weather_cloudy);
					break;
				case 6 : icon = getActivity().getString(R.string.weather_snowy);
					break;
				case 5 : icon = getActivity().getString(R.string.weather_rainy);
					break;
			}
		}
		weatherIcon.setText(icon);
	}

	public void changeCity(String city){
		updateWeatherData(city);
	}

	public void changeCity(String latitude,String longitude){
		updateWeatherData(latitude,longitude);
	}
}
