package com.easemob.chatuidemo.utils;

import android.content.Context;
import android.text.GetChars;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.example.pocketuniversity.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.db.FriendsDBHelper;
import com.pocketuniversity.group.bean.GroupMember;
import com.pocketuniversity.group.db.GroupDBHelper;
import com.pocketuniversity.service.PreferenceMap;
import com.pocketuniversity.utils.PhotoUtils;
import com.squareup.picasso.Picasso;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = App.getInstance().getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
        return user;
    }
    
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
		if(user==null||user.getAvatar()==null){
			System.out.println("无头像0");
			Picasso.with(context).load(R.drawable.default_avatar)
			.into(imageView);
		}
		else if (user != null && user.getAvatar().length() > 0) {
			System.out.println("有头像");
			System.out.println("头像--->" + user.getAvatar());
			Picasso.with(context).load(user.getAvatar())
					.placeholder(R.drawable.default_avatar).into(imageView);
			System.out.println("显示头像");
		} else {
			System.out.println("无头像");
			Picasso.with(context).load(R.drawable.default_avatar)
					.into(imageView);
		}
    }
    
    public static void setUserAvatar2(Context context , ImageView img){
    	ImageLoader imageLoader = ImageLoader.getInstance();

    	PUUser puUser = new PreferenceMap(context).getUser();
    	String url = puUser.getImage();
    	
    	imageLoader
		.displayImage(
				url,
				img,
				PhotoUtils
						.getImageOptions(R.drawable.icon_default_avatar_selector));
    }
    
    public static void setUserAvatar3(Context text , String imgUrl , ImageView img){
    	ImageLoader imageLoader = ImageLoader.getInstance();
    	imageLoader.displayImage(imgUrl,
				img,
				PhotoUtils
						.getImageOptions(R.drawable.icon_default_avatar_selector));
    }
    
    public static void setUserAvatar4(Context context ,String username ,ImageView img){
    	FriendsDBHelper friendsDbHelper = new FriendsDBHelper(context);
    	PUUser user =friendsDbHelper.getUserByUserName(username);
    	System.out.println("头像url:"+user.getImage());
    	ImageLoader imageLoader = ImageLoader.getInstance();
     	imageLoader.displayImage(user.getImage(),
				img,
				PhotoUtils
						.getImageOptions(R.drawable.default_avatar));
    }
    
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
    
    public static void setUserAvatar5(Context context ,String username ,ImageView img){
    	GroupDBHelper groupDBHelper = new GroupDBHelper(context);
    	GroupMember gm = groupDBHelper.getMember(username);
    	ImageLoader imageLoader = ImageLoader.getInstance();
    	imageLoader.displayImage(gm.getImgUrl(),
				img,
				PhotoUtils
						.getImageOptions(R.drawable.default_avatar));
    }
    
    public static void setUserAvatar6(Context context ,String username ,ImageView img){
    	GroupDBHelper groupDBHelper = new GroupDBHelper(context);
    	GroupMember gm = groupDBHelper.getMember(username);
    	ImageLoader imageLoader = ImageLoader.getInstance();
    	imageLoader.displayImage(gm.getImgUrl(),
				img,
				PhotoUtils
						.getImageOptions(R.drawable.default_useravatar));
    }
    
}
