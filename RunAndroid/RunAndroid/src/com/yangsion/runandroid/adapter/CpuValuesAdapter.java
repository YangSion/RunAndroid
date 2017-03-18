package com.yangsion.runandroid.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author stabilit.yang
 *
 */
public abstract class CpuValuesAdapter extends BaseAdapter {

	@Override
	public abstract int getCount();

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
