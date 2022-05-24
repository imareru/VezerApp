package com.example.simpleweather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.simpleweather.adapter.CityResultTableActivity;


public class WeatherActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }
        getLocationPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
        }

        if (item.getItemId() == R.id.adapter_city) {
            Intent intent = new Intent();
            intent.setClass(this, CityResultTableActivity.class);
            startActivity(intent);
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбор города");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Поиск", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void showInputDialog(String city) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбор города");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(city);
        builder.setView(input);
        builder.setPositiveButton("Поиск", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);
        new CityPreference(this).setCity(city);
    }

    LocationTrack locationTrack;


    public void forwardCity(View view) {
        String city = null;
        TextView city_field = findViewById(R.id.city_field);
        for (int i = 0; i < CityResultTableActivity.cityResults.size(); i++) {
            if (CityResultTableActivity.cityResults.get(i).getCity().contentEquals(city_field.getText())) {
                if (CityResultTableActivity.cityResults.size() == i + 1)
                    city = CityResultTableActivity.cityResults.get(0).getCity();
                else
                    city = CityResultTableActivity.cityResults.get(i + 1).getCity();
                break;
            }
        }
        if (city != null) {
            System.out.println("city =============" + city);
            WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            wf.changeCity(city);
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка со спиком городов, он пуст.", Toast.LENGTH_SHORT).show();
        }
    }

    public void backCity(View view) {
        String city = null;
        TextView city_field = findViewById(R.id.city_field);
//         ь ь
//        System.out.println("---------349172981832"+city_field.getText());
        for (int i = 0; i < CityResultTableActivity.cityResults.size(); i++) {

            if (CityResultTableActivity.cityResults.get(i).getCity().contentEquals(city_field.getText())) {
                if (i - 1 < 0)
                    city = CityResultTableActivity.cityResults.get(CityResultTableActivity.cityResults.size() - 1).getCity();
                else
                    city = CityResultTableActivity.cityResults.get(i - 1).getCity();
                break;
            }
        }
        if (city != null) {
            WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            wf.changeCity(city);
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка со спиком городов", Toast.LENGTH_SHORT).show();
        }
    }

    public void gps_button(View view) {
        locationTrack = new LocationTrack(WeatherActivity.this);


        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);

            wf.changeCity(Double.toString(latitude), Double.toString(longitude));
            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();

        } else {

            locationTrack.showSettingsAlert();
        }
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                System.out.println(grantResults);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}