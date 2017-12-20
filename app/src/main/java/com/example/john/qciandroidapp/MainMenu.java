package com.example.john.qciandroidapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.Toolbar.OnMenuItemClickListener;

public class MainMenu extends Activity implements OnMenuItemClickListener {

    //EditText txtMsg;
    Toolbar toolbar;
    Intent intent;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
//        txtMsg = (EditText) findViewById(R.id.txtMsg);
        settings = getSharedPreferences("SETTINGS_PREFS", Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sheridan");
        toolbar.setSubtitle("Career Centre App");
        toolbar.inflateMenu(R.menu.main);
        //toolbar.setLogo(R.drawable.sher_tag);
        toolbar.setOnMenuItemClickListener(this);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String page;
                switch(position) {
                    case 0:
                        page = "Events";
                        break;
                    case 1:
                        page = "Appointments";
                        break;
                    case 2:
                        page = "Jobs";
                        break;
                    case 3:
                        page = "Interview";
                        break;
                    case 4:
                        page = "Resources";
                        break;
                    case 5:
                        page = "Settings";
                        break;
                    default:
                        page = "Default";
                        break;


                }

                Toast.makeText(MainMenu.this, "Going to " + page,
                        Toast.LENGTH_SHORT).show();
                Switch(page);

            }
        });
    }

    public void Switch(String page) {

        switch(page) {
            case "Appointments":
                intent = new Intent(this, Appointments.class);
                break;
            case "Events":
                intent = new Intent(this, Events.class);
                break;
            case "Jobs":
                intent = new Intent(this, Jobs.class);
                break;
            case "Interview":
                intent = new Intent(this, Interview.class);
                break;
            case "Resources":
                intent = new Intent(this, Resources.class);
                break;
            case "Settings":
                intent = new Intent(this, Settings.class);
                break;
            default:
                intent = new Intent(this, MainMenu.class);
        }

        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.button_back) {
          //  txtMsg.setText("Going back...");
            Toast.makeText(getApplicationContext(), "Going Back...",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.button_logout) {
            //txtMsg.setText("Logging out...");
            Toast.makeText(getApplicationContext(), "Logging Out...",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("isUser", null);
        editor.commit();
       intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
       finish();

    }

    public void back(View view) {
        finish();
        System.exit(0);
    }
}
