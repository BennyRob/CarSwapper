package com.example.carswapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class ManageProfile extends AppCompatActivity {

    private String loggedInUser;

    TextView usernameInfo;
    TextView passwordInfo;
    TextView emailInfo;
    TextView phoneInfo;
    TextView zipInfo;

    Button manageListingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        Bundle extras = getIntent().getExtras();
        loggedInUser = (String) extras.get("username");

        configureManageListingsButton();
        configureText();

        try {
            configureProfileInformation();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void configureManageListingsButton(){
        manageListingsButton = (Button) findViewById(R.id.manageListingsButton);
        manageListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ManageProfile.this, ManageListings.class);
                intent.putExtra("username" ,loggedInUser);
                startActivity(intent);

            }
        });
    }

    private void configureText(){
        usernameInfo = findViewById(R.id.usernameInfo);
        passwordInfo = findViewById(R.id.passwordInfo);
        emailInfo = findViewById(R.id.emailInfo);
        phoneInfo = findViewById(R.id.phoneInfo);
        zipInfo = findViewById(R.id.zipInfo);

    }

    private void configureProfileInformation() throws IOException, JSONException {
        jsonHandler handler = new jsonHandler();
        JSONArray profiles = handler.read(ManageProfile.this, "profiles.json");

        System.out.println(profiles.toString());
        for(int i = 0; i < profiles.length(); i++){
            JSONObject profile = (JSONObject) profiles.get(i);

            if (profile.get("username").toString().equals(loggedInUser)){
                usernameInfo.setText("Username: " + profile.get("username").toString());
                passwordInfo.setText("Password: " + profile.get("password").toString());
                emailInfo.setText("Email: " + profile.get("email").toString());
                phoneInfo.setText("Phone: " + profile.get("phone").toString());
                zipInfo.setText("Zipcode: " + profile.get("zipcode").toString());
                return;
            }
        }

    }


}