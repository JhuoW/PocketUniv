package com.pocketuniversity.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {
	
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
