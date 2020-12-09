package com.example.carswapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ViewListings extends AppCompatActivity {

    private String loggedInUser;

    private ArrayList<Listing> totalListings;
    private ArrayList<Listing> searchedListings;

    private ScrollView scrollListings;
    private LinearLayout layout;
    private TextView resultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listings);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");


        try {
            getTotalListings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        configureListings();
        configureScroller();
    }

    //Need this until databse works
    private void getTotalListings() throws IOException, JSONException {
        totalListings = new ArrayList<>();

        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(ViewListings.this, "listings.json");
        imageHandler imageHandler = new imageHandler();
        if(jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject listing = (JSONObject) jArray.get(i);

                int id = (int) listing.get("id");
                String username = (String) listing.get("username");
                String year = (String) listing.get("year");
                String make = (String) listing.get("make");
                String model = (String) listing.get("model");
                String value = (String) listing.get("value");
                String miles = (String) listing.get("miles");
                Bitmap map = imageHandler.fromBas64((String) listing.get("picture"));
                Listing newListing = new Listing(id, username, year, make, model, value, miles, map);
                if (!username.equals(loggedInUser)) {
                    totalListings.add(newListing);
                }
                System.out.println(id);
            }
        }
    }

    private void configureListings(){
        searchedListings = new ArrayList<>();

        String year;
        String make;
        String model;
        String price;
        String miles;

        Bundle extras = getIntent().getExtras();
        year = (String) extras.get("year");
        make = (String) extras.get("make");
        model = (String) extras.get("model");
        price = (String) extras.get("price");
        miles = (String) extras.get("miles");

        for(int i = 0; i < totalListings.size(); i++){
            int bool = 1;

            //Check year
            if(year.equals("All Years")){
                //Do nothing
            } else if (!year.equals(totalListings.get(i).getYear())) {
                bool = 0;
            }

            //Check Make
            if(make.equals("All Makes")){
                //Do nothing
            } else if(!make.equals(totalListings.get(i).getMake())){
                bool = 0;
            }

            //Check Model
            if(model.equals("All Models")){
                //Do nothing
            } else if(!model.equals(totalListings.get(i).getModel())){
                bool = 0;
            }

            int val = Integer.parseInt(totalListings.get(i).getValue());
            if(price.equals("Any Price")){
                //Do nothing
            } else if(price.equals("$0 - $5,0000")){
                if(val > 5000){
                    bool = 0;
                }
            } else if(price.equals("$5,000 - $10,0000")){
                if(val < 5000 || val > 10000){
                    bool = 0;
                }
            } else if(price.equals("$10,000 - $15,0000")){
                if(val < 10000 || val > 15000){
                    bool = 0;
                }
            } else if(price.equals("$15,000 - $20,0000")){
                if(val < 15000 || val > 20000){
                    bool = 0;
                }
            } else if(price.equals("$20,000 - $30,0000")){
                if(val < 20000 || val > 30000){
                    bool = 0;
                }
            } else if(price.equals("$30,000 - $40,0000")){
                if(val < 30000 || val > 40000){
                    bool = 0;
                }
            } else if(price.equals("$40,0000+")){
                if(val < 40000){
                    bool = 0;
                }
            }

            int milesValue = Integer.parseInt(totalListings.get(i).getMiles());
            if(miles.equals("Any Mileage")){
                //Do nothing
            } else if(miles.equals("0 - 10,000 Miles")){
                if(milesValue > 10000){
                    bool = 0;
                }
            } else if(miles.equals("10,000 - 20,000 Miles")){
                if(milesValue < 10000 || milesValue > 20000){
                    bool = 0;
                }
            } else if(miles.equals("20,000 - 30,000 Miles")){
                if(milesValue < 200000 || milesValue > 30000){
                    bool = 0;
                }
            } else if(miles.equals("30,000 - 50,000 Miles")){
                if(milesValue < 30000 || milesValue > 50000){
                    bool = 0;
                }
            } else if(miles.equals("50,000 - 70,000 Miles")){
                if(milesValue < 50000 || milesValue > 70000){
                    bool = 0;
                }
            } else if(miles.equals("70,000 - 100,000 Miles")){
                if(milesValue < 70000 || milesValue > 100000){
                    bool = 0;
                }
            } else if(miles.equals("100,000+ Miles")){
                if(milesValue < 100000){
                    bool = 0;
                }
            }


            if(bool == 1){
                searchedListings.add(totalListings.get(i));
            }
        }

        resultsText = findViewById(R.id.resultsText);
        resultsText.setText(searchedListings.size() + " Results");

    }

    private void configureScroller(){
        scrollListings = findViewById(R.id.scrollListings);
        layout = findViewById(R.id.layout);

        for(int i = 0; i < searchedListings.size(); i++) {
            Listing currentListing = searchedListings.get(i);
            TextView infoText = new TextView(this);
//            infoText.setPadding(20,20,20,20);
            infoText.setTextColor(Color.BLACK);
            infoText.setTextSize(20);
            infoText.setGravity(Gravity.CENTER_HORIZONTAL);
            infoText.setText(currentListing.getYear() + " " + currentListing.getMake() + " " + currentListing.getModel());
            layout.addView(infoText);

            ImageView image = new ImageView(ViewListings.this);
            image.setImageBitmap(currentListing.getPhoto());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(image);

            TextView priceText = new TextView(this);
//            priceText.setPadding(20,20,20,20);
            priceText.setTextColor(Color.BLACK);
            priceText.setTextSize(20);
            priceText.setGravity(Gravity.CENTER_HORIZONTAL);
            priceText.setText("Value: $" + currentListing.getValue() + "   " + "Miles: " + currentListing.getMiles());
            layout.addView(priceText);

            Button viewButton = new Button(this);
            viewButton.setText("View Listing");
            viewButton.setTextColor(Color.WHITE);
            viewButton.setBackgroundColor(Color.rgb(217,43,30));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ViewListings.this, DetailedListing.class);
                    intent.putExtra("username", loggedInUser);
                    intent.putExtra("ID" , currentListing.getIdNumber());
                    startActivity(intent);
                }
            });
            layout.addView(viewButton);

        }
    }


}