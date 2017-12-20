package com.example.john.qciandroidapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 19/12/2017.
 */

public class CheckApt extends AppCompatActivity {

    private String TAG = CheckApt.class.getSimpleName();
    private AsyncTask<String, String, String> execute;
    private ProgressDialog pDialog;
    public ListView lv;
    private String aptIdNum;
    private SharedPreferences settings;
    public TextView tv;

    // URL to get contacts JSON
    private static String url = "http://careercentre.azurewebsites.net/api/Appointments/ApptsByUser?userId=";
    public static String userUrl;

    ArrayList<HashMap<String, String>> aptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apt_book);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        TextView tv = (TextView)findViewById(R.id.noApts);
        aptList = new ArrayList<>();
        this.setTitle("Currently Booked Appointments");
        lv = (ListView) findViewById(R.id.list);
//        lv.setClickable(true);
        String userId = settings.getString("isUser","");

        userUrl = url +  userId;


        new CheckApt.GetApts().execute();
    }

    private class GetApts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CheckApt.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String id = "";
            String day = "";
            String name = "";
            String time = "";


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(userUrl);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
//                    JSONObject j = new JSONObject(jsonStr);
//                    Toast.makeText(getApplicationContext(), "" + jsonStr,
//                            Toast.LENGTH_LONG).show();

                    // Getting JSON Array node

                    JSONArray advisors = new JSONArray((jsonStr));
//
//                    // looping through All Advisors
                    for (int i = 0; i < advisors.length(); i++) {
                        JSONObject c = advisors.getJSONObject(i);

                            id = c.getString("Id");
//                            day = c.getString("DoW");
                            name = c.getString("Advisor");
                            time = c.getString("Time");
//
//
//                            // hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();
//
//                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("time", time);
//
//                            // adding times to list
                            aptList.add(contact);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {

                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
//                        tv.setVisibility(View.VISIBLE);
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    CheckApt.this, aptList,
                    R.layout.list_item, new String[]{"id", "name",
                    "time"}, new int[]{R.id.id,
                    R.id.name, R.id.time});

            lv.setAdapter(adapter);
        }

    }
}
