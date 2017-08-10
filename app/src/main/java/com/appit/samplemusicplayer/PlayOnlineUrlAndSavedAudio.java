package com.appit.samplemusicplayer;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class PlayOnlineUrlAndSavedAudio extends Activity implements View.OnClickListener {
    private Button btnPause;
    private Button btnPlay,btnForward,btnBackward;
    boolean isPausing=false;
    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();

    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1;
    private TextView tx2,tx3;

    public static int oneTimeOnly = 0;

    Button buttonStop,buttonStart,btnPlayAudio ;
    View musicPlayerLayout;
    ProgressBar progressBar;

//    String AudioURL = "http://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3";
    String AudioURL = "http://naasongsdownload.com/Telugu/2017%20naasongs.audio/Baahubali%202%20-%20The%20Conclusion%20(2017)%20~128Kbps-Naasongs.Audio/01%20-%20Saahore%20Baahubali%20-Naasongs.Audio.mp3";

    MediaPlayer mediaplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_url);

        initViews();
        initClickListeners();



        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        seekbar.setMax(mediaplayer.getDuration());
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                changeSeekbarTime(view);
                return false;
            }
        });
    }

    private void initClickListeners() {
        btnPlayAudio.setOnClickListener(this);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
    }

    private void initViews() {

        btnPlayAudio=findViewById(R.id.btn_normal_audio);
        buttonStart = findViewById(R.id.button1);
        buttonStop = findViewById(R.id.button2);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        musicPlayerLayout=findViewById(R.id.music_player_layout);
        btnForward = findViewById(R.id.forward);
        btnPause = findViewById(R.id.pause);
        btnPlay = findViewById(R.id.play);
        btnBackward = findViewById(R.id.backward);

        tx1 = findViewById(R.id.textView2);
        tx2 = findViewById(R.id.textView3);
        tx3 = findViewById(R.id.textView4);
        seekbar = findViewById(R.id.seekBar);
        musicPlayerLayout.setVisibility(View.INVISIBLE);
        tx3.setText("Song.mp3");

    }

    private void changeSeekbarTime(View view) {
        if (mediaplayer.isPlaying()) {
            SeekBar sb = (SeekBar) view;
            mediaplayer.seekTo(sb.getProgress());
        }else if(isPausing){
            SeekBar sb=(SeekBar)view;
            mediaplayer.seekTo(sb.getProgress());
        }
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaplayer.getCurrentPosition();
            tx1.setText(String.format("%d : %d ",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btn_normal_audio:
                playSavedAudio();
                break;
            case R.id.button1:
                playUrlAudio();
                break;
            case R.id.button2:
                stopUrlAudio();
                break;
            case R.id.play:
                play();
                break;
            case R.id.pause:
                pause();
                break;
            case R.id.backward:
                backward();
                break;
            case R.id.forward:
                forward();
                break;

        }
    }

    private void play() {
        mediaplayer.start();

        finalTime = mediaplayer.getDuration();
        startTime = mediaplayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        tx2.setText(String.format("%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        tx1.setText(String.format("%d : %d ",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
        btnPause.setEnabled(true);
        btnPlay.setEnabled(false);
    }

    private void pause() {
        isPausing=true;
        Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
        mediaplayer.pause();
        btnPause.setEnabled(false);
        btnPlay.setEnabled(true);
        myHandler.postDelayed(UpdateSongTime, 100);
    }

    private void stopUrlAudio() {
        mediaplayer.stop();
        seekbar.setProgress((int) startTime);
        seekbar.setClickable(false);
        tx1.setText("0:0");
        musicPlayerLayout.setVisibility(View.INVISIBLE);
    }


    private void playSavedAudio() {
        mediaplayer = MediaPlayer.create(this, R.raw.bahubali_song);
        musicPlayerLayout.setVisibility(View.VISIBLE);
        play();
    }
    private void playUrlAudio() {
        mediaplayer=new MediaPlayer();
        // TODO Auto-generated method stub
        try {

            mediaplayer.setDataSource(AudioURL);
            mediaplayer.prepareAsync();
            progressBar.setVisibility(View.VISIBLE);

            mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.INVISIBLE);
                    mediaplayer.start();
                    musicPlayerLayout.setVisibility(View.VISIBLE);

                    if(mediaPlayer.isLooping()){
                        tx3.setText("loading....");
                    }
                    tx3.setText("song.mp3");
                    finalTime = mediaplayer.getDuration();
                    startTime = mediaplayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx2.setText(String.format("%d : %d ",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    tx1.setText(String.format("%d : %d ",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                    btnPause.setEnabled(true);
                    btnPlay.setEnabled(false);
                }
            });

        } catch (IllegalArgumentException | SecurityException | IOException | IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void forward() {
        int temp = (int) startTime;

        if ((temp + forwardTime) <= finalTime) {
            startTime = startTime + forwardTime;
            mediaplayer.seekTo((int) startTime);
            Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }
    private void backward() {
        int temp = (int) startTime;

        if ((temp - backwardTime) > 0) {
            startTime = startTime - backwardTime;
            mediaplayer.seekTo((int) startTime);
            Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
        }
    }



}
