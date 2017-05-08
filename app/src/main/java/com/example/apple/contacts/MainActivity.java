package com.example.apple.contacts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    ListView listView;
    String filename = "test";
    SDCard sdCard = null;
    Contacts contacts = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 5.0 x86_64 API21
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(ProgressBar.GONE);
        sdCard = new SDCard(filename);
        listView = (ListView)findViewById(R.id.listview1);
        contacts = new Contacts(MainActivity.this,getContentResolver(),listView);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new task_reflash().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new task_reflash().execute();
    }

    public void btn_savaSD(View view){
        //將聯絡人存入SD卡備份
        if(sdCard.isSDWriteable()) {
            sdCard.SDwrite(contacts.jsonContacts.toString());
            Toast.makeText(MainActivity.this,"備份成功！",Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_returnContacts(View view){
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new task_return().execute();
    }

    public class task_reflash extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //顯示聯絡人
                contacts.recovery();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            contacts.show();

        }
    }
    public class task_return extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonMainArr = null;
            try {
                jsonMainArr = sdCard.getJsonId().getJSONArray("contacts");
                //取得聯絡人資訊並且還原
                for(int i=0;i<jsonMainArr.length();i++){
                    contacts.WritePhoneContact(jsonMainArr.getJSONObject(i).getString("name"),
                            jsonMainArr.getJSONObject(i).getString("phoneNumber"),MainActivity.this);
                }
                //備份後畫面更新
                contacts.recovery();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            contacts.show();
            Toast.makeText(MainActivity.this,"還原成功！",Toast.LENGTH_SHORT).show();
        }
    }
}