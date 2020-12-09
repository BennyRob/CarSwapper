package com.example.carswapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CreateListing extends AppCompatActivity {


    private String loggedInUser;

    String[] years;
    String[] makes;
    ArrayList<String[]> models;

    Spinner createYear;
    Spinner createMake;
    Spinner createModel;

    EditText createValue;
    EditText createMiles;

    Button uploadPhoto;
    Button cancelButton2;
    Button createButton2;

    private String photo;

    int RESPONSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");

        photo = null;

        configureText();
        configureLists();
        configureYear();
        configureMake();
        configureModel();
        configureUpload();
        configureCreate();
        configureCancel();
    }

    private void configureText(){
        createYear = findViewById(R.id.createYear);
        createYear.setSelection(0);
        createMake = findViewById(R.id.createMake);
        createMake.setSelection(0);
        createModel = findViewById(R.id.createModel);
        createModel.setSelection(0);

        createValue = findViewById(R.id.createValue);
        createMiles = findViewById(R.id.createMiles);
    }




    private void configureLists(){

        //Hard coding for prototype

        //years
        years = new String[60];
        int j = 0;
        for(int i = 2020; i > 1960; i--){
            years[j] = Integer.toString(i);
            j++;
        }

        //Makes
        makes = new String[4];
        makes[0] = "Audi";
        makes[1] = "Chevy";
        makes[2] = "Dodge";
        makes[3] = "Ford";

        //Models
        models = new ArrayList<String[]>();


        String[] audi = new String[5];
        audi[0] = "A3";
        audi[1] = "A4";
        audi[2] = "A5";
        audi[3] = "S5";
        audi[4] = "Q4";
        models.add(audi);

        String[] chevy = new String[5];
        chevy[0] = "Camaro";
        chevy[1] = "Corvette";
        chevy[2] = "Malibu";
        chevy[3] = "Silverado";
        chevy[4] = "Tahoe";
        models.add(chevy);

        String[] dodge = new String[5];
        dodge[0] = "Charger";
        dodge[1] = "Challenger";
        dodge[2] = "Durango";
        dodge[3] = "Journey";
        dodge[4] = "Grand Caravan";
        models.add(dodge);

        String[] ford = new String[5];
        ford[0] = "Bronco";
        ford[1] = "Escape";
        ford[2] = "F-150";
        ford[3] = "Fusion";
        ford[4] = "Mustang";
        models.add(ford);


    }

    private void configureYear(){
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<>(CreateListing.this, android.R.layout.simple_spinner_dropdown_item,years);
        createYear.setAdapter(yearAdapter);
    }

    private void configureMake(){
        ArrayAdapter<CharSequence> makeAdapter = new ArrayAdapter<>(CreateListing.this, android.R.layout.simple_spinner_dropdown_item, makes);
        createMake.setAdapter(makeAdapter);
        createMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ArrayAdapter<CharSequence> modelAdapter = new ArrayAdapter<>(CreateListing.this, android.R.layout.simple_spinner_dropdown_item,models.get(createMake.getSelectedItemPosition()));
        createModel.setAdapter(modelAdapter);
    }

    private void configureUpload(){
        uploadPhoto = findViewById(R.id.uploadPhoto);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESPONSE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESPONSE && resultCode == Activity.RESULT_OK) {

            try {
                InputStream stream = CreateListing.this.getContentResolver().openInputStream(data.getData());
                System.out.println(stream.toString());
                Bitmap map = BitmapFactory.decodeStream(stream);

                imageHandler handler = new imageHandler();
                photo = handler.toBase64(map);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }




    private void configureCreate(){
            createButton2 = (Button) findViewById(R.id.createButton2);
            createButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photo == null) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateListing.this);
                        builder.setCancelable(true);
                        builder.setTitle("You must upload a photo");
                        builder.setPositiveButton("Acknowledge",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateListing.this);
                        builder.setCancelable(true);
                        builder.setTitle("Confirm to create this listing ");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(checkText() == 1) {

                                        try {
                                            newListing();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                            ManageListings.ac.finish();

                                            Intent intent = new Intent(CreateListing.this, ManageListings.class);
                                            intent.putExtra("username", loggedInUser);
                                            startActivity(intent);
                                            finish();
                                        }

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
                }
            });
        configureCancel();

    }

    private int checkText(){
        int bool = 1;
        if(createValue.getText().toString().matches("")){
            createValue.setHintTextColor(Color.rgb(217,43,30));
            createValue.setHint("Required");
            bool = 0;
        }
        if(createMiles.getText().toString().matches("")){
            createMiles.setHintTextColor(Color.rgb(217,43,30));
            createMiles.setHint("Required");
            bool = 0;
        }

        return bool;
    }

    private void newListing() throws IOException, JSONException {
        //Get highest current ID
        jsonHandler handler = new jsonHandler();
        JSONArray listings = null;

        listings =  handler.read(CreateListing.this, "listings.json");

        int idNumber = 0;

        if(listings == null){
            idNumber = 1;
        } else {
            for(int i = 0; i < listings.length(); i++){
                JSONObject listing = (JSONObject) listings.get(i);

                int currval = (int) listing.get("id");
                if(currval > idNumber);
                idNumber = currval + 1;
            }
        }

        JSONObject listing = new JSONObject();
        listing.put("id", idNumber);
        listing.put("username", loggedInUser);
        listing.put("year", createYear.getSelectedItem().toString());
        listing.put("make",createMake.getSelectedItem().toString());
        listing.put("model", createModel.getSelectedItem().toString());
        listing.put("value",createValue.getText());
        listing.put("miles",createMiles.getText());
        listing.put("picture",photo);
        handler.write(CreateListing.this,listing,"listings.json");

    }



    private void configureCancel(){
        cancelButton2 = (Button) findViewById(R.id.cancelButton2);
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateListing.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to cancel?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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