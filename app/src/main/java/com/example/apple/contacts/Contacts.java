package com.example.apple.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 2017/4/17.
 */

public class Contacts {
    public JSONObject jsonContacts = new JSONObject();
    private ContentResolver resolver;
    private final Context context;
    private ArrayAdapter<String> listAdapter;
    private String id
            ,name
            ,email
            ,phoneNumber;
    private JSONArray jsonId;
    private ListView listView;
    Contacts(Context context, ContentResolver resolver, ListView listView){
        this.context = context;
        this.resolver = resolver;
        this.listView = listView;
    }
    public JSONArray getJsonId(){
        return jsonId;
    }
    public void show() throws JSONException {
        Cursor cursor =
                resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        String allData = "";
        jsonId = new JSONArray();
        while(cursor.moveToNext()){
            JSONObject jsonObject = new JSONObject();
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phoneCursor =
                    resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
                            ,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[]{id},null);
            /*store id and name*/
            Log.i("MY INFO:",id+"="+name);
            allData += id+"="+name;
//            textWindows.append(id);
//            textWindows.append(name);
            while(phoneCursor.moveToNext()){
                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("MY INFO:",phoneNumber);
                /*store phoneNumber*/
                allData += phoneNumber;
//                textWindows.append(phoneNumber);
            }

            Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",new String[]{id},null);
            while (emailCursor.moveToNext()){
                email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                Log.i("MY INFO:",email);
                /*store email*/

                allData += email;
//                textWindows.append(name);
            }
            try {
                jsonObject.put("name",name);
                jsonObject.put("phoneNumber",phoneNumber);
                jsonObject.put("email",email);
                jsonId.put(jsonObject);
                jsonContacts.put("contacts",jsonId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] list = new String[jsonId.length()];
        //parse Json
        for (int i=0;i<jsonId.length();i++) {
            String info="";
            String post_name = jsonId.getJSONObject(i).getString("name");
            info+=post_name;
            String post_phoneNumber = jsonId.getJSONObject(i).getString("phoneNumber");
            info+=post_phoneNumber;
//            String post_email = jsonId.getJSONObject(i).getString("email");
//            info+=post_email;
            list[i] = info;
        }


        listAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);


    }
    public void WritePhoneContact(String displayName, String number,Context cntx /*App or Activity Ctx*/)
    {
        Context contetx 	= cntx; //Application's context or Activity's context
        String strDisplayName 	=  displayName; // Name of the Person to add
        String strNumber 	=  number; //number of the person to add with the Contact

        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
     // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());
        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        try
        {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
        }
        catch (RemoteException exp)
        {
            //logs;
        }
        catch (OperationApplicationException exp)
        {
            //logs
        }
    }
}
