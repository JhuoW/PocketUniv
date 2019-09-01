package com.pocketuniversity.ab.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AbAnimationUtil {

	/** 定义动画的时�?. */
	public final static long aniDurationMillis = 1L;

	/**
	 * 用来改变当前选中区域的放大动画效�? �?1.0f放大1.2f倍数
	 * 
	 * @param view
	 *            the view
	 * @param scale
	 *            the scale
	 */
	public static void largerView(View view, float scale) {
		if (view == null)
			return;

		// 置于�?有view�?上层
		view.bringToFront();
		int width = view.getWidth();
		float animationSize = 1 + scale / width;
		scaleView(view, animationSize);
	}

	/**
	 * 用来还原当前选中区域的还原动画效�?.
	 * 
	 * @param view
	 *            the view
	 * @param scale
	 *            the scale
	 */
	public static void restoreLargerView(View view, float scale) {
		if (view == null)
			return;
		int width = view.getWidth();
		float toSize = 1 + scale / width;
		// �?1.2f缩小1.0f倍数
		scaleView(view, -1 * toSize);
	}

	/**
	 * 缩放View的显�?.
	 * 
	 * @param view
	 *            �?要改变的View
	 * @param toSize
	 *            缩放的大小，其中正�?�代表放大，负�?�代表缩小，数�?�代表缩放的倍数
	 */
	private static void scaleView(final View view, float toSize) {
		ScaleAnimation scale = null;
		if (toSize == 0) {
			return;
		} else if (toSize > 0) {
			scale = new ScaleAnimation(1.0f, toSize, 1.0f, toSize,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			scale = new ScaleAnimation(toSize * (-1), 1.0f, toSize * (-1),
					1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		scale.setDuration(aniDurationMillis);
		scale.setInterpolator(new AccelerateDecelerateInterpolator());
		scale.setFillAfter(true);
		view.startAnimation(scale);
	}

	/**
	 * 跳动-跳起动画.
	 * 
	 * @param view
	 *            the view
	 * @param offsetY
	 *            the offset y
	 */
	private void playJumpAnimation(final View view, final float offsetY) {
		float originalY = 0;
		float finalY = -offsetY;
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new TranslateAnimation(0, 0, originalY,
				finalY));
		animationSet.setDuration(300);
		animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
		animationSet.setFillAfter(true);

		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				playLandAnimation(view, offsetY);
			}
		});

		view.startAnimation(animationSet);
	}

	/**
	 * 跳动-落下动画.
	 * 
	 * @param view
	 *            the view
	 * @param offsetY
	 *            the offset y
	 */
	private void playLandAnimation(final View view, final float offsetY) {
		float originalY = -offsetY;
		float finalY = 0;
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new TranslateAnimation(0, 0, originalY,
				finalY));
		animationSet.setDuration(200);
		animationSet.setInterpolator(new AccelerateInterpolator());
		animationSet.setFillAfter(true);

		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 两秒后再�?
				view.postDelayed(new Runnable() {

					@Override
					public void run() {
						playJumpAnimation(view, offsetY);
					}
				}, 2000);
			}
		});

		view.startAnimation(animationSet);
	}

	/**
	 * 旋转动画
	 * 
	 * @param v
	 * @param durationMillis
	 * @param repeatCount
	 *            Animation.INFINITE
	 * @param repeatMode
	 *            Animation.RESTART
	 */
	public static void playRotateAnimation(View v, long durationMillis,
			int repeatCount, int repeatMode) {

		// 创建AnimationSet对象
		AnimationSet animationSet = new AnimationSet(true);
		// 创建RotateAnimation对象
		RotateAnimation rotateAnimation = new RotateAnimation(0f, +360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画持续
		rotateAnimation.setDuration(durationMillis);
		rotateAnimation.setRepeatCount(repeatCount);
		// Animation.RESTART
		rotateAnimation.setRepeatMode(repeatMode);
		// 动画插入�?
		rotateAnimation.setInterpolator(v.getContext(),
				android.R.anim.decelerate_interpolator);
		// 添加到AnimationSet
		animationSet.addAnimation(rotateAnimation);

		// �?始动�?
		v.startAnimation(animationSet);
	}

}
