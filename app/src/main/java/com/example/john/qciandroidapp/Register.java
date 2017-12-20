package com.example.john.qciandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.qciandroidapp.models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    AsyncTask<String, String, List<EventModel>> task;
    AsyncTask<String, String, String> register, unregister;
    List<EventModel> eventModelList;
    TextView name,desc,loc,time;
    Button reg, unReg;
    SharedPreferences settings;
    int userId, eventId;
    boolean registered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        task = new JSONTask();
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        name = (TextView)findViewById(R.id.txtName);
        desc =(TextView)findViewById(R.id.txtDesc);
        loc = (TextView)findViewById(R.id.txtLoc);
        time = (TextView)findViewById(R.id.txtTime);
        reg = (Button)findViewById(R.id.btnReg);
        unReg = (Button)findViewById(R.id.btnUnReg);
        userId = Integer.parseInt(settings.getString("isUser",""));
        eventId = getIntent().getIntExtra("EventId", 0);
        task.execute("http://careercentre.azurewebsites.net/api/Events/" + eventId);

        if (registered){

        }
    }

    public void registerEvent(View view) {
        register = new RegisterEvent();
        register.execute("http://careercentre.azurewebsites.net/api/Events/register?eventId="+eventId+"&userId="+ userId);
        registered = true;
        reg.setVisibility(View.INVISIBLE);
        unReg.setVisibility(View.VISIBLE);
    }

    public void unRegister(View view) {
        unregister = new UnRegisterEvent();
        unregister.execute("http://careercentre.azurewebsites.net/api/Events/unregister?eventId=" + eventId + "&userId=" + userId);
        registered = false;
        reg.setVisibility(View.VISIBLE);
        unReg.setVisibility(View.INVISIBLE);
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

                JSONObject jsonArray = new JSONObject(finalJSON);

                eventModelList = new ArrayList<>();


                    EventModel eventModel = new EventModel();

                    eventModel.setId(jsonArray.getInt("Id"));
                    eventModel.setTime(jsonArray.getString("Time"));
                    eventModel.setName(jsonArray.getString("Name"));
                    eventModel.setEvent(jsonArray.getString("Description"));
                    eventModel.setLocation(jsonArray.getString("Location"));

                    eventModelList.add(eventModel);


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


            addText();

        }
    }
    public class RegisterEvent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                StringBuffer stringBuffer = new StringBuffer();


                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String finalJSON = stringBuffer.toString();
                return finalJSON;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result !="-1") {


            }
            else
                Toast.makeText(getApplicationContext(), "Your Email and/or Password does not match with our system", Toast.LENGTH_LONG).show();





        }
    }
    public class UnRegisterEvent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                StringBuffer stringBuffer = new StringBuffer();


                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String finalJSON = stringBuffer.toString();
                return finalJSON;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result !="-1") {


            }
            else
                Toast.makeText(getApplicationContext(), "Your Email and/or Password does not match with our system", Toast.LENGTH_LONG).show();





        }
    }

    public void addText(){

        name.setText(eventModelList.get(0).getName());
        desc.setText(eventModelList.get(0).getEvent());
        loc.setText(eventModelList.get(0).getLocation());
        time.setText(eventModelList.get(0).getTime());
    }

}
