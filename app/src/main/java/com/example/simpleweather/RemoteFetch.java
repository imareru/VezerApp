package com.example.simpleweather;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFetch {
	private static JSONObject data;
	private static final String OPEN_WEATHER_MAP_API =
			"https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

	public static JSONObject getJSON(Context context, String city){
		try {

			URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
			HttpURLConnection connection =
					(HttpURLConnection)url.openConnection();


			connection.addRequestProperty("x-api-key",
					context.getString(
							R.string.open_weather_maps_app_id
					)
			);

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			StringBuilder json = new StringBuilder(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();

			data = new JSONObject(json.toString());
			if(data.getInt("cod") != 200){
				return null;
			}

//			return data;
		}catch(Exception e){
			System.err.println("----------------------------"+e.getMessage());
		}
		return data;
	}

	public static JSONObject getJSON(String latitude, String longitude) {
		try {
			String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=ed409747919d19d26246a45886401d21&lang=ru&units=metric";
			URL url = new URL(urlString);
			HttpURLConnection connection =
					(HttpURLConnection)url.openConnection();

//
//			connection.addRequestProperty("x-api-key",
//					context.getString(
//							R.string.open_weather_maps_app_id
//					)
//			);

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			StringBuilder json = new StringBuilder(1024);
			String tmp="";
			while((tmp=reader.readLine())!=null)
				json.append(tmp).append("\n");
			reader.close();

			data = new JSONObject(json.toString());
			if(data.getInt("cod") != 200){
				return null;
			}

//			return data;
		}catch(Exception e){
			System.err.println("----------------------------"+e.getMessage());
		}
		return data;
	}
}
