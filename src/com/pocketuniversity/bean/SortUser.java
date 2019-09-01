package com.pocketuniversity.bean;


/**
 * 排序用户对象
 */
public class SortUser {
  private PUUser innerUser;
  private String sortLetters;

  public PUUser getInnerUser() {
    return innerUser;
  }

  public void setInnerUser(PUUser innerUser) {
    this.innerUser = innerUser;
  }

  public String getSortLetters() {
    return sortLetters;
  }

  public void setSortLetters(String sortLetters) {
    this.sortLetters = sortLetters;
  }
}
