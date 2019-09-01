package com.pocketuniversity.https;

import java.util.HashMap;
import java.util.Map;

import com.easemob.chatuidemo.video.util.Utils;
import com.pocketuniversity.base.C;


public class APIHelper {

	Map<String,String> param;


	public APIHelper() {
		param = new HashMap<String, String>();
	}

	public String getNewsCatergory() {
		param.clear();
		return new WebService(C.GETCATEGORY, param).getReturnInfo();
	}

	public String getCampaignCategory(){
		param.clear();
		return new WebService(C.GETCAMPAIGNCATEGORY, param).getReturnInfo();
	}

	public String getTopPosts() {
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETFIRSTTHREEPOST, param).getReturnInfo();

	}

	public String getCampaignPosts(){
		param.clear();
		return new WebService(C.GETCAMPAIGNNEWS, param).getReturnInfo();
	}
	
	public String searchPosts(String keyWord, int page, int count) {
		return "";
	}

	public String getPostsByCategory(String id) {
		param.clear();
		param.put("userID",Utils.getID());
		param.put("category", id);
//		param.put("page", page+"");
//		param.put("count", count+"");
		return new WebService(C.GETNEWSLIST, param).getReturnInfo();
	}

	public String getPostsByCampaignCategory(String id) {
		param.clear();
		param.put("userID",Utils.getID());
		param.put("category", id);
		return new WebService(C.GETCAMPAIGNLIST, param).getReturnInfo();
	}

	public String getPostsByUserId(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETMYCAMPAIGNLIST, param).getReturnInfo();
	}
	
	public String getTiaozaoList(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETTIAOZAOLIST, param).getReturnInfo();
	}
	
	public String getMyUploadGoodsByUserId(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETMYUPLOADGOODS, param).getReturnInfo();
	}
	
	public String getLostList(){
		param.clear();
		return new WebService(C.GETLOSTLIST, param).getReturnInfo();
	}
	
	public String getMyLostListByUserId(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETMYLOSTLIST, param).getReturnInfo();
	}
	
	public String getNoticeListByUserId(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETNOTICELIST, param).getReturnInfo();
	}
	
	public String getFirstTwoCampaign(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETFIRSTTWOCAMPAIGN, param).getReturnInfo();
	}
	
	public String getFirstThreeTiaozao(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.GETFIRSTTHREEGOODS, param).getReturnInfo();
	}
	
	public String getMyAddCampaign(){
		param.clear();
		param.put("userID", Utils.getID());
		return new WebService(C.ADDMYADDCAMPAIGN, param).getReturnInfo();
	}
	
	public String AddZan(String id){
		param.clear();
		param.put("userID", Utils.getID());
		param.put("id", id);
		return new WebService(C.ADDZAN, param).getReturnInfo();
	} 
	public String CancelZan(String id){
		param.clear();
		param.put("userID", Utils.getID());
		param.put("id", id);
		return new WebService(C.CANCELZAN, param).getReturnInfo();
	} 
	
	public String deleteDynamic(String id){
		param.clear();
		param.put("id",id);
		return new WebService(C.DELETEDYNAMIC, param).getReturnInfo();
	}
}
