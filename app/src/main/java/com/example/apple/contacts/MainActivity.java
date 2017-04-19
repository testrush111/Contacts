package com.example.apple.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textWindows;
    Button btn_save, btn_return;
    String filename = "test2";
    JSONArray jsonId = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 5.0 x86_64 API21
        final SDCard sdCard = new SDCard(filename);
        listView = (ListView)findViewById(R.id.listview1);
        textWindows = (TextView)findViewById(R.id.textView1);
        btn_save = (Button) findViewById(R.id.btn_savaSD);
        btn_return = (Button) findViewById(R.id.btn_returnContacts);
        Context cntx = getApplicationContext();
        final Contacts contacts = new Contacts(MainActivity.this,getContentResolver(),listView);
        try {
            //顯示聯絡人
            contacts.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonMainArr = null;
                try {
                    jsonMainArr = contacts.jsonContacts.getJSONArray("contacts");
                    //取得聯絡人資訊並且還原
                    contacts.WritePhoneContact(jsonMainArr.getJSONObject(0).getString("name"),"4467",MainActivity.this);
                    //備份後畫面更新
                    try {
                        contacts.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,"還原成功！",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //將聯絡人存入SD卡備份
                if(sdCard.isSDWriteable()) {
                    sdCard.SDwrite(contacts.jsonContacts.toString());
                    Toast.makeText(MainActivity.this,"備份成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}