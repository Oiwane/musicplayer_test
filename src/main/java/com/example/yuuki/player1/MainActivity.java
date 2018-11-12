package com.example.yuuki.player1;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaPlayer mp;
    Button play_button;  //音楽再生・停止用ボタン
    TextView current_position;  //現在再生しているポジション
    TextView duration;  //音楽の再生時間

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_button = findViewById(R.id.button);
        current_position = findViewById(R.id.textView);
        duration = findViewById(R.id.textView2);
        play_button.setOnClickListener(this);

        if (mp != null) {
            current_position.setText(mp.getCurrentPosition());
            duration.setText(mp.getDuration());
            if (mp.isPlaying()) {
                play_button.setText(R.string.stop);
            }
        } else {
            play_button.setText(R.string.start);
        }

    }

        // リスナーをボタンに登録
        //play_button.setOnClickListener(new View.OnClickListener() {
            //@Override
    public void onClick(View v) {
        if (mp != null && mp.isPlaying()) {  //再生中
            play_button.setText(R.string.start);  //ボタンの文字をSTARTに
            mp.pause();  //一時停止
            //audioStop();
        } else {  //停止中
            play_button.setText(R.string.stop);  //ボタンの文字をSTOPに
            audioPlay();
            /*try {
            audioPlay();  //再生
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
        //});


    private boolean audioSetup(){
        boolean fileCheck = false;

        // 曲データの受け取り
        mp = MediaPlayer.create(this, R.raw.sample);

        // 音量調節を端末のボタンでする。
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        fileCheck = true;

        return fileCheck;
    }

    private void audioPlay() {
        //繰返し再生
        /*mp.stop();
            mp.reset();
            //リソースの解放
            mp.release();*/
        if(mp == null) {
            // audio ファイルを読み出しする。
            if (audioSetup()) {
                Toast.makeText(getApplication(), "read audio file", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            try{  //再生準備
                mp.prepare();
            } catch (IllegalStateException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        mp.start();

        //終了の検知
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("debug", "end of audio");
                audioStop();
            }
        });
    }

    // 曲の停止
    private void audioStop(){
        mp.stop();
        mp.reset();
        mp.release();
        mp = null;
    }
}
