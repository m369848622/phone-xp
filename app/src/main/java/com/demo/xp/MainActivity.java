package com.demo.xp;

import static com.demo.xp.common.readFileString;
import static com.demo.xp.common.writeFileString;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity{// implements VolumeChangeObserver.VolumeChangeListener

//    private VolumeChangeObserver mVolumeChangeObserver;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            Log.i("sdfljksdlfk", "DOWN");
//        }
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            Log.i("sdfljksdlfk", "UP");
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    @Override
//    protected void onResume() {
//        //注册广播接收器
//        mVolumeChangeObserver.registerReceiver();
//        super.onResume();
//    }
//    @Override
//    protected void onPause() {
//        //解注册广播接收器
//        mVolumeChangeObserver.unregisterReceiver();
//        super.onPause();
//    }
//    @Override
//
//    public void onVolumeChanged(int volume) {
//        //系统媒体音量改变时的回调
//        Log.e("sdfsdf", "onVolumeChanged()--->volume = " + volume);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, PlayerService.class));
        super.onCreate(savedInstanceState);
//        mVolumeChangeObserver = new VolumeChangeObserver(this);
//        mVolumeChangeObserver.setVolumeChangeListener(this);
//        int initVolume = mVolumeChangeObserver.getCurrentMusicVolume();
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        Button bt = (Button) findViewById(R.id.button);
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        File file = new File(sdcardPath + "/phone.txt");
        Log.i("sldkjflksdf","555");
        if (!file.exists()) {// 判断文件目录是否存在
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bt.setText(readFileString(file));
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = ((TextView) findViewById(R.id.phone)).getText().toString();
                writeFileString(file, phone);
                bt.setText(phone);
            }
        });
    }


}
