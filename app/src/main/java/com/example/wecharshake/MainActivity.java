package com.example.wecharshake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;//传感器管理器
    private Sensor accelerometerSensor;//加速度传感器
    private boolean isShake=false;
    private final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        
                    }
                }).start();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
