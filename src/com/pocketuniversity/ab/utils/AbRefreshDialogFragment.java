package com.pocketuniversity.ab.utils;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AbRefreshDialogFragment extends AbDialogFragment {

	private int mTheme;
	private int mStyle;
	private int mIndeterminateDrawable;
	private int mTextSize = 15;
	private int mTextColor = Color.WHITE;
	private View mContentView;
	private TextView mTextView = null;
	private ImageView mImageView = null;
	private int mBackgroundColor = Color.parseColor("#88838B8B");

	/**
	 * Create a new instance of AbDialogFragment, providing "style" as an
	 * argument.
	 */
	public static AbRefreshDialogFragment newInstance(int style, int theme) {
		AbRefreshDialogFragment f = new AbRefreshDialogFragment();
		Bundle args = new Bundle();
		args.putInt("style", style);
		args.putInt("theme", theme);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStyle = getArguments().getInt("style");
		mTheme = getArguments().getInt("theme");
		setStyle(mStyle, mTheme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout parent = new LinearLayout(this.getActivity());
		parent.setBackgroundColor(mBackgroundColor);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setPadding(20, 20, 20, 20);
		parent.setMinimumWidth(AbViewUtil.scale(this.getActivity(), 400));
		
		mImageView = new ImageView(this.getActivity());
		mImageView.setImageResource(mIndeterminateDrawable);
		mImageView.setScaleType(ScaleType.MATRIX);

		mTextView = new TextView(this.getActivity());
		mTextView.setText(mMessage);
		mTextView.setTextColor(mTextColor);
		mTextView.setTextSize(mTextSize);
		mTextView.setPadding(5, 5, 5, 5);

		parent.addView(mImageView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		parent.addView(mTextView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					// 执行刷新
					load(v);
			}

		});

		mContentView = parent;

		return mContentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void setTextSize(int textSize) {
		this.mTextSize = textSize;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}
	
	@Override
	public void setMessage(String message) {
		this.mMessage = message;
		if(mTextView!=null){
			mTextView.setText(mMessage);
		}
	}

	public int getIndeterminateDrawable() {
		return mIndeterminateDrawable;
	}

	public void setIndeterminateDrawable(int indeterminateDrawable) {
		this.mIndeterminateDrawable = indeterminateDrawable;
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}


}
