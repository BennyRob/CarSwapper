package com.example.carswapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    private String loggedInUser;

    private String[] years;
    private String[] makes;
    private ArrayList<String[]> models;
    private String[] priceRanges;
    private String[] miles;

    private Spinner year;
    private Spinner make;
    private Spinner model;
    private Spinner priceRange;
    private Spinner mile;

    private Button searchButton;
    private Button manageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");

        configureText();
        configureLists();
        configureYear();
        configureMake();
        configureModel();
        configurePrice();
        configureMiles();
        configureSearchButton();
        configureManageProfileButton();
    }

    private void configureText(){
        year = findViewById(R.id.year);
        year.setSelection(0);
        make = findViewById(R.id.make);
        make.setSelection(0);
        model = findViewById(R.id.model);
        model.setSelection(0);
        priceRange = findViewById((R.id.priceRange));
        priceRange.setSelection(0);
        mile = findViewById((R.id.miles));
        mile.setSelection(0);
    }

    private void configureLists(){

        //Hard coding for prototype

        //years
        years = new String[61];
        years[0] = "All Years";
        int j = 1;
        for(int i = 2020; i > 1960; i--){
            years[j] = Integer.toString(i);
            j++;
        }

        //Makes
        makes = new String[5];
        makes[0] = "All Makes";
        makes[1] = "Audi";
        makes[2] = "Chevy";
        makes[3] = "Dodge";
        makes[4] = "Ford";

        //Models
        models = new ArrayList<String[]>();

        String[] all = new String[1];
        all[0] = "All Models";
        models.add(all);

        String[] audi = new String[6];
        audi[0] = "All Models";
        audi[1] = "A3";
        audi[2] = "A4";
        audi[3] = "A5";
        audi[4] = "S5";
        audi[5] = "Q4";
        models.add(audi);

        String[] chevy = new String[6];
        chevy[0] = "All Models";
        chevy[1] = "Camaro";
        chevy[2] = "Corvette";
        chevy[3] = "Malibu";
        chevy[4] = "Silverado";
        chevy[5] = "Tahoe";
        models.add(chevy);

        String[] dodge = new String[6];
        dodge[0] = "All Models";
        dodge[1] = "Charger";
        dodge[2] = "Challenger";
        dodge[3] = "Durango";
        dodge[4] = "Journey";
        dodge[5] = "Grand Caravan";
        models.add(dodge);

        String[] ford = new String[6];
        ford[0] = "All Models";
        ford[1] = "Bronco";
        ford[2] = "Escape";
        ford[3] = "F-150";
        ford[4] = "Fusion";
        ford[5] = "Mustang";
        models.add(ford);

        //Prices
        priceRanges = new String[8];
        priceRanges[0] = "Any Price";
        priceRanges[1] = "$0 - $5,0000";
        priceRanges[2] = "$5,000 - $10,0000";
        priceRanges[3] = "$10,000 - $15,0000";
        priceRanges[4] = "$15,000 - $20,0000";
        priceRanges[5] = "$20,000 - $30,0000";
        priceRanges[6] = "$30,000 - $40,0000";
        priceRanges[7] = "$40,0000+";

        //Miles
        miles = new String[8];
        miles[0] = "Any Mileage";
        miles[1] = "0 - 10,000 Miles";
        miles[2] = "10,000 - 20,000 Miles";
        miles[3] = "20,000 - 30,000 Miles";
        miles[4] = "30,000 - 50,000 Miles";
        miles[5] = "50,000 - 70,000 Miles";
        miles[6] = "70,000 - 100,000 Miles";
        miles[7] = "100,000+ Miles";

    }

    private void configureYear(){
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item,years);
        year.setAdapter(yearAdapter);
    }

    private void configureMake(){
        ArrayAdapter<CharSequence> makeAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, makes);
        make.setAdapter(makeAdapter);
        make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                configureModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void configureModel(){
        ArrayAdapter<CharSequence> modelAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item,models.get(make.getSelectedItemPosition()));
        model.setAdapter(modelAdapter);
    }

    private void configurePrice(){
        ArrayAdapter<CharSequence> priceAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item,priceRanges);
        priceRange.setAdapter(priceAdapter);
    }

    private void configureMiles(){
        ArrayAdapter<CharSequence> milesAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item,miles);
        mile.setAdapter(milesAdapter);
    }



    private void configureSearchButton(){
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, ViewListings.class);
                intent.putExtra("username" , loggedInUser);

                intent.putExtra("year", year.getSelectedItem().toString());
                intent.putExtra("make", make.getSelectedItem().toString());
                intent.putExtra("model", model.getSelectedItem().toString());
                intent.putExtra("price", priceRange.getSelectedItem().toString());
                intent.putExtra("miles", mile.getSelectedItem().toString());

                startActivity(intent);

            }
        });
    }

    private void configureManageProfileButton(){
        manageButton = (Button) findViewById(R.id.manageButton);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, ManageProfile.class);
                intent.putExtra("username" , loggedInUser);
                startActivity(intent);

            }
        });
    }

}