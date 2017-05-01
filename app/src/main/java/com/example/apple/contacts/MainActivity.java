package com.example.apple.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    //測試commit
    ListView listView;
    String filename = "test";
    SDCard sdCard = null;
    Contacts contacts = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 5.0 x86_64 API21
        sdCard = new SDCard(filename);
        listView = (ListView)findViewById(R.id.listview1);
        contacts = new Contacts(MainActivity.this,getContentResolver(),listView);
        try {
            //顯示聯絡人
            contacts.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btn_savaSD(View view){
        //將聯絡人存入SD卡備份
        if(sdCard.isSDWriteable()) {
            sdCard.SDwrite(contacts.jsonContacts.toString());
            Toast.makeText(MainActivity.this,"備份成功",Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_returnContacts(View view){
        JSONArray jsonMainArr = null;
        try {
            jsonMainArr = sdCard.getJsonId().getJSONArray("contacts");
//                    jsonMainArr = contacts.jsonContacts.getJSONArray("contacts");
            //取得聯絡人資訊並且還原
            for(int i=0;i<jsonMainArr.length();i++){
                contacts.WritePhoneContact(jsonMainArr.getJSONObject(i).getString("name"),
                        jsonMainArr.getJSONObject(i).getString("phoneNumber"),MainActivity.this);
            }
            //備份後畫面更新
            contacts.show();
            Toast.makeText(MainActivity.this,"還原成功！",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}