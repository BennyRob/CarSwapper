package com.example.carswapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class CreateAccount extends AppCompatActivity {

    Button cancelButton;
    Button createButton;

    EditText userCreateText;
    EditText passCreateText;
    EditText emailText;
    EditText phoneText;
    EditText zipText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        configureText();
        configureCreate();
        configureCancel();
    }

    private void configureText(){
        userCreateText = findViewById(R.id.userCreateText);
        userCreateText.setHintTextColor(Color.GRAY);
        userCreateText.setHint("Username");
        userCreateText.getText().clear();

        passCreateText = findViewById(R.id.passCreateText);
        passCreateText.setHintTextColor(Color.GRAY);
        passCreateText.setHint("Password");
        passCreateText.getText().clear();;

        emailText = findViewById(R.id.emailText);
        emailText.setHintTextColor(Color.GRAY);
        emailText.setHint("Email");
        emailText.getText().clear();

        phoneText = findViewById(R.id.phoneText);
        phoneText.setHintTextColor(Color.GRAY);
        phoneText.setHint("Phone");
        phoneText.getText().clear();

        zipText = findViewById(R.id.zipText);
        zipText.setHintTextColor(Color.GRAY);
        zipText.setHint("Zip code");
        zipText.getText().clear();
    }

    private void configureCreate(){

        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm to create your account");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(checkText() == 1) {

                                    //Create JSON object
                                    JSONObject newProfile = new JSONObject();
                                    try {
                                        newProfile.put("username", userCreateText.getText());
                                        newProfile.put("password", passCreateText.getText());
                                        newProfile.put("email", emailText.getText());
                                        newProfile.put("phone", phoneText.getText());
                                        newProfile.put("zipcode", zipText.getText());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //Insert into JSON object
                                    jsonHandler handler = new jsonHandler();
                                    try {
                                        handler.write(CreateAccount.this, newProfile, "profiles.json");
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }


                                    startActivity(new Intent(CreateAccount.this, MainActivity.class));
                                    configureText();

                                } else {
                                    return;
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
        });

    }

    private int checkText(){
        int bool = 1;
        if(userCreateText.getText().toString().matches("")){
            userCreateText.setHintTextColor(Color.rgb(217,43,30));
            userCreateText.setHint("Required");
            bool = 0;
        }
        if(passCreateText.getText().toString().matches("")){
            passCreateText.setHintTextColor(Color.rgb(217,43,30));
            passCreateText.setHint("Required");
            bool = 0;
        }

        if(emailText.getText().toString().matches("")){
            emailText.setHintTextColor(Color.rgb(217,43,30));
            emailText.setHint("Required");
            bool = 0;
        }

        if(phoneText.getText().toString().matches("")){
            phoneText.setHintTextColor(Color.rgb(217,43,30));
            phoneText.setHint("Required");
            bool = 0;
        }

        if(zipText.getText().toString().matches("")){
            zipText.setHintTextColor(Color.rgb(217,43,30));
            zipText.setHint("Required");
            bool = 0;
        }

        return bool;
    }


    private void configureCancel(){
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to cancel?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(CreateAccount.this, MainActivity.class));
                                configureText();
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
