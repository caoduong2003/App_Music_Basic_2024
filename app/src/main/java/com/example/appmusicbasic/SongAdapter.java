package com.example.appmusicbasic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<Song>{
    Context context;
    private ArrayList<Song> song;



    public SongAdapter(@NonNull Context context, ArrayList<Song> dataArrayList) {
        super(context,R.layout.song,dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song currentSong = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song,parent,false);
        }

        ImageView listImage =convertView.findViewById(R.id.imgAvatar);
        TextView txtSong =convertView.findViewById(R.id.txtSong);
        TextView txtName =convertView.findViewById(R.id.txtName);


        listImage.setImageResource(currentSong.getImage());
        txtSong.setText(currentSong.getTenbaihat());
        txtName.setText(currentSong.getTencasi());



        return convertView;
    }
}
