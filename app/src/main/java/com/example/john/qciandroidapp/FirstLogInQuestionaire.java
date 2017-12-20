package com.example.john.qciandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FirstLogInQuestionaire extends AppCompatActivity {
    Spinner faculty, semester, campus;
    private SharedPreferences settings;
    private Intent intent;
    private AsyncTask<String, String, String> execute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_log_in_questionaire);

        intent = new Intent(this, MainMenu.class);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        faculty = (Spinner)findViewById(R.id.faculty);
        campus = (Spinner)findViewById(R.id.campus);
        semester = (Spinner)findViewById(R.id.semester);

        setItemsOnList();
    }
    public void setItemsOnList() {
        ArrayList facult;
        facult = new ArrayList<>();
        facult.add("FAAD");
        facult.add("FAHCS");
        facult.add("FAST");
        facult.add("FPSB");
        facult.add("FHSS");
        facult.add("FCPS");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, facult);
        faculty.setAdapter(adapter);
        ArrayList campusName;
        campusName = new ArrayList<>();
        campusName.add("Davis");
        campusName.add("Trafalgar");
        campusName.add("Mississaugua");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campusName);
        campus.setAdapter(adapter);

        ArrayList semesterNumber;
        semesterNumber = new ArrayList<>();

        semesterNumber.add("1");
        semesterNumber.add("2");
        semesterNumber.add("3");
        semesterNumber.add("4");
        semesterNumber.add("5");
        semesterNumber.add("6");
        semesterNumber.add("7");
        semesterNumber.add("8");


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semesterNumber);
        semester.setAdapter(adapter);


    }

    public void complete(View view) {
        execute = new JSONTask();
        String campusSelected, facultySelected;
        int semesterSelected,userId;
        campusSelected = campus.getSelectedItem().toString();
        facultySelected = faculty.getSelectedItem().toString();
        semesterSelected = Integer.parseInt(semester.getSelectedItem().toString());
        userId = Integer.parseInt(settings.getString("isUser",""));
        execute.execute("http://careercentre.azurewebsites.net/api/Users/AddDetails?id="+userId+"&campus="+campusSelected+"&faculty="+facultySelected+"&semester="+semesterSelected);
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

            if(result != null) {

                SharedPreferences.Editor editor = settings.edit();

                editor.putString("completedQuestionaire", "yes");
                editor.commit();
                startActivity(intent);
                finish();
            }





        }
    }

}
