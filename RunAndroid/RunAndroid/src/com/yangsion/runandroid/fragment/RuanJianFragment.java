package com.yangsion.runandroid.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.DataInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 软件管理 Software management
 * @author stabilit.yang
 */
@SuppressLint("NewApi")
public class RuanJianFragment extends BaseFragment {

	public static final String Tag = "RuanJianFragment";
	private List<DataInfo> mDataInfo;
	private ListView mList;
	private PackageManager mPm;
	private ListInfoAdapter mInfoAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_ruanjian, null);
		mDataInfo = new ArrayList<DataInfo>();
		mInfoAdapter = new ListInfoAdapter();
		mList = (ListView) view.findViewById(R.id.list);
		mList.setAdapter(mInfoAdapter);

		mPm = getActivity().getPackageManager();
		List<PackageInfo> infos = mPm.getInstalledPackages(0);
		for (PackageInfo info : infos) {
			if (!isSystemApp(info)) {
				getCacheSize(info);
				mInfoAdapter.notifyDataSetChanged();
			}
			
		}
		return view;

	}
	
	
	/**
	 * 是否系统应用
	 * @param pInfo
	 * @return
	 */
	public boolean isSystemApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);  
    }  
	
	

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 获取包名的应用缓存
	 * 
	 * @param packgeName
	 */
	public void getCacheSize(PackageInfo packgeInfo) {
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			method.invoke(mPm, packgeInfo.packageName, new MyPackObserver(
					packgeInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyPackObserver extends
			android.content.pm.IPackageStatsObserver.Stub {

		private PackageInfo packgeInfo;

		public MyPackObserver(PackageInfo info) {
			this.packgeInfo = info;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;// 缓存大小
			long datasize = pStats.dataSize;// 数据大小
			long codesize = pStats.codeSize;// 应用代码（包）大小
			long SDcachesize = pStats.externalCacheSize;// SD卡缓存大小
			long SDdatasize = pStats.externalDataSize;// SD卡数据大小
			long SDcodesize = pStats.externalCodeSize;// SD卡应用代码（包）大小
			
			DataInfo info = new DataInfo();
			info.setAppName(packgeInfo.applicationInfo.loadLabel(mPm).toString());
			info.setIcon(packgeInfo.applicationInfo.loadIcon(mPm));
			info.setPackgeName(packgeInfo.packageName);
			info.setAppVersion(packgeInfo.versionName);
			info.setDataSize(datasize + codesize + cachesize + SDcachesize + SDdatasize + SDcodesize);
			mDataInfo.add(info);
		}

	}

	class ListInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataInfo.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(getActivity());
				convertView = mInflater.inflate(R.layout.item_list_app_data_info2, null);
				holder.appName = (TextView) convertView.findViewById(R.id.appName);
				holder.version = (TextView) convertView.findViewById(R.id.version);
				holder.dataSize = (TextView) convertView.findViewById(R.id.dataSize);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.uninstall = (Button) convertView.findViewById(R.id.uninstall);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.icon.setImageDrawable(mDataInfo.get(position).getIcon());
			holder.appName.setText(mDataInfo.get(position).getAppName());
			holder.version.setText((String)getActivity().getString(R.string.Version)+ mDataInfo.get(position).getAppVersion());
			holder.dataSize.setText((String)getActivity().getString(R.string.Size)+ Formatter.formatFileSize(getActivity().getApplicationContext(), mDataInfo.get(position).getDataSize()));
			holder.uninstall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 数据清理/卸载   需要Root权限  跳转到应用详情 （用户自行选择）
					 Intent localIntent = new Intent();
				        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        if (Build.VERSION.SDK_INT >= 9) {
				            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				            localIntent.setData(Uri.fromParts("package", mDataInfo.get(position).getPackgeName(), null));
				        } else if (Build.VERSION.SDK_INT <= 8) {
				            localIntent.setAction(Intent.ACTION_VIEW);
				            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
				            localIntent.putExtra("com.android.settings.ApplicationPkgName", mDataInfo.get(position).getPackgeName());
				        }
				        startActivity(localIntent);

				}
			});
			
			return convertView;
		}

	}

	private static class ViewHolder {

		TextView appName;
		TextView version;
		TextView dataSize;
		ImageView icon;
		Button uninstall;

	}

}