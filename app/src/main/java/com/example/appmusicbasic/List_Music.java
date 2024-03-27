package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class List_Music extends AppCompatActivity {
    Button logout;
    SharedPreferences sharedPreferences; // luu trang thai dang nhap

    TextView userID;
    ListView listMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        logout = findViewById(R.id.logout);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", "");
        String username = getIntent().getStringExtra("username");
        userID = (TextView) findViewById(R.id.userID);
        if (!username.isEmpty()) {
            userID.setText(username);
        } else {
            userID.setText("User");
        }

        listMusic = findViewById(R.id.listMusic);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("Login");
                editor.remove("username");
                editor.remove("password");


                editor.apply();
                Intent intent = new Intent(List_Music.this, Login.class);
                startActivity(intent);
            }
        });

    }
}