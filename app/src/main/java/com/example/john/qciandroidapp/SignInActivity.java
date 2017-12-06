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

public class SignInActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private Intent intent;
    private EditText email, pass, rePass;
    private AsyncTask<String, String, String> execute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        intent = new Intent(this, FirstLogInQuestionaire.class);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        email = (EditText) findViewById(R.id.signUpEmail);
        pass = (EditText) findViewById(R.id.signUpPass);
        rePass = (EditText) findViewById(R.id.signUpRePass);
    }

    public void SignupBtn(View view) {
        execute = new JSONTask();
        String emailpa, password,rePassword;
        emailpa = email.getText().toString() + "@sheridancollege.ca";
        password = pass.getText().toString();
        rePassword = rePass.getText().toString();
        if(password.equals(rePassword)){


            execute.execute("http://careercentre.azurewebsites.net/api/Users/register?email=" + emailpa + "&password=" + password);
        }else{
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

        }

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

            if(result !="-1") {
                Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("isUser", result);
                editor.putString("completedQuestionaire", "no");
                editor.commit();
                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Your Email and/or Password does not match with our system", Toast.LENGTH_LONG).show();





        }
    }

}
