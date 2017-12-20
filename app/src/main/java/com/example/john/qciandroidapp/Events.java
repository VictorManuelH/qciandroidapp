package com.example.john.qciandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.qciandroidapp.models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import static android.view.View.VISIBLE;
import static android.widget.AdapterView.*;


/**
 * Created by victor on 9/19/2017.
 */

public class Events extends AppCompatActivity {
    TextView mText;
    ListView eventList;
    ProgressBar mProgressBar;
    Bundle bundle;
    int ud;
    List<EventModel> eventModelList;
    private ArrayList<String> names = new ArrayList<String>();
    AsyncTask<String, String, List<EventModel>> task;
    Intent inten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        mText = (TextView) findViewById(R.id.text);
        eventList = (ListView) findViewById(R.id.eventList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(VISIBLE);
        inten = new Intent(this, Register.class);
        task = new JSONTask().execute("http://careercentre.azurewebsites.net/api/Events");

        eventList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"you clicked on " + eventModelList.get(i).getName(), Toast.LENGTH_SHORT).show();
                inten.putExtra("EventId", eventModelList.get(i).getId());
                startActivity(inten);

            }
        });

    }


    public class JSONTask extends AsyncTask<String, String, List<EventModel>> {

        @Override
        protected List<EventModel> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                StringBuffer stringBuffer = new StringBuffer();


                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String finalJSON = stringBuffer.toString();

                JSONArray jsonArray = new JSONArray(finalJSON);

                eventModelList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    EventModel eventModel = new EventModel();
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                    eventModel.setId(finalObject.getInt("Id"));
                    eventModel.setTime(finalObject.getString("Time"));
                    eventModel.setName(finalObject.getString("Name"));
                    eventModel.setEvent(finalObject.getString("Description"));
                    eventModel.setLocation(finalObject.getString("Location"));

                    eventModelList.add(eventModel);
                }

                return eventModelList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<EventModel> result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);

            for (EventModel e :eventModelList) {
                names.add(e.getId() + "" + e.getName());
            }
           eventList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names));



        }
    }


}

