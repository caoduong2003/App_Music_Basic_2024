package com.example.appmusicbasic;

import static com.example.appmusicbasic.PlayerActivity.mediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SongRun extends AppCompatActivity {
    Button logout, feedback;
    boolean isPlaying;
    SharedPreferences sharedPreferences; // luu trang thai dang nhap

    SharedPreferences.Editor editor , editor1;

    private static final String PREF_SONG_POSITION = "song_position";
    private static final String PREF_SEEK_POSITION = "seek_position";
    TextView userID;
    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        listView = findViewById(R.id.listMusic);
        logout = findViewById(R.id.logout);


        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String usernameNew = getIntent().getStringExtra("username");

        userID = (TextView) findViewById(R.id.userID);

        if (!username.isEmpty() || !usernameNew.isEmpty() || username != null || usernameNew != null || !username.equals("null") || !usernameNew.equals("null") || !username.equals("") || !usernameNew.equals("")) {
            userID.setText(username);
            userID.setText(usernameNew);
        } else {
            userID.setText("User");

        }

        feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongRun.this, FeedbackSong.class);
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
                editor1.apply();
                Intent intent = new Intent(SongRun.this, Login.class);
                startActivity(intent);
            }
        });

        //luu vi tri bai hat dang phat
        sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
        isPlaying = sharedPreferences.getBoolean("playing", false);
        runtimePermission();

        if (isPlaying) {
//            Intent intent = new Intent(SongRun.this, PlayerActivity.class);
//            startActivity(intent);
//            finish();
//            return;

            String songName = sharedPreferences.getString("song_name", "");
            final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
            int position = sharedPreferences.getInt("thePosition", 0);

            startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                    .putExtra("songs", mySong)
                    .putExtra("songname", songName)
                    .putExtra("pos", position));

        }


    }


    public void runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();


    }


    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();


        if (files != null) { // Thêm kiểm tra này để xử lý trường hợp files là null
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSong(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    void displaySong() {
        final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
        // Sắp xếp danh sách các tập tin theo kích thước từ nhỏ đến lớn
        Collections.sort(mySong, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return Long.compare(file1.length(), file2.length());
            }
        });
        items = new String[mySong.size()];
//        for (int i = 0; i < mySong.size(); i++) {
//            items[i] = mySong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
//
//        }
        for (int i = mySong.size()-1; i >= 0 ; i--) {
            items[i] = mySong.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");

        }


//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
//        listView.setAdapter(myAdapter);
        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = (String) listView.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                        .putExtra("songs", mySong)
                        .putExtra("songname", songName)
                        .putExtra("pos", position));
                sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.putBoolean("playing", false);
                editor.putInt(PREF_SONG_POSITION, 0);
                editor.putInt(PREF_SEEK_POSITION, 0);
                editor.apply();
            }
        });

    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View myView = getLayoutInflater().inflate(R.layout.song, null);
            TextView textsong = myView.findViewById(R.id.txtSong);
            textsong.setSelected(true);
            textsong.setText(items[position]);
            return myView;
        }
    }
}