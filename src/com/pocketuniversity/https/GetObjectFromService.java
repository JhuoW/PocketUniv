package com.pocketuniversity.https;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pocketuniversity.base.App;
import com.pocketuniversity.bean.CampaignJoiner;
import com.pocketuniversity.bean.CampaignPostModel;
import com.pocketuniversity.bean.CarRoute;
import com.pocketuniversity.bean.ClassInfo;
import com.pocketuniversity.bean.Comment;
import com.pocketuniversity.bean.ContactUser;
import com.pocketuniversity.bean.Courier;
import com.pocketuniversity.bean.CourierDetail;
import com.pocketuniversity.bean.DepartmentPhone;
import com.pocketuniversity.bean.DpDetail;
import com.pocketuniversity.bean.FriendsCircle;
import com.pocketuniversity.bean.History;
import com.pocketuniversity.bean.Lost;
import com.pocketuniversity.bean.MyAddCampaign;
import com.pocketuniversity.bean.Notice;
import com.pocketuniversity.bean.Notify;
import com.pocketuniversity.bean.PE;
import com.pocketuniversity.bean.PUUser;
import com.pocketuniversity.bean.PostModel;
import com.pocketuniversity.bean.Reminder;
import com.pocketuniversity.bean.RequestUser;
import com.pocketuniversity.bean.SearchGroup;
import com.pocketuniversity.bean.Tiaozao;
import com.pocketuniversity.bean.TopPic;
import com.pocketuniversity.bean.push.CampaignNotify;
import com.pocketuniversity.bean.push.NewsNotify;
import com.pocketuniversity.bean.push.RemindNotify;
import com.pocketuniversity.service.PreferenceMap;

public class GetObjectFromService {
	static PreferenceMap preference = new PreferenceMap(App.ctx);
	static Map<String, String> result = new HashMap<String, String>();

	/**
	 * 获取简单的json结果
	 * 
	 * @param jsonstr
	 * @return
	 */
	public static Boolean getSimplyResult(String jsonstr) {
		Boolean flag = false;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (!json.getString("ret").equals("success")) {
				return flag;
			} else {
				flag = true;
			}
		} catch (Exception e) {
		}
		return flag;

	}

	/**
	 * 获取登录结果
	 * 
	 * @param jsonstr
	 * @param number
	 * @param password
	 * @return
	 */
	public static Map<String, Boolean> getLoginResult(String jsonstr) {
		final Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", true);
		try {
			final JSONObject json = new JSONObject(jsonstr);

			if (json.get("ret").equals("success")) {
				final String id = json.getString("ID");
				final String name = json.getString("nickName");
				final String image = json.getString("imgUrl");
				final String sex = json.getString("sex");
				final String address = json.getString("address");
				final String authority = json.getString("authority");

				System.out.println("id:" + id + "name:" + name + "image:"
						+ image + "sex:" + sex + "address:" + address
						+ "authority:" + authority);

				if (id.isEmpty() || name.isEmpty() || authority.isEmpty()
						|| address.isEmpty()) {
					result.put("result", false);
					return result;
				} else {
					PUUser user = new PUUser();
					user.setAddress(address);
					user.setAuthority(authority);
					user.setID(id);
					user.setImage(image);
					user.setName(name);
					user.setSex(sex);
					preference.setUser(user);
				}
			} else {
				result.put("result", false);
			}
		} catch (Exception e) {
			result.put("result", false);
		}
		return result;
	}

	public static List<PUUser> getSearchResult(String jsonstr) {
		List<PUUser> result = new ArrayList<PUUser>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			String ret = (String) json.get("ret");
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("userSearchList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject child = jsonarray.getJSONObject(i);
					PUUser user = new PUUser();
					user.setID(child.getString("ID"));
					user.setName(child.getString("nickName"));
					user.setImage(child.getString("imgUrl"));
					user.setAddress(child.getString("address"));
					user.setSex(child.getString("sex"));
					result.add(user);
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static Notify getNotificationDetail(String jsonstr) {
		Notify notify = new Notify();
		try {
			JSONObject json = new JSONObject(jsonstr);
			String type = json.getString("type");
			notify.setType(type);

			if (type.equals("RequestAddFriend")) {
				notify.setTitle("好友请求");
				notify.setContent(json.getString("nickname") + "想要添加你为好友");
			}
		} catch (Exception e) {
		}
		return notify;
	}

	public static RemindNotify getRemindNotify(String jsonStr){
		RemindNotify remindNotify = new RemindNotify();
		JSONObject json;
		try {
			json = new JSONObject(jsonStr);
			String type = json.getString("type");
			remindNotify.setKind(type);
			if(type.equals("PushRemind")){
				remindNotify.setContent(json.getString("content"));
				remindNotify.setCreateTime(json.getString("createTime"));
				remindNotify.setId(json.getString("id"));
				remindNotify.setRemindTime(json.getString("remindTime"));
				remindNotify.setType(json.getString("type"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remindNotify;
	}
	public static CampaignNotify getCampaignNotificationDetail(String jsonStr) {
		CampaignNotify campaignNotify = new CampaignNotify();
		JSONObject json;
		String type;
		try {
			json = new JSONObject(jsonStr);
			System.out.println("推送值：" + json);
			type = json.getString("type");
			campaignNotify.setType(type);

			if (type.equals("PushCampaign")) {
				campaignNotify.setComment_count(json.getString("commentCount"));
				campaignNotify.setContent(json.getString("betime"));
				campaignNotify.setPlace(json.getString("place"));
				campaignNotify.setId(json.getString("newsID"));
				campaignNotify.setImageurl(json.getString("imgUrl"));
				campaignNotify.setSource(json.getString("source"));
				campaignNotify.setTime(json.getString("time"));
				campaignNotify.setTitle(json.getString("title"));
				campaignNotify.setView_count(json.getString("viewCount"));
				campaignNotify.setJoin(json.getString("isJoin"));
				campaignNotify.setHasJoin(json.getString("attendCount"));
				campaignNotify.setCollection(json.getString("collection"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return campaignNotify;

	}

	public static NewsNotify getNewsNotificationDetail(String jsonStr) {
		NewsNotify newsNotify = new NewsNotify();
		JSONObject json;
		String type;
		try {
			json = new JSONObject(jsonStr);
			type = json.getString("type");
			newsNotify.setType(type);
			if (type.equals("PushNews")) {
				newsNotify.setCollection(json.getString("collection"));
				newsNotify.setId(json.getString("newsID"));
				newsNotify.setSource(json.getString("source"));
				newsNotify.setTitle(json.getString("title"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsNotify;
	}

	public static List<RequestUser> getRequestUser(String jsonstr) {
		List<RequestUser> data = new ArrayList<RequestUser>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("newFriendList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject child = jsonarray.getJSONObject(i);
					RequestUser user = new RequestUser();
					user.setID(child.getString("ID"));
					user.setImage(child.getString("imgUrl"));
					user.setName(child.getString("nickName"));
					user.setSex(child.getString("sex"));
					user.setStatus(child.getString("status"));
					user.setRemark(child.getString("remarks"));
					// child.getString("address")
					data.add(user);
				}
			}
		} catch (Exception e) {
		}
		return data;
	}

	public static List<PUUser> getFriend(String jsonstr) {
		List<PUUser> list = new ArrayList<PUUser>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("newFriendList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject child = jsonarray.getJSONObject(i);
					PUUser user = new PUUser();
					user.setID(child.getString("ID"));
					user.setSex(child.getString("sex"));
					user.setImage(child.getString("imgUrl"));
					user.setName(child.getString("nickName"));
					user.setUserId(child.getString("userId"));
					list.add(user);
				}
			}
		} catch (Exception e) {
		}
		return list;
	}

	public static List<ContactUser> getContactUser(String jsonstr) {
		List<ContactUser> contactuser = new ArrayList<ContactUser>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray array = json.getJSONArray("contactsList");
				for (int i = 0; i < array.length(); i++) {
					JSONObject child = array.getJSONObject(i);
					ContactUser user = new ContactUser();
					user.setId(child.getString("ID"));
					user.setImage("");
					user.setName(child.getString("name"));
					user.setPhone(child.getString("phone"));
					user.setStatue(child.getString("status"));
					contactuser.add(user);
				}
			}
		} catch (Exception e) {
		}
		return contactuser;
	}

	public static PUUser getPUUser(String jsonstr) {
		PUUser user = new PUUser();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				user.setUserId(json.getString("userId"));
				user.setAddress(json.getString("address"));
				user.setID(json.getString("ID"));
				user.setImage(json.getString("imgUrl"));
				user.setName(json.getString("nickName"));
				user.setSex(json.getString("sex"));
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return user;
	}

	// for (int i = 0; i < 4; i++) {
	// Comment comment = new Comment();
	// comment.setCode("");
	// comment.setContent("�ǳ��úܺò����п��Էǳ��úܺò����п��Էǳ��úܺò����п�");
	// comment.setImage_url("");
	// comment.setName("С����" + i + "��");
	// comment.setTime("03-31 9:00");
	// commentdata.add(comment);
	public static List<Comment> getCommentList(String jsonstr) {
		List<Comment> data = new ArrayList<Comment>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("comments");
				for (int i = 0; i < jsonarray.length(); i++) {
					Comment comment = new Comment();
					JSONObject obj = jsonarray.getJSONObject(i);
					comment.setCode("");
					comment.setContent(obj.getString("content"));
					comment.setImage_url(obj.getString("picture"));
					comment.setName(obj.getString("nickName"));
					comment.setTime(obj.getString("time"));
					comment.setAuthority(obj.getString("authority"));
					data.add(comment);
				}
			}
		} catch (Exception e) {
		}
		return data;
	}

	public static List<Comment> getFriendsCircleCommentList(String jsonstr) {
		List<Comment> data = new ArrayList<Comment>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("comments");
				for (int i = 0; i < jsonarray.length(); i++) {
					Comment comment = new Comment();
					JSONObject obj = jsonarray.getJSONObject(i);
					comment.setContent(obj.getString("content"));
					comment.setImage_url(obj.getString("picture"));
					comment.setName(obj.getString("nickName"));
					comment.setTime(obj.getString("time"));
					comment.setAuthority(obj.getString("authority"));
					comment.setCode(obj.getString("userID"));
					data.add(comment);
				}
			}
		} catch (Exception e) {
		}
		return data;
	}

	public static List<PostModel> getCollectionNews(String jsonstr) {
		List<PostModel> datas = new ArrayList<PostModel>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json.getJSONArray("MyNewsList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					PostModel post = new PostModel();
					post.setCollection(obj.getString("collection"));
					post.setComment_count(obj.getString("commentCount"));
					post.setContent(obj.getString("description"));
					post.setId(obj.getString("newsID"));
					post.setImageurl(obj.getString("imgUrl"));
					post.setSource(obj.getString("source"));
					post.setTime(obj.getString("time"));
					post.setTitle(obj.getString("title"));
					post.setView_count(obj.getString("viewCount"));
					datas.add(post);
				}

			}
		} catch (Exception e) {
		}
		return datas;
	}

	public static List<CampaignPostModel> getMyCampaign(String jsonstr) {
		List<CampaignPostModel> datas = null;

		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<CampaignPostModel>();
				JSONArray jsonarray = json.getJSONArray("MyCampaignList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					CampaignPostModel post = new CampaignPostModel();
					// post.setCollection("true");
					post.setComment_count(obj.getString("commentCount"));
					post.setContent(obj.getString("betime"));
					post.setPlace(obj.getString("place"));
					post.setId(obj.getString("newsID"));
					post.setImageurl(obj.getString("imgUrl"));
					post.setSource(obj.getString("source"));
					post.setTime(obj.getString("time"));
					post.setTitle(obj.getString("title"));
					post.setView_count(obj.getString("viewCount"));
					post.setJoin(obj.getString("isJoin"));
					post.setHasJoin(obj.getString("attendCount"));
					post.setCollection(obj.getString("collection"));
					datas.add(post);
				}

			}
		} catch (Exception e) {
		}
		return datas;
	}

	public static List<Tiaozao> getMyTiaozao(String jsonstr) {
		List<Tiaozao> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Tiaozao>();
				JSONArray jsonarray = json.getJSONArray("MyCommodityList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Tiaozao tiaozao = new Tiaozao();
					tiaozao.setUserId(obj.getString("studentID"));
					tiaozao.setNickname(obj.getString("nickname"));
					tiaozao.setImgUrl(obj.getString("imgUrl"));
					tiaozao.setGoodsId(obj.getString("commodityID"));
					tiaozao.setHeader(obj.getString("picture"));
					tiaozao.setGoodsname(obj.getString("commodityName"));
					tiaozao.setPrice(obj.getString("price"));
					tiaozao.setPhone(obj.getString("phone"));
					tiaozao.setTime(obj.getString("time"));
					tiaozao.setView_count(obj.getString("viewCount"));
					tiaozao.setCollection(obj.getString("collection"));
					tiaozao.setDetail(obj.getString("detail"));
					datas.add(tiaozao);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<Tiaozao> getMyUplaodTiaozao(String jsonstr) {
		List<Tiaozao> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Tiaozao>();
				JSONArray jsonarray = json.getJSONArray("MyCommodityList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Tiaozao tiaozao = new Tiaozao();
					tiaozao.setUserId(obj.getString("studentID"));
					tiaozao.setNickname(obj.getString("nickname"));
					tiaozao.setImgUrl(obj.getString("imgUrl"));
					tiaozao.setGoodsId(obj.getString("commodityID"));
					tiaozao.setHeader(obj.getString("picture"));
					tiaozao.setGoodsname(obj.getString("commodityName"));
					tiaozao.setPrice(obj.getString("price"));
					tiaozao.setPhone(obj.getString("phone"));
					tiaozao.setTime(obj.getString("time"));
					tiaozao.setView_count(obj.getString("viewCount"));
					tiaozao.setCollection(obj.getString("collection"));
					tiaozao.setDetail(obj.getString("detail"));
					datas.add(tiaozao);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<Lost> getMyLost(String jsonstr) {
		List<Lost> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Lost>();
				JSONArray jsonarray = json.getJSONArray("lostList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Lost lost = new Lost();
					lost.setUserId(obj.getString("userId"));
					lost.setLostId(obj.getString("lostId"));
					lost.setNickname(obj.getString("nickName"));
					lost.setPhone(obj.getString("phone"));
					lost.setHeader(obj.getString("header"));
					lost.setMajor(obj.getString("major"));
					lost.setTitle(obj.getString("title"));
					lost.setPublishTime(obj.getString("publishTime"));
					lost.setState(obj.getString("state"));
					lost.setLostTime(obj.getString("lostTime"));
					lost.setPlace(obj.getString("place"));
					lost.setDetail(obj.getString("detail"));
					lost.setImgUrl(obj.getString("imgUrl"));
					datas.add(lost);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<Notice> getMyNotice(String jsonstr) {
		List<Notice> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Notice>();
				JSONArray jsonarray = json.getJSONArray("myNoticeList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Notice notice = new Notice();
					notice.setId(obj.getString("id"));
					notice.setViewCount(obj.getString("viewCount"));
					notice.setTime(obj.getString("time"));
					notice.setCollection(obj.getString("collection"));
					notice.setTitle(obj.getString("title"));
					notice.setSource(obj.getString("source"));

					datas.add(notice);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<DepartmentPhone> getDp(String jsonstr) {
		List<DepartmentPhone> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<DepartmentPhone>();
				JSONArray jsonarray = json.getJSONArray("ContactInformation");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					DepartmentPhone dp = new DepartmentPhone();
					dp.setName(obj.getString("departmentname"));
					dp.setId(obj.getString("departmentID"));
					datas.add(dp);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<DpDetail> getDpDetail(String jsonstr) {
		List<DpDetail> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<DpDetail>();
				JSONArray jsonarray = json.getJSONArray("ContactInformation");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					DpDetail dp = new DpDetail();
					dp.setName(obj.getString("departmentname"));
					dp.setPhone(obj.getString("departmentphone"));
					datas.add(dp);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<MyAddCampaign> getMyAddCampaign(String jsonstr) {
		List<MyAddCampaign> datas = null;

		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<MyAddCampaign>();
				JSONArray jsonarray = json.getJSONArray("MyAddCampaignList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					MyAddCampaign post = new MyAddCampaign();
					// post.setCollection("true");
					post.setComment_count(obj.getString("commentCount"));
					post.setContent(obj.getString("betime"));
					post.setPlace(obj.getString("place"));
					post.setId(obj.getString("newsID"));
					post.setImageurl(obj.getString("imgUrl"));
					post.setSource(obj.getString("source"));
					post.setTime(obj.getString("time"));
					post.setTitle(obj.getString("title"));
					post.setView_count(obj.getString("viewCount"));
					post.setJoin(obj.getString("isJoin"));
					post.setHasJoin(obj.getString("attendCount"));
					post.setCollection(obj.getString("collection"));
					post.setState(obj.getString("state"));
					datas.add(post);
				}

			}
		} catch (Exception e) {
		}
		return datas;
	}

	public static List<CampaignJoiner> getCampaignJoiner(String jsonStr) {
		List<CampaignJoiner> datas = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<CampaignJoiner>();
				JSONArray jsonarray = json.getJSONArray("campaignJoiner");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					CampaignJoiner joiner = new CampaignJoiner();
					joiner.setUserId(obj.getString("userId"));
					joiner.setNickName(obj.getString("nickName"));
					joiner.setHeader(obj.getString("imgUrl"));
					datas.add(joiner);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<CampaignJoiner> getZanUser(String jsonStr) {
		List<CampaignJoiner> datas = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<CampaignJoiner>();
				JSONArray jsonarray = json.getJSONArray("PraiseList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					CampaignJoiner joiner = new CampaignJoiner();
					joiner.setUserId(obj.getString("userId"));
					joiner.setNickName(obj.getString("nickName"));
					joiner.setHeader(obj.getString("imgUrl"));
					datas.add(joiner);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<SearchGroup> getHotGroupList(String jsonStr) {
		List<SearchGroup> datas = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<SearchGroup>();
				JSONArray jsonarray = json.getJSONArray("hotGroupList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject child = jsonarray.getJSONObject(i);
					SearchGroup searchGroup = new SearchGroup();
					String groupName = child.getString("groupName");
					String serverGroupIdg = child.getString("serverGroupId");
					String groupId = child.getString("groupId");
					String description = child.getString("description");
					String curNum = child.getString("curNum");
					String maxNum = child.getString("maxNum");
					String ownerNickName = child.getString("ownerNickName");
					String ownerId = child.getString("ownerId");
					String ownerHeader = child.getString("ownerHeader");
					searchGroup.setGroupName(groupName);
					searchGroup.setServerGroupId(serverGroupIdg);
					searchGroup.setGroupId(groupId);
					searchGroup.setDescription(description);
					searchGroup.setCurNum(curNum);
					searchGroup.setMaxNum(maxNum);
					searchGroup.setOwnerNickName(ownerNickName);
					searchGroup.setOwnerId(ownerId);
					searchGroup.setOwnerHeader(ownerHeader);
					datas.add(searchGroup);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	
	public static List<PUUser> getRecommendFriendList(String jsonStr){
		List<PUUser> datas = null;
		JSONObject json;
		try {
			json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<PUUser>();
				JSONArray jsonarray = json.getJSONArray("recommendFriendsList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject child = jsonarray.getJSONObject(i);
					PUUser user = new PUUser();
					user.setID(child.getString("ID"));
					user.setName(child.getString("nickName"));
					user.setImage(child.getString("imgUrl"));
					user.setAddress(child.getString("address"));
					user.setSex(child.getString("sex"));
					datas.add(user);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	
	public static List<ClassInfo> getClassInfo(String jsonStr) {
		List<ClassInfo> datas = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<ClassInfo>();
				JSONArray jsonarray = json.getJSONArray("classInfo");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobj = jsonarray.getJSONObject(i);
					ClassInfo classInfo = new ClassInfo();
					classInfo.setClassname(jsonobj.getString("className"));
					classInfo.setClassNumLen(jsonobj.getString("classNumLen"));
					classInfo
							.setFromClassNum(jsonobj.getString("fromClassNum"));
					classInfo.setClassRoom(jsonobj.getString("classRoom"));
					classInfo.setWeekday(jsonobj.getString("weekday"));

					datas.add(classInfo);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<FriendsCircle> getFriendsCircleList(String jsonStr) {
		List<FriendsCircle> datas = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<FriendsCircle>();
				JSONArray jsonarray = json.getJSONArray("friendsCircleList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobj = jsonarray.getJSONObject(i);
					FriendsCircle fc = new FriendsCircle();
					fc.setCommentCount(jsonobj.getString("commentCount"));
					fc.setContent(jsonobj.getString("content"));
					fc.setHeader(jsonobj.getString("header"));
					fc.setImgUrl(jsonobj.getString("imgUrl"));
					fc.setName(jsonobj.getString("name"));
					fc.setTime(jsonobj.getString("time"));
					fc.setZanCount(jsonobj.getString("zanCount"));
					fc.setIsZan(jsonobj.getString("isZan"));
					datas.add(fc);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	public static List<TopPic> getTopPicList(JSONArray jsonarray) {
		List<TopPic> datas = new ArrayList<TopPic>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject obj = jsonarray.getJSONObject(i);
				TopPic tp = new TopPic();
				String imgUrl = obj.getString("imgUrl");
				tp.setImgUrl(imgUrl);
				datas.add(tp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return datas;
	}

	public static List<Courier> getCourierList(JSONArray jsonarray) {
		List<Courier> datas = new ArrayList<Courier>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject obj = jsonarray.getJSONObject(i);
				Courier courier = new Courier();
				courier.setImgUrl(obj.getString("imgUrl"));
				courier.setName(obj.getString("expName"));
				courier.setPhone(obj.getString("phone"));
				courier.setUrl(obj.getString("url"));
				courier.setSimpleName(obj.getString("simpleName"));
				datas.add(courier);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datas;
	}

	public static List<CourierDetail> getCourierDetailList(JSONArray jsonarray) {
		List<CourierDetail> datas = new ArrayList<CourierDetail>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject obj = jsonarray.getJSONObject(i);
				CourierDetail cd = new CourierDetail();
				cd.setContent(obj.getString("context"));
				cd.setTime(obj.getString("time"));
				datas.add(cd);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datas;
	}

	public static PE getPEList(JSONObject json) {
		PE pe = new PE();
		try {
			String description = json.getString("description");
			String picUrl = json.getString("picUrl");
			String time = json.getString("time");
			String url = json.getString("url");
			String title = json.getString("title");
			pe.setDescription(description);
			pe.setPicUrl(picUrl);
			pe.setTime(time);
			pe.setTitle(title);
			pe.setUrl(url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pe;
	}

	public static List<History> getHistoryList(JSONArray jsonarray) {
		List<History> datas = new ArrayList<History>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject obj = jsonarray.getJSONObject(i);
				History history = new History();
				history.setDay(obj.getString("day"));
				history.setImg(obj.getString("img"));
				history.setMonth(obj.getString("month"));
				history.setTitle(obj.getString("title"));
				history.setYear(obj.getString("year"));
				datas.add(history);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datas;
	}

	public static List<CarRoute> getCarRouteList(JSONArray jsonarray) {
		List<CarRoute> datas = new ArrayList<CarRoute>();
		for (int i = 0; i < jsonarray.length(); i++) {
			try {
				JSONObject obj = jsonarray.getJSONObject(i);
				CarRoute cr = new CarRoute();
				cr.setInfo(obj.getString("info"));
				cr.setName(obj.getString("name"));
				cr.setStats(obj.getString("stats"));
				datas.add(cr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datas;
	}

	public static List<Reminder> getReminderList(String jsonstr) {
		List<Reminder> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Reminder>();
				JSONArray jsonarray = json.getJSONArray("RemindList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Reminder remind = new Reminder();
					remind.setContent(obj.getString("content"));
					remind.setCreateTime(obj.getString("createTime"));
					remind.setId(obj.getString("id"));
					remind.setRemindTime(obj.getString("remindTime"));
					remind.setType(obj.getString("type"));
					remind.setIsFinish(obj.getString("isFinish"));
					datas.add(remind);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	public static List<Reminder> getHasFinishReminderList(String jsonstr) {
		List<Reminder> datas = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				datas = new ArrayList<Reminder>();
				JSONArray jsonarray = json.getJSONArray("RemindList");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Reminder remind = new Reminder();
					remind.setContent(obj.getString("content"));
					remind.setCreateTime(obj.getString("createTime"));
					remind.setId(obj.getString("id"));
					remind.setRemindTime(obj.getString("remindTime"));
					remind.setType(obj.getString("type"));
					remind.setIsFinish(obj.getString("isFinish"));
					remind.setFinishTime(obj.getString("finishTime"));
					datas.add(remind);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
}


