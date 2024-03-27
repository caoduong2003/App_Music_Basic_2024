package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView txtTenBaiHat, txtTotalTime, txtTimeSong;
    ImageButton btnPrevious, btnNexts,btnStop,btnPlay;
    ImageView ivDisc;
    SeekBar seekBar;
    ArrayList<Song> arrayListSong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);
        addControl();
        createData();
        khoiTaoMediaPlayer();

        addEvent();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.disc_rotate);
    }

    @Override
    protected void onPause() {
        super.onPause();


        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("seekBarProgress", seekBar.getProgress());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Khôi phục trạng thái của SeekBar
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        int progress = prefs.getInt("seekBarProgress", 0); // Giá trị mặc định ở đây là 0
        System.out.println("process = "+ progress);
        khoiTaoMediaPlayer();
        seekBar.setProgress(progress);

    }

    private void addEvent(){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Neu dang phat thi cho phep pause
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }else{
                    //neu dang pause thi cho phep phat
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);

                }
                ivDisc.startAnimation(animation);
                SetTotalTime();
                UpdateTimeSong();

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play);
                khoiTaoMediaPlayer();
                ivDisc.clearAnimation();
            }
        });
        btnNexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;

                if(position>arrayListSong.size()-1){
                    position = 0;
                }
                // neu dang phat bai hat nao do thi dung
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                ivDisc.startAnimation(animation);
                SetTotalTime();
                UpdateTimeSong();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;

                if(position < 0 ){
                    position = arrayListSong.size() - 1;
                }
                // neu dang phat bai hat nao do thi dung
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                ivDisc.startAnimation(animation);
                SetTotalTime();
                UpdateTimeSong();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private  void khoiTaoMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this,arrayListSong.get(position).getFile());
        txtTenBaiHat.setText(arrayListSong.get(position).getTenbaihat());
    }
    private void createData(){
        arrayListSong = new ArrayList<>();
        arrayListSong.add(new Song("Chúng ta của hiện tại",R.raw.chungtacuahientai));
        arrayListSong.add(new Song("Chúng ta của tương lai",R.raw.chungtacuatuonglai));
        arrayListSong.add(new Song("Hãy trao cho anh",R.raw.haytraochoanh));
        arrayListSong.add(new Song("Nơi này có anh",R.raw.noinaycoanh));

    }

    private void addControl(){
        txtTenBaiHat = findViewById(R.id.txtTenBaiHat);
        txtTotalTime = findViewById(R.id.txtTotalTime);
        txtTimeSong = findViewById(R.id.txtTimeSong);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNexts = findViewById(R.id.btnNexts);
        btnStop = findViewById(R.id.btnStop);

        ivDisc = findViewById(R.id.ivDisc);
        seekBar = findViewById(R.id.seekBar);

    }

    private void SetTotalTime(){
        //getDuration tra ve tong tg bai hat: mili giay, dinh dang theo giay bang simpledateformat
        SimpleDateFormat fm = new SimpleDateFormat("mm:ss");
        txtTotalTime.setText(fm.format(mediaPlayer.getDuration())+"");
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void UpdateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //thoi gian bai hat dang phat getCurruntPosition
                SimpleDateFormat fm = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(fm.format(mediaPlayer.getCurrentPosition())+"");
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;

                        if(position>arrayListSong.size()-1){
                            position = 0;
                        }
                        // neu dang phat bai hat nao do thi dung
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        khoiTaoMediaPlayer();
                        mediaPlayer.start();
                        ivDisc.startAnimation(animation);
                        SetTotalTime();
                        UpdateTimeSong();
                    }
                });
                handler.postDelayed(this,500);

            }
        }, 500);
    }
}