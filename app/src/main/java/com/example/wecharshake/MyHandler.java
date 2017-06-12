package com.example.wecharshake;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 作者：国富小哥
 * 日期：2017/6/12
 * Created by Administrator
 */

public class MyHandler extends Handler{

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
            
        }
    }
}
