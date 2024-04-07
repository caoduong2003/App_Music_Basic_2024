package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    TextView txtTenBaiHat, txtTotalTime, txtTimeSong;
    ImageButton btnPrevious, btnNexts,btnBack,btnPlay,btnff,btnfr;
    ImageView ivDisc;
    SeekBar seekBar;
    Animation animation;

    String sname;

    private static final String PREF_SONG_POSITION = "song_position";
    private static final String PREF_SEEK_POSITION = "seek_position";
    private static final String PREF_SONG_NAME = "song_name";

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekbar;
    String endTime;



    SharedPreferences sharedPreferences; // luu trang thai dang nhap

    SharedPreferences.Editor editor;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);
        // Initialize SharedPreferences
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = sharedPreferences.edit();

        sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
        editor = sharedPreferences.edit();



        btnPrevious = findViewById(R.id.btnPrevious);
        btnNexts = findViewById(R.id.btnNexts);
        btnPlay = findViewById(R.id.btnPlay);
        btnff = findViewById(R.id.btnFf);
        btnfr = findViewById(R.id.btnFr);

        txtTenBaiHat = findViewById(R.id.txtTenBaiHat);
        txtTimeSong = findViewById(R.id.txtTimeSong);
        txtTotalTime = findViewById(R.id.txtTotalTime);
        seekBar = findViewById(R.id.seekBar);

        ivDisc = findViewById(R.id.ivDisc);

        // Restore playback state from SharedPreferences

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();

        }



        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList)bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        txtTenBaiHat.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).getName();
        txtTenBaiHat.setText(sname);


        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);

        mediaPlayer.start();


//        if(mediaPlayer != null){
//            mediaPlayer.stop();
//            mediaPlayer.release();
//
//        }

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration ){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


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

        endTime = createTime(mediaPlayer.getDuration());
        txtTotalTime.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtTimeSong.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();

                    btnPlay.setImageResource(R.drawable.play);
                }else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause);

                }
//                ivDisc.startAnimation(animation);
            }
        });

        //next listener


        btnNexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtTenBaiHat.setText(sname);
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_pause);
                startAnimation(ivDisc);
                endTime = createTime(mediaPlayer.getDuration());
                txtTotalTime.setText(endTime);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                btnNexts.performClick();
            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)? (mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtTenBaiHat.setText(sname);
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_pause);
                startAnimation(ivDisc);

                endTime = createTime(mediaPlayer.getDuration());
                txtTotalTime.setText(endTime);

            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });
        


    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);


        // Retrieve saved song position and seekBar progress
        int songPosition = sharedPreferences.getInt(PREF_SONG_POSITION, 0);
        int seekPosition = sharedPreferences.getInt(PREF_SEEK_POSITION, 0);
        int position = sharedPreferences.getInt("thePosition",0);
        String savedSongName = sharedPreferences.getString(PREF_SONG_NAME, "");

        // Restore song information and seekBar progress
        if (!savedSongName.isEmpty()) {

            txtTenBaiHat.setText(sname);
            mediaPlayer.seekTo(songPosition);
            seekBar.setProgress(seekPosition);
            mediaPlayer.start();
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        // Save current song position and seekBar progress
        if (mediaPlayer != null) {
            sharedPreferences = getSharedPreferences("mediaPlaying", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            editor.putInt(PREF_SONG_POSITION, mediaPlayer.getCurrentPosition());
            editor.putInt(PREF_SEEK_POSITION, seekBar.getProgress());
            editor.putInt("thePosition",position);
            editor.putString(PREF_SONG_NAME, sname);
            editor.putBoolean("playing",true);
            editor.apply();
        }
    }


    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivDisc,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator);
        animatorSet.start();


    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if(sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }
}