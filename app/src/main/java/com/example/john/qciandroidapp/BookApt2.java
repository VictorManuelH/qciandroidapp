package com.example.john.qciandroidapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 19/12/2017.
 */
public class BookApt2 extends AppCompatActivity implements View.OnClickListener {

    private String TAG = BookApt2.class.getSimpleName();
    private AsyncTask<String, String, String> execute;
    private ProgressDialog pDialog;
    public ListView lv;
    private String aptIdNum;
    private SharedPreferences settings;

    // URL to get contacts JSON
    private static String url = "http://careercentre.azurewebsites.net/api/ApptTemplates";

    ArrayList<HashMap<String, String>> aptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apt_book);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        this.setTitle("Appointments Currently Available");
        aptList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        lv.setClickable(true);


        new GetApts().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                HashMap<String, String> map = (HashMap<String, String>) o;
                String aptIdNum = map.get("id");
                if (aptIdNum != null) {
                    String userId = settings.getString("isUser","");
                    String makeApt = ("http://careercentre.azurewebsites.net/api/Appointments/register?userId="+userId+"&apptTemplateId="+aptIdNum);
                    execute = new BookApt();
                    execute.execute(makeApt);
                    Toast.makeText(getApplicationContext(), aptIdNum + "Appointment Booked!",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to Book!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    @Override
    public void onClick(View view) {
//        if (aptIdNum != null) {
//            String userId = settings.getString("isUser","");
//            String makeApt = ("http://careercentre.azurewebsites.net/api/Appointments/register?userId="+userId+"&apptTemplateId="+aptIdNum);
//            execute = new BookApt();
//            execute.execute(makeApt);
//            Toast.makeText(getApplicationContext(), aptIdNum + "Appointment Booked!",
//                    Toast.LENGTH_LONG).show();
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), "Unable to Book!",
//                    Toast.LENGTH_LONG).show();
//        }
    }

    private class GetApts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BookApt2.this);
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
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    // Getting JSON Array node

                    JSONArray advisors = new JSONArray((jsonStr));

                    // looping through All Advisors
                    for (int i = 0; i < advisors.length(); i++) {
                        JSONObject c = advisors.getJSONObject(i);
                        if (c.getString("Available").equals("Yes")) {
                            id = c.getString("Id");
                            day = c.getString("DoW");
                            name = c.getString("Advisor");
                            time = (day + " at " + c.getString("Time"));


                            // hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("time", time);

                            // adding times to list
                            aptList.add(contact);
                        }
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
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
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
                    BookApt2.this, aptList,
                    R.layout.list_item, new String[]{"id", "name",
                    "time"}, new int[]{R.id.id,
                    R.id.name, R.id.time});

            lv.setAdapter(adapter);
        }

    }


}