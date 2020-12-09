package com.example.carswapper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class ManageListings extends AppCompatActivity {

    private String loggedInUser;
    private ScrollView scrollListings2;
    private ArrayList<Listing> totalListings = new ArrayList<Listing>();

    private TextView resultText2;
    private LinearLayout layout2;

    Button createListingButton;

    public static Activity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_listings);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");

        ac = this;

        configureCreateListingsButton();

        try {
            getTotalListings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        configureScroller();
    }


    private void configureCreateListingsButton(){
        createListingButton = (Button) findViewById(R.id.createListingButton);
        createListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ManageListings.this, CreateListing.class);
                intent.putExtra("username" , loggedInUser);
                startActivity(intent);

            }
        });
    }

    private void getTotalListings() throws IOException, JSONException {
        resultText2 = findViewById(R.id.resultsText2);
        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(ManageListings.this, "listings.json");
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
                if (username.equals(loggedInUser)) {
                    totalListings.add(newListing);
                }
            }
        }
        if(totalListings.size() == 1){
            resultText2.setText("You have " + totalListings.size() + " listing");

        } else if(totalListings.size() > 1) {
            resultText2.setText("You have " + totalListings.size() + " listings");
        }
    }

    private void configureScroller(){
        scrollListings2 = findViewById(R.id.scrollListings2);
        layout2 = findViewById(R.id.layout2);

        for(int i = 0; i < totalListings.size(); i++) {
            Listing currentListing = totalListings.get(i);
            TextView infoText = new TextView(this);
//            infoText.setPadding(20,20,20,20);
            infoText.setTextColor(Color.BLACK);
            infoText.setTextSize(20);
            infoText.setGravity(Gravity.CENTER_HORIZONTAL);
            infoText.setText(currentListing.getYear() + " " + currentListing.getMake() + " " + currentListing.getModel());
            layout2.addView(infoText);

            ImageView image = new ImageView(ManageListings.this);
            image.setImageBitmap(currentListing.getPhoto());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            layout2.addView(image);

            TextView priceText = new TextView(this);
//            priceText.setPadding(20,20,20,20);
            priceText.setTextColor(Color.BLACK);
            priceText.setTextSize(20);
            priceText.setGravity(Gravity.CENTER_HORIZONTAL);
            priceText.setText("Value: $" + currentListing.getValue() + "   " + "Miles: " + currentListing.getMiles());
            layout2.addView(priceText);

            Button viewButton = new Button(this);
            viewButton.setText("View Listing");
            viewButton.setTextColor(Color.WHITE);
            viewButton.setBackgroundColor(Color.rgb(217,43,30));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ManageListings.this, ManageDetailedListing.class);
                    intent.putExtra("username", loggedInUser);
                    intent.putExtra("ID" , currentListing.getIdNumber());
                    startActivity(intent);
                }
            });
            layout2.addView(viewButton);

        }
    }
}