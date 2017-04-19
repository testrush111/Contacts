package com.example.apple.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textWindows;
    Button btn1;
    String filename = "test2";
    JSONArray jsonId = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 5.0 x86_64 API21
        final SDCard sdCard = new SDCard(filename, MainActivity.this);
        listView = (ListView)findViewById(R.id.listview1);
        textWindows = (TextView)findViewById(R.id.textView1);
        btn1 = (Button) findViewById(R.id.button1);
        Context cntx = getApplicationContext();
        final Contacts contacts = new Contacts(MainActivity.this,getContentResolver(),listView);

        try {
            //顯示聯絡人
            contacts.show();
            JSONArray jsonMainArr = contacts.jsonContacts.getJSONArray("contacts");
            //取得聯絡人資訊並且還原
            contacts.WritePhoneContact(jsonMainArr.getJSONObject(0).getString("name"),"4467",MainActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*insert*/



        /*insert*/
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sdCard.isSDWriteable()) {
                    sdCard.SDwrite(contacts.jsonContacts.toString());
                }
            }
        });
    }
}