package com.example.wecharshake;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

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
        TranslateAnimation start2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        start2.setDuration(1000);
        TranslateAnimation start3 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        start3.setDuration(1000);
        start3.setStartOffset(1000);
        animationSetDn.addAnimation(start2);
        animationSetDn.addAnimation(start3);
        mImgDn.startAnimation(animationSetDn);
    }

}
