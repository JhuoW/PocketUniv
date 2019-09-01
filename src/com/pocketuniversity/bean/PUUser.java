package com.pocketuniversity.bean;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PUUser implements Parcelable {
	private String userId ;
	private String ID;
	private String name;
	private String image;
	private String sex;
	private String address;
	private String authority;

	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public PUUser() {
	}
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<PUUser> CREATOR = new Creator<PUUser>() {
		@Override
		public PUUser[] newArray(int size) {
			return new PUUser[size];
		}

		@Override
		public PUUser createFromParcel(Parcel in) {
			return new PUUser(in);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userId);
		dest.writeString(ID);
		dest.writeString(name);
		dest.writeString(image);
		dest.writeString(sex);
		dest.writeString(address);
	}

	private PUUser(Parcel in) {
		userId = in.readString();
		ID = in.readString();
		name = in.readString();
		image = in.readString();
		sex = in.readString();
		address = in.readString();
	}

}
