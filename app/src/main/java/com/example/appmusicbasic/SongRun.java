package com.example.appmusicbasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongRun extends AppCompatActivity {
    ListView listView;
    String[] items;
    boolean isPlaying;
    SharedPreferences sharedPreferences; // luu trang thai dang nhap

    SharedPreferences.Editor editor;

    private static final String PREF_SONG_POSITION = "song_position";
    private static final String PREF_SEEK_POSITION = "seek_position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_run);

        listView = findViewById(R.id.listViewSong);
        sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
        isPlaying  = sharedPreferences.getBoolean("playing", false);
        runtimePermission();

        if (isPlaying) {
//            Intent intent = new Intent(SongRun.this, PlayerActivity.class);
//            startActivity(intent);
//            finish();
//            return;

            String songName = sharedPreferences.getString("song_name","");
            final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
            int position = sharedPreferences.getInt("thePosition",0);

            startActivity(new Intent(getApplicationContext(),PlayerActivity.class )
                    .putExtra("songs",mySong)
                    .putExtra("songname",songName)
                    .putExtra("pos",position));

        }







    }




//    @Override
//    public void onPermissionsChecked(@NonNull MultiplePermissionsReport report) {
//        if (report.areAllPermissionsGranted()) {
//            displaySong();
//        } else {
//            // Xử lý trường hợp quyền truy cập bị từ chối (ví dụ: hiển thị thông báo cho người dùng)
//            Toast.makeText(SongRun.this, "Quyền truy cập READ_EXTERNAL_STORAGE bị từ chối!", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void runtimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_MEDIA_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }


    public ArrayList<File> findSong(File file){
       ArrayList<File> arrayList = new ArrayList<>();
       File[] files = file.listFiles();



        if(files != null) { // Thêm kiểm tra này để xử lý trường hợp files là null
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

    void displaySong(){
        final ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySong.size()];
        for (int i = 0; i<mySong.size();i++){
            items[i] = mySong.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
//        listView.setAdapter(myAdapter);
          customAdapter customAdapter = new customAdapter();
            listView.setAdapter(customAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String songName = (String) listView.getItemAtPosition(position);
                    startActivity(new Intent(getApplicationContext(),PlayerActivity.class )
                            .putExtra("songs",mySong)
                            .putExtra("songname",songName)
                            .putExtra("pos",position));
                    sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    editor.putBoolean("playing",false);
                    editor.putInt(PREF_SONG_POSITION, 0);
                    editor.putInt(PREF_SEEK_POSITION, 0);
                    editor.apply();


                }
            });

    }

    class customAdapter extends BaseAdapter
    {

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

            View myView = getLayoutInflater().inflate(R.layout.song,null);
            TextView textsong = myView.findViewById(R.id.txtSong);
            textsong.setSelected(true);
            textsong.setText(items[position]);

            return  myView;
        }
    }
}