package com.example.wecharshake;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;//传感器管理器
    private Sensor accelerometerSensor;//加速度传感器
    private boolean isShake=false;
    private final String TAG="MainActivity";
    private MyHandler mMyHandler;

//    private LinearLayout mLinearLayout_top;
//    private LinearLayout mLinearLayout_bom;
//    private ImageView mImageView_top_line;
//    private ImageView mImageView_bom_line;
    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;

    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效
    private Map<Integer, Integer> loadSound;


    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置只竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        init();
        mMyHandler=new MyHandler(this);


    }

    private void init() {
//        mLinearLayout_top= (LinearLayout) findViewById(R.id.main_linear_top);
//        mLinearLayout_bom= (LinearLayout) findViewById(R.id.main_linear_bottom);
//        mImageView_top_line= (ImageView) findViewById(R.id.main_shake_top_line);
//        mImageView_bom_line= (ImageView) findViewById(R.id.main_shake_bottom_line);
//
//        //顶部和底部横线不可见
//        mImageView_top_line.setVisibility(View.GONE);
//        mImageView_bom_line.setVisibility(View.GONE);
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
        //获取传感器管理器
        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager!=null){
            //获取加速度传感器
            accelerometerSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor!=null){
                //为传感器注册监听事件(1,监听器接口 2，传感器对象 3，传感器数据变化的采样率)
                //SensorManager.SENSOR_DELAY_UI:延迟60秒
                mSensorManager.registerListener((SensorEventListener) this,accelerometerSensor,SensorManager.SENSOR_DELAY_UI);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销传感器
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type=event.sensor.getType();
        if (type==Sensor.TYPE_ACCELEROMETER){
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if ((Math.abs(x)>17||Math.abs(y)>17||Math.abs(z)>17)&&!isShake){
                //开始摇了
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"onSensorChanged 开始摇了");

                        try {
                            //开始震动 发出提示音 展示动画效果
                            mMyHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            mMyHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mMyHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static class MyHandler extends Handler {

        private WeakReference<MainActivity> mReference;
        private MainActivity mMainActivity;

        public MyHandler(MainActivity activity){
            mReference=new WeakReference<MainActivity>(activity);
            if (mReference!=null){
                mMainActivity=mReference.get();
            }

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_SHAKE:
                    //开始震动 发出提示音 展示动画效果
                  mMainActivity.mVibrator.vibrate(300);//震动0.3秒
                    //发出提示音
                    mMainActivity.mSoundPool.play(mMainActivity.mWeiChatAudio,1,1,0,0,1);
                    //顶部与底部线条可见
                    mMainActivity.mImageView_bom_line.setVisibility(View.GONE);
                    mMainActivity.mImageView_top_line.setVisibility(View.GONE);

                    break;
                case AGAIN_SHAKE:
                    //再来一次震动提示
                    mMainActivity.mVibrator.vibrate(300);//震动0.3秒
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    mMainActivity.isShake = false;
                    // 展示上下两种图片回来的效果
                    mMainActivity.startAnimation(true);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //顶部的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        //大家一定不要忘记, 当要回来时, 我们中间的两根线需要GONE掉
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    mImageView_top_line.setVisibility(View.GONE);
                    mImageView_bom_line.setVisibility(View.GONE);
                }
            });
        }
        //设置动画
        mLinearLayout_top.startAnimation(topAnim);
        mLinearLayout_bom.startAnimation(bottomAnim);

    }


}
