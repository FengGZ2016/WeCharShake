package com.example.wecharshake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

/**
 * 作者：国富小哥
 * 日期：2017/6/14
 * Created by Administrator
 *
 * 用于监听加速度传感器的监听器
 */

public class ShakeListener implements SensorEventListener{
    private static final int SPEED_SHRESHOLD = 2000; // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int UPTATE_INTERVAL_TIME = 70; // 两次检测的时间间隔
    private SensorManager mSensorManager;//传感器管理器
    private Sensor accelerometerSensor;//加速度传感器
    private OnShakeListener mOnShakeListener;//摇晃监听接口
    private Context mContext;
    private long lastUpdateTime; // 上次检测时间
    // 手机上一个位置时加速度感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;


    public ShakeListener(Context mContext){
        this.mContext=mContext;
        start();
    }

    /**
     * 开始注册传感器
     * */
    public void start() {
        //获取传感器管理器
        mSensorManager= (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        if (mSensorManager!=null){
            //获取加速度传感器
            accelerometerSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor!=null){
                //为传感器注册监听事件(1,监听器接口 2，传感器对象 3，传感器数据变化的采样率)
                //SensorManager.SENSOR_DELAY_UI:延迟60秒
                //mSensorManager.registerListener((SensorEventListener) this,accelerometerSensor,SensorManager.SENSOR_DELAY_UI);
                mSensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);

            }else {
                Toast.makeText(mContext, "你的手机不支持此功能！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 停止检测
    public void stop() {
        mSensorManager.unregisterListener(this);
    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();	// 当前检测时间
        long timeInterval = currentUpdateTime - lastUpdateTime;		// 两次检测的时间间隔
        // 判断是否达到了检测时间间隔
        //时间间隔要大于70
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        // 达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {
            mOnShakeListener.onShake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    // 摇晃监听接口
    public interface OnShakeListener {
        //开始震动了
        public void onShake();
    }

    // 设置重力感应监听器

    public void setOnShakeListener(OnShakeListener listener) {
        mOnShakeListener = listener;
    }
}
