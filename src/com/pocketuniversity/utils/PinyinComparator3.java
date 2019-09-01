package com.pocketuniversity.utils;

import java.util.Comparator;

import com.pocketuniversity.bean.SortCourier;

public class PinyinComparator3 implements Comparator<SortCourier> {
	public int compare(SortCourier o1, SortCourier o2) {
	    if (o1.getSortLetters().equals("@")
	        || o2.getSortLetters().equals("#")) {
	      return -1;
	    } else if (o1.getSortLetters().equals("#")
	        || o2.getSortLetters().equals("@")) {
	      return 1;
	    } else {
	      return o1.getSortLetters().compareTo(o2.getSortLetters());
	    }
	  }

}
