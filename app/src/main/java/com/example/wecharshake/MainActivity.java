package com.example.wecharshake;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private final String TAG="MainActivity";


    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;

    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效
    private Map<Integer, Integer> loadSound;//mp3文件
    private ShakeListener mShakeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置只竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        init();

        // 调用工具类方法把assets目录下的声音存放在map中，返回一个HashMap
        loadSound = Util.loadSound(mSoundPool, this);


    }

    private void init() {

        mImgUp= (RelativeLayout) findViewById(R.id.shakeImgUp);
        mImgDn= (RelativeLayout) findViewById(R.id.shakeImgDown);
        //初始化音效
        mSoundPool=new SoundPool(2, AudioManager.STREAM_SYSTEM,5);
        //获取震动服务
        mVibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakeListener=new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //开始震动了
                Util.startAnim(mImgUp, mImgDn); // 开始 摇一摇手掌动画
                mShakeListener.stop();// 停止加速度传感器
                mSoundPool.play(loadSound.get(0), (float) 1, (float) 1, 0, 0,
                        (float) 1.2);// 摇一摇时播放map中存放的第一个声音
                //打开震动
                startVibrato();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mSoundPool.play(loadSound.get(1), (float) 1, (float) 1, 0,
                                0, (float) 1.0);// 摇一摇结束后播放map中存放的第二个声音
                        Toast.makeText(MainActivity.this,   "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
                        mVibrator.cancel();// 震动关闭
                        mShakeListener.start();// 再次开始检测加速度传感器值
                    }
                }, 2000);
            }
        });
    }

    /**
     *  定义震动
     * */
    public void startVibrato() {
        mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销传感器
        if (mShakeListener!=null){
            mShakeListener.stop();
        }

    }

    public void shake_activity_back(View v) { // 标题栏 返回按钮
        this.finish();
    }

    public void linshi(View v) { // 标题栏
        Util.startAnim(mImgUp, mImgDn);
    }

}
