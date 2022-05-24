package com.example.simpleweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.location.LocationManager;

public class CityPreference {

	SharedPreferences prefs;
	LocationManager locationManager;

	public CityPreference(Activity activity){
		prefs = activity.getPreferences(Activity.MODE_PRIVATE);
	}

	String getCity(){

		return prefs.getString("city", "Sydney, AU");
	}

	@SuppressLint("ApplySharedPref")
	void setCity(String city){
		prefs.edit().putString("city", city).commit();
	}

}
