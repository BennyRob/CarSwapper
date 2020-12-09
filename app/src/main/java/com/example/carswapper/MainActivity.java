package com.example.carswapper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button logButton;
    private Button accountButton;

    private EditText userText;
    private EditText passText;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            configureJsons();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configureText();
        configureLogButton();
        configureCreateButton();

    }

    private void configureText(){
        userText = findViewById(R.id.userText);
        userText.setHintTextColor(Color.GRAY);
        userText.setHint("Username");
        userText.getText().clear();

        passText = findViewById(R.id.passText);
        passText.setHintTextColor(Color.GRAY);
        passText.setHint("Password");
        passText.getText().clear();

    }

    private void configureLogButton(){
        logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int bool = 1;

                if(userText.getText().toString().matches("")){
                    userText.setHintTextColor(Color.rgb(217,43,30));
                    userText.setHint("Required");
                    bool = 0;
                }
                if(passText.getText().toString().matches("")){
                    passText.setHintTextColor(Color.rgb(217,43,30));
                    passText.setHint("Required");
                    bool = 0;
                }
                if(bool == 1) {

                    try {
                        if(checkCredentials(userText.getText().toString(), passText.getText().toString()) == 1) {
                            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                            intent.putExtra("username" , username);
                            startActivity(intent);
                            configureText();
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Incorrect username or password");
                            builder.setPositiveButton("Acknowledge",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        });
    }

    private void configureCreateButton(){
        accountButton = (Button) findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, CreateAccount.class));


                userText.setHint("Username");
                userText.setHintTextColor(Color.GRAY);
                userText.getText().clear();

                passText.setHint("Password");
                passText.setHintTextColor(Color.GRAY);
                passText.getText().clear();

            }
        });
    }

    private int checkCredentials(String username, String password) throws IOException, JSONException {
        jsonHandler handler = new jsonHandler();
        JSONArray profiles = handler.read(MainActivity.this, "profiles.json");

        if(profiles == null){
            return 0;
        }

        System.out.println(profiles.toString());
        for(int i = 0; i < profiles.length(); i++){
            JSONObject profile = (JSONObject) profiles.get(i);
            System.out.println("Typed username: " + username + ". Data username: " + profile.get("username"));
            System.out.println("Typed password: " + password + ". Data username: " + profile.get("password"));

            if (profile.get("username").toString().equals(username) && profile.get("password").toString().equals(password)){
                this.username = profile.get("username").toString();
                return 1;
            }
        }

        return 0;
    }

    private void configureJsons() throws IOException {
        File profiles = new File(MainActivity.this.getFilesDir().getPath() + java.io.File.separator +"profiles.json");
        if(!profiles.exists()) {
            profiles.createNewFile();
        }

        File listings = new File(MainActivity.this.getFilesDir().getPath() + java.io.File.separator +"listings.json");
        if(!listings.exists()) {
            listings.createNewFile();
        }

        File offers = new File(MainActivity.this.getFilesDir().getPath() + java.io.File.separator +"offers.json");
        if(!offers.exists()) {
            offers.createNewFile();
        }





    }
}