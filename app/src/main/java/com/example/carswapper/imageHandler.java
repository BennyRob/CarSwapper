package com.example.carswapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class imageHandler {

    public imageHandler(){

    }

    public String toBase64(Bitmap map){
        Bitmap bm = map;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        byte[] bArray = bs.toByteArray();
        return Base64.encodeToString(bArray,Base64.DEFAULT);
    }

    public Bitmap fromBas64(String input){
        return BitmapFactory.decodeByteArray(Base64.decode(input, 0), 0, Base64.decode(input, 0).length);
    }
}
