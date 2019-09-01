package com.pocketuniversity.fragment;


import com.example.pocketuniversity.R;
import com.pocketuniversity.view.HeaderLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment{
	 public HeaderLayout headerLayout;
	  public Context ctx;
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    ctx = getActivity();
	    headerLayout = (HeaderLayout) getView().findViewById(R.id.headerLayout);
	  }
}
