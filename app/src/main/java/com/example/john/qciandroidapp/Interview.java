package com.example.john.qciandroidapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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


import java.io.File;

import java.util.ArrayList;
import java.util.Objects;

import static android.widget.AdapterView.*;


public class Interview extends AppCompatActivity {
    public static final int MEDIA_TYPE_VIDEO = 2;
    SharedPreferences settings;
    final int TAKE_VIDEO_CAMERA_REQUEST = 200;
    Button record, delete;
    VideoView mVideoView;
    TextView dataText;
    Uri fileUri;
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
        setItemsOnList();


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

    public void setItemsOnList() {

        mStringArray = new ArrayList<>();
        mStringArray.add("Tell me about yourself");
        mStringArray.add("Why we should hire you");
        mStringArray.add("What are your Strength");
        mStringArray.add("What are your Weaknesses");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mStringArray);
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
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {


                Toast.makeText(this.getApplicationContext(), "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


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

