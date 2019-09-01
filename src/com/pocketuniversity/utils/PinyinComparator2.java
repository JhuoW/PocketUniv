package com.pocketuniversity.utils;

import java.util.Comparator;

import com.pocketuniversity.bean.SortDepart;
import com.pocketuniversity.bean.SortUser;

public class PinyinComparator2 implements Comparator<SortDepart>{
	public int compare(SortDepart o1, SortDepart o2) {
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
