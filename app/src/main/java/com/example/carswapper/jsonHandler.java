package com.example.carswapper;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class jsonHandler {


    public jsonHandler(){

    }

    public void write(Context context, JSONObject object, String fileName) throws IOException, JSONException {
        JSONArray jArray = read(context, fileName);
        if (jArray == null){
            jArray = new JSONArray();
        }
        jArray.put(object);

        File file = new File(context.getFilesDir(), fileName);
        FileWriter writer = new FileWriter(file);
        BufferedWriter bufWriter = new BufferedWriter(writer);
        bufWriter.write(jArray.toString());
        bufWriter.close();
    }

    public JSONArray read(Context context, String fileName) throws IOException, JSONException {

        File file = new File(context.getFilesDir(), fileName);

        FileInputStream jsonInput = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        jsonInput.read(data);
        jsonInput.close();

        String str = new String(data, "UTF-8");
        if(str.length() == 0){
            return null;
        } else {
            return new JSONArray(str);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteListing(Context context, int id) throws IOException, JSONException {
        JSONArray listings = read(context, "listings.json");
        if(listings != null) {
            for (int i = 0; i < listings.length(); i++) {
                JSONObject listing = (JSONObject) listings.get(i);
                if (listing.get("id").equals(id)) {
                    listings.remove(i);
                }
            }


            File file = new File(context.getFilesDir(), "listings.json");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufWriter = new BufferedWriter(writer);
            bufWriter.write(listings.toString());
            bufWriter.close();
        }

        //Delete offers for this vehicle
        deleteOffer(context, id, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void deleteOffer(Context context, int id, int type) throws IOException, JSONException {
        JSONArray offers = read(context, "offers.json");
        if(offers != null) {
            for (int i = 0; i < offers.length(); i++) {
                JSONObject offer = (JSONObject) offers.get(i);
                if (type == 0) {
                    if (offer.get("idFor").equals(id)) {
                        offers.remove(i);
                    }
                } else if (type == 1) {
                    if (offer.get("idFrom").equals(id)) {
                        offers.remove(i);
                    }
                }
            }


            File file = new File(context.getFilesDir(), "offers.json");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufWriter = new BufferedWriter(writer);
            bufWriter.write(offers.toString());
            bufWriter.close();
        }
    }

}
