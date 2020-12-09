package com.example.carswapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SendOffer extends AppCompatActivity {

    private String loggedInUser;
    private int listingID;
    private ScrollView scrollListings2;
    private ArrayList<Listing> totalListings = new ArrayList<Listing>();

    private TextView resultText;
    private LinearLayout layout2;

    public static Activity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_offer);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");
        listingID = (int) extras.get("ID");



        ac = this;

        try {
            getTotalListings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        configureScroller();

    }


    private void getTotalListings() throws IOException, JSONException {
        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(SendOffer.this, "listings.json");
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
    }

    private void configureScroller(){
        scrollListings2 = findViewById(R.id.offerView);
        layout2 = findViewById(R.id.offerLayout);

        for(int i = 0; i < totalListings.size(); i++) {
            Listing currentListing = totalListings.get(i);
            TextView infoText = new TextView(this);
//            infoText.setPadding(20,20,20,20);
            infoText.setTextColor(Color.BLACK);
            infoText.setTextSize(20);
            infoText.setGravity(Gravity.CENTER_HORIZONTAL);
            infoText.setText(currentListing.getYear() + " " + currentListing.getMake() + " " + currentListing.getModel());
            layout2.addView(infoText);

            ImageView image = new ImageView(SendOffer.this);
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
            viewButton.setText("Offer Vehicle");
            viewButton.setTextColor(Color.WHITE);
            viewButton.setBackgroundColor(Color.rgb(217,43,30));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendOffer.this);
                    builder.setCancelable(true);
                    builder.setTitle("Are you sure you want to send an offer for this vehicle?");
                    Spinner spinner = new Spinner(builder.getContext());

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject offer = new JSONObject();
                                    jsonHandler handler = new jsonHandler();

                                    try {
                                        offer.put("idFor", listingID);
                                        offer.put("idFrom", currentListing.getIdNumber());
                                        handler.write(SendOffer.this, offer, "offers.json");
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }


                                    finish();
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            layout2.addView(viewButton);

        }
    }

}