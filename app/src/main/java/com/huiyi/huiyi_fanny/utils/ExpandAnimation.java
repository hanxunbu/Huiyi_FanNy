package com.huiyi.huiyi_fanny.utils;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.huiyi.huiyi_fanny.R;

/**
 * Created by lw on 2017/10/27.
 * 展开收起某个隐藏布局
 *
 */

public class ExpandAnimation extends Animation {
    private View mAnimatedView;
    private LayoutParams mViewLayoutParams;
    private int mMarginStart, mMarginEnd;
    private boolean mIsVisibleAfter = false;
    private boolean mWasEndedAlready = false;

    private ImageView arrow;
    /**
     * Initialize the animation
     * @param view The layout we want to animate
     * @param duration The duration of the animation, in ms
     */
    public ExpandAnimation(View open, View view, int duration) {

        setDuration(duration);
        arrow = (ImageView) open;
        mAnimatedView = view;
        mViewLayoutParams = (LayoutParams) view.getLayoutParams();

        // if the bottom margin is 0,
        // then after the animation will end it'll be negative, and invisible.
        mIsVisibleAfter = (mViewLayoutParams.bottomMargin == 0);

        //根据状态获取动画起始参数
        mMarginStart = mIsVisibleAfter ? 0:(0- view.getHeight());
        mMarginEnd = (mMarginStart == 0 ? (0- view.getHeight()) : 0);

        arrow.setImageResource(R.drawable.text_ic_expand);
        mAnimatedView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {

            // Calculating the new bottom margin, and setting it
            mViewLayoutParams.bottomMargin = mMarginStart
                    + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

            // Invalidating the layout, making us seeing the changes we made
            mAnimatedView.requestLayout();

            // Making sure we didn't run the ending before (it happens!)
        } else if (!mWasEndedAlready) {
            mViewLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();

            if (mIsVisibleAfter) {
                mAnimatedView.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.text_ic_expand);
            }
            mWasEndedAlready = true;
        }
    }
}
