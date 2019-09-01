package com.pocketuniversity.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.db.FriendsDBHelper;

public class UserUtils2 {

    public static void setUserAvatar4(Context context ,String username ,ImageView img){
    	FriendsDBHelper friendsDbHelper = new FriendsDBHelper(context);
    	PUUser user =friendsDbHelper.getUserByUserName(username);
    	System.out.println("Í·Ïñurl:"+user.getImage());
    	ImageLoader imageLoader = ImageLoader.getInstance();
     	imageLoader.displayImage(user.getImage(),
				img,
				PhotoUtils
						.getImageOptions(R.drawable.default_avatar));
    }
    
    public static String getUserName(Context context , String username){
    	FriendsDBHelper friendsDbHelper = new FriendsDBHelper(context);
    	PUUser user = friendsDbHelper.getUserByUserName(username);
    	return user.getName();
    }
}
