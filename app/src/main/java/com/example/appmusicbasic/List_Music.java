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
    Button logout, feedback;
    SharedPreferences sharedPreferences; // luu trang thai dang nhap
    SharedPreferences.Editor editor, editor1;
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
//        if (!username.isEmpty()) {
//            userID.setText(username);
//        } else {
//            userID.setText("User");
//        }

        listMusic = findViewById(R.id.listMusic);

        feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_Music.this, FeedbackSong.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
                editor1 = sharedPreferences.edit();
                editor.remove("Login");
                editor.remove("username");
                editor.remove("password");
                editor1.remove("song_position");
                editor1.remove("seek_position");
                editor1.remove("playing");
                editor1.remove("song_name");
                editor1.remove("thePosition");


                editor.apply();
                Intent intent = new Intent(List_Music.this, Login.class);
                startActivity(intent);
            }
        });

    }
}