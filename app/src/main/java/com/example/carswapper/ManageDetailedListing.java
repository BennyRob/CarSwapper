package com.example.carswapper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ManageDetailedListing extends AppCompatActivity {

    private String loggedInUser;

    int listingID;
    private Listing listing;
    private Button viewOffersButton;
    private Button deleteListingButton;

    private TextView nameText;
    private TextView priceValueText;
    private ArrayList<Listing> totalListings;
    private ImageView carPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_detailed_listing);

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

        configureViewOffersbutton();
        configureDeleteListingButton();
    }


    private void getTotalListings() throws IOException, JSONException {
        totalListings = new ArrayList<>();

        jsonHandler handler = new jsonHandler();
        JSONArray jArray = handler.read(ManageDetailedListing.this, "listings.json");
        imageHandler imageHandler = new imageHandler();
        if (jArray != null) {
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
                System.out.println(id);
            }
        }
    }


    private void configureInfo() {

        System.out.println(listingID);

        for (int i = 0; i < totalListings.size(); i++) {
            if (listingID == totalListings.get(i).getIdNumber()) {
                listing = totalListings.get(i);
            }
        }

        nameText = findViewById(R.id.nameText2);
        nameText.setText(listing.getYear() + " " + listing.getMake() + " " + listing.getModel());

        priceValueText = findViewById(R.id.otherText2);
        priceValueText.setText("Value: $" + listing.getValue() + "   " + "Miles: " + listing.getMiles());

        carPic = findViewById(R.id.carPic2);
        carPic.setImageBitmap(listing.getPhoto());

    }

    private void configureViewOffersbutton(){
        viewOffersButton = findViewById(R.id.viewOffersButton);
        viewOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageDetailedListing.this, ViewOffers.class);
                intent.putExtra("username", loggedInUser);
                intent.putExtra("ID", listingID);
                startActivity(intent);
            }
        });
    }

    private void configureDeleteListingButton(){
        deleteListingButton = findViewById(R.id.deleteListing);
        deleteListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageDetailedListing.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to delete this listing?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                jsonHandler handler = new jsonHandler();

                                try {
                                    handler.deleteListing(ManageDetailedListing.this, listingID);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ManageListings.ac.finish();

                                Intent intent = new Intent(ManageDetailedListing.this, ManageListings.class);
                                intent.putExtra("username", loggedInUser);
                                intent.putExtra("ID", listingID);
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
    }

}

