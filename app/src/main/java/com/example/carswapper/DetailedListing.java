package com.example.carswapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DetailedListing extends AppCompatActivity {

    private String loggedInUser;

    int listingID;
    private Listing listing;
    private Button sendOfferButton;

    private TextView nameText;
    private TextView priceValueText;
    private ArrayList<Listing> totalListings;
    private ArrayList<Listing> userListings;
    private ImageView carPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_listing);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");
        listingID = (int) extras.get("ID");

        try {
            getTotalListings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        configureInfo();
        configureSendOfferButton();
    }


    private void getTotalListings() throws IOException, JSONException {
        totalListings = new ArrayList<>();
        userListings = new ArrayList<>();

        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(DetailedListing.this, "listings.json");
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
                if(username.equals(loggedInUser)){
                    userListings.add(newListing);
                }
                System.out.println(id);
            }
        }
    }


    private void configureInfo(){

        System.out.println(listingID);

        for(int i = 0; i  < totalListings.size(); i++){
            if(listingID == totalListings.get(i).getIdNumber()){
                listing = totalListings.get(i);
            }
        }

        nameText = findViewById(R.id.nameText);
        nameText.setText(listing.getYear() + " " + listing.getMake() + " " + listing.getModel());

        priceValueText = findViewById(R.id.otherText);
        priceValueText.setText("Value: $" + listing.getValue() + "   " + "Miles: " + listing.getMiles());

        carPic = findViewById(R.id.carPic);
        carPic.setImageBitmap(listing.getPhoto());

    }

    private void configureSendOfferButton(){
        sendOfferButton = (Button) findViewById(R.id.sendOfferButton);
        sendOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedListing.this, SendOffer.class);
                intent.putExtra("username", loggedInUser);
                intent.putExtra("ID" , listingID);
                startActivity(intent);
            }
        });
    }

}