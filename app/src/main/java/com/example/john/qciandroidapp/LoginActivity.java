package com.example.john.qciandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private Intent intent, intentQuestionaire;
    private SharedPreferences settings;
    private EditText emailEtext, passwordEtext;
    private String password, email;
    private Boolean completed;
    private AsyncTask<String, String, String> execute;
    private AsyncTask<String, String, Boolean> check;
    String completedQuestionaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        String key = settings.getString("isUser", "");
        completedQuestionaire = settings.getString("completedQuestionaire", "");
        intent = new Intent(this, MainMenu.class);
        intentQuestionaire = new Intent(this, FirstLogInQuestionaire.class);


             if (!key.equals("")){
                startActivity(intent);
              finish();
            }


    }


    public void Process(View view) {
        emailEtext = (EditText)findViewById(R.id.email);
        passwordEtext = (EditText)findViewById(R.id.password);
        password = passwordEtext.getText().toString();
        email = emailEtext.getText().toString();
        execute = new JSONTask();


      execute.execute("http://careercentre.azurewebsites.net/api/login?email=" + email + "&password=" + password);

    }

    public void SignUp(View view) {
        intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

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

            if(result.equals("-1") || !result.equals(null)) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("isUser", result);
                editor.commit();
                check = new CheckQuestionaire();
                int id = Integer.parseInt(settings.getString("isUser", ""));
                // check.execute("http://careercentre.azurewebsites.net/api/Users/didCompleteSurvey?id=" + id);

                if (completedQuestionaire.equals("no")){
                    startActivity(intentQuestionaire);
                    finish();
                }else{
               startActivity(intent);
               finish();
                }
               }
            else
                Toast.makeText(getApplicationContext(), "Your Email and/or Password does not match with our system", Toast.LENGTH_LONG).show();





        }
    }

    public class CheckQuestionaire extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

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

                return Boolean.valueOf(stringBuffer.toString());
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
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result == true) {
            completed = true;
            }
            else
            completed = false;




        }
    }


}
