package com.pocketuniversity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by lzw on 14-9-25.
 */
public class ChatBaseListAdapter<T> extends BaseAdapter {
  Context ctx;
  LayoutInflater inflater;
  List<T> datas = new ArrayList<T>();

  public ChatBaseListAdapter(Context ctx) {
    initWithContext(ctx);
  }

  public void initWithContext(Context ctx) {
    this.ctx = ctx;
    inflater = LayoutInflater.from(ctx);
  }

  public ChatBaseListAdapter(Context ctx, List<T> datas) {
    initWithContext(ctx);
    this.datas = datas;
  }

  public void setDatas(List<T> datas) {
    this.datas = datas;
  }

  public List<T> getDatas() {
    return datas;
  }

  public void addAll(List<T> subDatas) {
    datas.addAll(subDatas);
    notifyDataSetChanged();
  }

  public void add(T object) {
    datas.add(object);
    notifyDataSetChanged();
  }

  public void remove(int position) {
    datas.remove(position);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int position) {
    return datas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }

  public void clear() {
    datas.clear();
    notifyDataSetChanged();
  }
}
