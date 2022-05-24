package com.example.simpleweather.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simpleweather.R;
import com.example.simpleweather.WeatherActivityAdapterFind;

import java.util.ArrayList;

public class CityResultTableActivity extends AppCompatActivity {
    CityResult pushInfo;
    public static ArrayList<CityResult> cityResults = new ArrayList<CityResult>();
//    AdapterView<?> adapterView;
    ResultAdapter resultAdapter;
    ListView countriesList;
    private int positionCityInAdapter;
    private String city;
    CityResult cityResults1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        Button button = (Button)findViewById(R.id.personalAccauntButton);
        // получаем элемент ListView
        countriesList = findViewById(R.id.countriesList);
        // создаем адаптер
        resultAdapter = new ResultAdapter(this, R.layout.list_item, cityResults);
        // устанавливаем адаптер
        countriesList.setAdapter(resultAdapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                adapterView = parent;
                // получаем выбранный пункт
                CityResult selectedCityResult = (CityResult)parent.getItemAtPosition(position);
                cityResults1 =  (CityResult)parent.getItemAtPosition(position);
                pushInfo = (CityResult) parent.getItemAtPosition(position);
                positionCityInAdapter = position;
                city = selectedCityResult.getCity();
                Toast.makeText(getApplicationContext(), "Был выбран город: " + selectedCityResult.getCity(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        countriesList.setOnItemClickListener(itemListener);
    }

    public void deleteCity(View v) {
//        System.err.println("--------------------");
        if (pushInfo != null) {
//            System.err.println("--------------------"+positionCityInAdapter);
            resultAdapter.remove(cityResults1);
//            adapterView.removeViewAt(positionCityInAdapter);
        }else {
            Toast.makeText(getApplicationContext(), "Выберите город!!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void findCity(View v) {
        if (city != null) {
            Intent intent = new Intent(this, WeatherActivityAdapterFind.class);
// передача объекта с ключом "hello" и значением "Hello World"
            intent.putExtra("city", city);
// запуск SecondActivity
            startActivity(intent);


//            WeatherActivity weatherActivity = new WeatherActivity();
////            System.out.println("---------------------------"+weatherActivity);
////            weatherActivity.onCreate(savedInstanceState1);
//            weatherActivity.showInputDialog(city);
        } else {
            Toast.makeText(getApplicationContext(), "Выберите город!!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

//    private void setInitialData(){
//
////        cityResults.add(new CityResult("Kazan", "Температура: 18"));
////        cityResults.add(new CityResult("Moscow", "Температура: 24"));
////        states.add(new State ("Колумбия", "Богота", R.drawable.columbia));
////        states.add(new State ("Уругвай", "Монтевидео", R.drawable.uruguai));
////        states.add(new State ("Чили", "Сантьяго", R.drawable.chile));
//    }
}