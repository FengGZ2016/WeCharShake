package com.example.wecharshake;

import android.content.Context;
import android.media.SoundPool;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：国富小哥
 * 日期：2017/6/12
 * Created by Administrator
 */

public class Util {
    /**
     * 开启动画
     * */
    public static void startAnim(RelativeLayout mImgUp, RelativeLayout mImgDn){
        //上面的图片的动画
        AnimationSet animationSetUp=new AnimationSet(true);
        //平移动画
        TranslateAnimation start0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        start0.setDuration(1000);
        TranslateAnimation start1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        start1.setDuration(1000);
        //延迟1秒
        start1.setStartOffset(1000);
        animationSetUp.addAnimation(start0);
        animationSetUp.addAnimation(start1);
        mImgUp.startAnimation(animationSetUp);

        //下面的图片的动画
        AnimationSet animationSetDn=new AnimationSet(true);
        TranslateAnimation end0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        end0.setDuration(1000);
        TranslateAnimation end1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        end1.setDuration(1000);
        end1.setStartOffset(1000);
        animationSetDn.addAnimation(end0);
        animationSetDn.addAnimation(end1);
        mImgDn.startAnimation(animationSetDn);
    }


    /**
     * 添加声音资源文件
     * */
    public static Map<Integer,Integer> loadSound(final SoundPool pool, final Context context){
        final Map<Integer,Integer> soundPoolMap=new HashMap<>();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        soundPoolMap.put(0,pool.load(context,R.raw.shake_sound_male,1));
                        soundPoolMap.put(1,pool.load(context,R.raw.shake_match,1));
                    }
                }
        ).start();
        return soundPoolMap;
    }

}
