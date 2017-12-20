package com.example.john.qciandroidapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.john.qciandroidapp.models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.widget.AdapterView.*;


public class Interview extends AppCompatActivity {
    public static final int MEDIA_TYPE_VIDEO = 2;
    SharedPreferences settings;
    final int TAKE_VIDEO_CAMERA_REQUEST = 200;
    Button record, delete;
    VideoView mVideoView;
    ArrayList<String> mQuestionsText;
    Uri fileUri;
    AsyncTask<String, String, List<String>> execute;
    ArrayList<String> mStringArray;
    public Spinner mQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.interview);
        mQuestions = (Spinner) findViewById(R.id.spinner);
        record = (Button) findViewById(R.id.record);
        delete = (Button) findViewById(R.id.delete);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mStringArray = new ArrayList<>();

        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        execute = new JSONTask();
        execute.execute("http://careercentre.azurewebsites.net/api/InterviewQuestions");


        mQuestions.setOnItemSelectedListener(new OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String key = settings.getString(mQuestions.getSelectedItem().toString(), "");

                if (!key.equals(""))
                    mVideoView.setVideoURI(Uri.parse(key));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callVideoApp = new Intent();
                callVideoApp.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                callVideoApp.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // set the video image quality to high
                callVideoApp.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);


                startActivityForResult(callVideoApp, TAKE_VIDEO_CAMERA_REQUEST);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.start();
            }
        });


    }

    public  class JSONTask extends AsyncTask<String, String, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {

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

                mQuestionsText = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String question;
                    JSONObject finalObject = jsonArray.getJSONObject(i);
                   question = finalObject.getString("Question");

                    mQuestionsText.add(question);
                }

                return mQuestionsText;
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
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            setItemsOnList();
        }
    }


    public void setItemsOnList() {




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mQuestionsText);
        mQuestions.setAdapter(adapter);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_VIDEO_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Uri uri = Uri.parse(data.getStringExtra("uri"));


                mVideoView.setVideoURI(data.getData());
                // mVideoView.setVideoPath(uri);


            } else
                Toast.makeText(getApplicationContext(), "video not recorded", Toast.LENGTH_LONG).show();

        }
    }

    private Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {


        // Create the storage directory(MyCameraVideo) if it does not exist
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "");


        // Create the storage directory(MyCameraVideo) if it does not exist



        // Create a media file name
        String name = mQuestions.getSelectedItem().toString();
        // For unique file name appending current timeStamp with file name


        File mediaFile;
        // For unique video file name appending current timeStamp with file name
        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + name + ".mp4");
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(name, Uri.fromFile(mediaFile).toString());
            editor.commit();

        } else {
            return null;
        }

        return mediaFile;
    }

}

