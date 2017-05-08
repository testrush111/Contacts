package com.example.apple.contacts;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by apple on 2017/4/17.
 */

public class SDCard {
    private String filename ,str;
    SDCard(String filename){
        this.filename = filename;
    }
    public JSONObject getJsonId() throws JSONException {
        showSD();
        JSONObject jsonObj = new JSONObject(str);
        return jsonObj;
    }
    private void showSD(){
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File f = new File(sdCard,filename);
            FileInputStream in = new FileInputStream(f);
            byte[] data = new byte[10240];
            in.read(data);
            in.close();

            str =new String(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SDwrite(String data){
        File sdCard = Environment.getExternalStorageDirectory();
        File f = new File(sdCard,filename);
        try{
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isSDWriteable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }else{
            return  false;
        }
    }
}