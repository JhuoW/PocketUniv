package com.pocketuniversity.view;


import com.example.pocketuniversity.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class MyProgressDialog extends Dialog {
	
	private Context context ; 
	CircularProgress view;
	
	public MyProgressDialog(Context context) {
		super(context,R.style.MyprogressDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState);
        View view=LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(  
        		LinearLayout.LayoutParams.MATCH_PARENT,  
        		LinearLayout.LayoutParams.MATCH_PARENT);  
        lp.gravity = Gravity.CENTER; 
        addContentView(view, lp); 
        setCanceledOnTouchOutside(false);
	}
	 @Override
	public void show() {
		super.show();
	}

}
