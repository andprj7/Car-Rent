package com.example.caronrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {
    SwitchCompat switchCompat,notify;
    boolean nightmode;
    ImageView back;
    SharedPreferences sharedPreferences;

    Button editbutton;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    String user,usern,userName,userEmail,userPassword;

    private DatabaseReference usersRef;
    private static final String SHARED_PREF_NAME = "mypref";
    SharedPreferences.Editor editor;
    TextView usen;
    RelativeLayout rv,sentmsg,aboutus,logout;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageView = findViewById(R.id.uid);
        usen = findViewById(R.id.uname);
        editbutton = findViewById(R.id.editprofile);
        switchCompat = findViewById(R.id.nnm);
        // Deva's code
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Ramani's code
        //sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("night", false); //light mode

        logout = findViewById(R.id.log_out);
        back = findViewById(R.id.imgback);
        rv = findViewById(R.id.privacy);
        sentmsg = findViewById(R.id.sentmessage);
        aboutus = findViewById(R.id.aboutus);
        notify = findViewById(R.id.notification);

        int Flag= getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean dark=Flag== Configuration.UI_MODE_NIGHT_YES;

        //dark mode
        if (dark) {
            switchCompat.setChecked(true);

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);

                }
                editor.apply();
            }
        });
        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(notify.isChecked()){
                    Toast.makeText(Settings.this, "Turn On Notification", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Settings.this, "Turn Off Notification", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,Home.class));
            }
        });


    }
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        startActivity(new Intent(Settings.this,Home.class));
    }
}