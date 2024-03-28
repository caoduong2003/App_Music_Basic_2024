package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ListSong extends AppCompatActivity {
    public String[] songName = {"bai1","bai2","bai3","bai4"};

    public String[] name = {"bai1","bai2","bai3","bai4"};

    public int[] image = {R.drawable.man,R.drawable.man,R.drawable.man,R.drawable.man};
    public  int[] file = {R.raw.chungtacuahientai,R.raw.haytraochoanh,R.raw.chungtacuatuonglai,R.raw.noinaycoanh};
    ListView listView;


    SongAdapter songAdapter;
    ArrayList<Song> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_song);

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        songAdapter = new SongAdapter(this,arrayList);

        for(int i = 0; i<name.length;i++){
            Song song = new Song(songName[i],name[i],image[i],file[i]);
            arrayList.add(song);
            songAdapter.notifyDataSetChanged();
        }

        listView.setAdapter(songAdapter);




    }
}