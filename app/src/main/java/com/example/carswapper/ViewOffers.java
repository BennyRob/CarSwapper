package com.example.carswapper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
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

public class ViewOffers extends AppCompatActivity {

    private String loggedInUser;
    private int listingID;
    private ScrollView scrollListings2;
    private ArrayList<Listing> totalListings;
    private ArrayList<Offer> totalOffers;
    private ArrayList<Listing> offeredListings;

    private TextView resultText;
    private LinearLayout layout2;

    public static Activity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");
        listingID = (int) extras.get("ID");

        ac = this;

        try {
            getTotalListings();
            getTotalOffers();
            getOfferedListings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(offeredListings != null) {
            try {
                configureScroller();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void getTotalListings() throws IOException, JSONException {
        totalListings = new ArrayList<>();
        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(ViewOffers.this, "listings.json");
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
                totalListings.add(newListing);

            }
        }
    }

    private void getTotalOffers() throws IOException, JSONException {
        totalOffers = new ArrayList<>();
        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(ViewOffers.this, "offers.json");

        if(jArray != null){
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject offer = (JSONObject) jArray.get(i);

                int idFor = (int) offer.get("idFor");
                int idFrom = (int) offer.get("idFrom");
                Offer newOffer = new Offer(idFor,idFrom);
                totalOffers.add(newOffer);
            }
        }
    }

    private void getOfferedListings(){
        offeredListings = new ArrayList<>();
        if(totalListings != null && totalOffers != null) {
            for (int i = 0; i < totalListings.size(); i++) {

                for (int j = 0; j < totalOffers.size(); j++) {
                    if (totalListings.get(i).getIdNumber() == totalOffers.get(j).getIdFrom() && listingID == totalOffers.get(j).getIdFor()) {
                        offeredListings.add(totalListings.get(i));
                    }
                }
            }
        }

        resultText = findViewById(R.id.resultsText4);
        if(offeredListings.size() == 1){
            resultText.setText("You have 1 offer");
        } else if (offeredListings.size() > 1){
            resultText.setText("You have " + offeredListings.size() + " offers");
        }

    }

    private void configureScroller() throws IOException, JSONException {
        scrollListings2 = findViewById(R.id.viewOffersView);
        layout2 = findViewById(R.id.viewOffersLayout);

        for(int i = 0; i < offeredListings.size(); i++) {
            Listing currentListing = offeredListings.get(i);
            TextView infoText = new TextView(this);
//            infoText.setPadding(20,20,20,20);
            infoText.setTextColor(Color.BLACK);
            infoText.setTextSize(20);
            infoText.setGravity(Gravity.CENTER_HORIZONTAL);
            infoText.setText(currentListing.getYear() + " " + currentListing.getMake() + " " + currentListing.getModel());
            layout2.addView(infoText);

            ImageView image = new ImageView(ViewOffers.this);
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

            String username = null;
            String email = null;
            String phone = null;

            jsonHandler handler = new jsonHandler();
            JSONArray jArray = handler.read(ViewOffers.this, "profiles.json");

            for(int j = 0; j < jArray.length(); j++){
                JSONObject profile = (JSONObject) jArray.get(j);
                if(currentListing.getUser().equals(profile.get("username"))){
                    username = (String) profile.get("username");
                    email = (String) profile.get("email");
                    phone = (String) profile.get("phone");
                }
            }

            TextView userText = new TextView(this);
//            priceText.setPadding(20,20,20,20);
            userText.setTextColor(Color.BLACK);
            userText.setTextSize(20);
            userText.setGravity(Gravity.CENTER_HORIZONTAL);
            priceText.setText("Offer from " + username);
            layout2.addView(userText);

            TextView emailText = new TextView(this);
//            priceText.setPadding(20,20,20,20);
            emailText.setTextColor(Color.BLACK);
            emailText.setTextSize(20);
            emailText.setGravity(Gravity.CENTER_HORIZONTAL);
            emailText.setText("Email: " + email  + " Phone: " + phone);
            layout2.addView(emailText);

            Button viewButton = new Button(this);
            viewButton.setText("Delete Offer");
            viewButton.setTextColor(Color.WHITE);
            viewButton.setBackgroundColor(Color.rgb(217,43,30));
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewOffers.this);
                    builder.setCancelable(true);
                    builder.setTitle("Are you sure you want to delete this offer?");
                    Spinner spinner = new Spinner(builder.getContext());

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject offer = new JSONObject();
                                    jsonHandler handler = new jsonHandler();

                                    try {
                                        handler.deleteOffer(ViewOffers.this, currentListing.getIdNumber(),1 );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(ViewOffers.this, ViewOffers.class);
                                    intent.putExtra("username", loggedInUser);
                                    intent.putExtra("ID" , listingID);
                                    startActivity(intent);

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