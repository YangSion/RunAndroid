package com.yangsion.runandroid.fragment;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yangsion.runandroid.R;
import com.yangsion.runandroid.domen.CacheInfo;

/**
 * 垃圾清理 Clean up the cell phone cache
 * @author stabilit.yang
 */
@SuppressLint("NewApi")
public class QingLiFragment extends BaseFragment implements OnClickListener {
	public static final String TAG = "JiaSuFragment";
	
	protected static final int INFOS = 1;
	protected static final int LIST_INFOS = 2;
	protected static final int CACHE_INFOS = 3;
	protected static final int CACHE_OVER = 4;
	private static PackageManager mPm;
	private TextView mScanning, mJiasu, mNohc_ts ,mBfb;
	private boolean mBtn = false;
	private FrameLayout mList_frame, mSy_frame;
	private GridView mList;
	private ProgressBar mProgressBar;
	private List<CacheInfo> mCacheInfos;
	private ListInfoAdapter mInfoAdapter;
	private int mCount = 0;
	private long mSavemem = 0;
	private int mLengs = 0;
	/**
	 * 是否第一次扫描
	 */
	public boolean mFlag = true;
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INFOS:
				mInfoAdapter.notifyDataSetChanged();
				PackageInfo info = (PackageInfo)msg.obj;
				mScanning.setText((String)getActivity().getString(R.string.Scanning)+ info.applicationInfo.loadLabel(mPm));
				break;
			case LIST_INFOS:
				mInfoAdapter.notifyDataSetChanged();
				mSy_frame.setVisibility(View.GONE);
				mScanning.setVisibility(View.GONE);
				mList_frame.setVisibility(View.VISIBLE);
				if (mCacheInfos.size() == 0) {
					mBtn = true;
					mJiasu.setText(R.string.To_scan);
					toast(getString(R.string.Mobile_phone_is_very_clean));
					mBfb.setVisibility(View.INVISIBLE);
					mProgressBar.setVisibility(View.INVISIBLE);
					mNohc_ts.setVisibility(View.VISIBLE);
				} else {
					mBtn = false;
					mJiasu.setText(R.string.Clear);
					mBfb.setText("/");
					mProgressBar.setVisibility(View.VISIBLE);
					mNohc_ts.setVisibility(View.GONE);
					mBfb.setVisibility(View.VISIBLE);
					toast(getString(R.string.Mobile_phone_there_are_rubbish));
				}
				break;
			case CACHE_INFOS:
				CacheInfo cacheInfo = (CacheInfo)msg.obj;
				mNohc_ts.setVisibility(View.VISIBLE);
				mNohc_ts.setText((String)getActivity().getString(R.string.Are_cleaning_up)+cacheInfo.getAppName());
				mProgressBar.setProgress(mLengs);
				mBfb.setText(R.string.Are_cleanings);
				mInfoAdapter.notifyDataSetChanged();
				break;
			case CACHE_OVER:
				mJiasu.setEnabled(true);
				mNohc_ts.setText(R.string.No_apps_need_to_clean_up);
				mJiasu.setText(R.string.To_scan);
				mBfb.setText(R.string.Complete);
				mInfoAdapter.notifyDataSetChanged();
				toast(getString(R.string.Clean_up_the)+mCount+(String)getActivity().getString(R.string.app)+Formatter.formatFileSize(getActivity(), mSavemem)+(String)getActivity().getString(R.string.Rubbish));
				//初始化
				mCount = 0;
				mSavemem = 0;
				mLengs = 0;
				break;

			default:
				break;
			}
		};
	};

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_qingli, null);
		getCaChe();
		mCacheInfos = new ArrayList<CacheInfo>();
		mInfoAdapter = new ListInfoAdapter();
		mScanning = (TextView) view.findViewById(R.id.scanning);
		mBfb = (TextView) view.findViewById(R.id.bfb);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mNohc_ts = (TextView) view.findViewById(R.id.nohc_ts);
		mJiasu = (TextView) view.findViewById(R.id.jiasu);
		mJiasu.setOnClickListener(this);
		mSy_frame = (FrameLayout) view.findViewById(R.id.sy_frame);
		mList_frame = (FrameLayout) view.findViewById(R.id.list_frame);
		mList = (GridView) view.findViewById(R.id.list);
		mList.setAdapter(mInfoAdapter);
		// 设置滚动条可见
		getActivity().setProgressBarIndeterminateVisibility(true);
//		list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int position,
//					long arg3) {
//				Object obj = list.getItemAtPosition(position);
//				if (obj != null && obj instanceof CacheInfo) {
//					CacheInfo info = (CacheInfo) obj;
//					ViewHolder holder = (ViewHolder) view.getTag();
//					if (info.isChecked()) {
//						holder.cb_status.setChecked(false);
//						info.setChecked(false);
//					} else {
//						holder.cb_status.setChecked(true);
//						info.setChecked(true);
//					}
//				}
//				infoAdapter.notifyDataSetChanged();
//			}
//		});
		
		return view;
	}

	/**
	 * 获取缓存
	 */
	private void getCaChe() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				mPm = getActivity().getPackageManager();
				List<PackageInfo> infos = mPm.getInstalledPackages(0);
				for (PackageInfo info : infos) {
					getCacheSize(info);
					SystemClock.sleep(60);
					Message msg = Message.obtain();
					msg.what = INFOS;
					msg.obj = info;
					mHandler.sendMessage(msg);
				}
				
				Message msg = Message.obtain();
				msg.what = LIST_INFOS;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		if (mInfoAdapter != null) {
			mInfoAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	
	
	/**
	 * 获取包名的应用缓存
	 * @param packgeName
	 */
    public void getCacheSize(PackageInfo packgeInfo) {
    	try {
    		Method myUserId = UserHandle.class.getDeclaredMethod("myUserId");
    		// 4.2 后需要一个int 类型的参数  从系统UserHandle中获取
            int userID = (Integer) myUserId.invoke(mPm,null); 
			Method method  = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, int.class, IPackageStatsObserver.class);
			method.invoke(mPm, packgeInfo.packageName, userID, new MyPackObserver(packgeInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private class MyPackObserver extends android.content.pm.IPackageStatsObserver.Stub{
    	
    	private PackageInfo packgeInfo;
    	
    	public MyPackObserver(PackageInfo info){
    		this.packgeInfo = info;
    	}
    	
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;//缓存大小
			long datasize = pStats.dataSize;//数据大小
			long codesize = pStats.codeSize;//应用代码（包）大小
//			Log.v(TAG,"cachesize:  "+Formatter.formatFileSize(getActivity().getApplicationContext(), cachesize));
//			Log.v(TAG,"datasize:  "+Formatter.formatFileSize(getActivity().getApplicationContext(), datasize));
//			Log.v(TAG,"codesize:  "+Formatter.formatFileSize(getActivity().getApplicationContext(), codesize));
			
			if (mFlag == true) {//有的应用是默认有12KB缓存的 清理不了，只能加个判断
				if (cachesize > 0) {
					CacheInfo info = new CacheInfo();
					info.setAppName(packgeInfo.applicationInfo.loadLabel(mPm).toString());
					info.setIcon(packgeInfo.applicationInfo.loadIcon(mPm));
					info.setCacheSize(cachesize);
//					info.setChecked(true);
					mCacheInfos.add(info);
				}
			} else {
				if (cachesize > 12288) {//大于12KB
					CacheInfo info = new CacheInfo();
					info.setAppName(packgeInfo.applicationInfo.loadLabel(mPm).toString());
					info.setIcon(packgeInfo.applicationInfo.loadIcon(mPm));
					info.setCacheSize(cachesize);
//					info.setChecked(true);
					mCacheInfos.add(info);
				}
			}
		}
		
	}
    
    
    
   class ListInfoAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCacheInfos.size();
	}

	@Override
	public Object getItem(int position) {
		//return null;
		CacheInfo info = null;
		info = mCacheInfos.get(position);
		return info;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		CacheInfo info = null;
//		info = cacheInfos.get(position);
		
		
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(getActivity());
			convertView = mInflater.inflate(R.layout.item_list_app_data_info, null);
			holder.appName = (TextView) convertView.findViewById(R.id.appName);
			holder.cb_status = (CheckBox) convertView.findViewById(R.id.cb_task_status);
			holder.cacheSize = (TextView) convertView.findViewById(R.id.cacheSize);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		//cacheInfos.get(position)
//		holder.icon.setImageDrawable(info.getIcon());
//		holder.appName.setText(info.getAppName());
//		holder.cb_status.setChecked(true);//info.isChecked()
//		holder.cacheSize.setText(Formatter.formatFileSize(getActivity().getApplicationContext(), info.getCacheSize()));
		holder.icon.setImageDrawable(mCacheInfos.get(position).getIcon());
		holder.appName.setText(mCacheInfos.get(position).getAppName());
		holder.cb_status.setChecked(true);//info.isChecked()
		holder.cacheSize.setText(Formatter.formatFileSize(getActivity().getApplicationContext(), mCacheInfos.get(position).getCacheSize()));
		
		return convertView;
	}
	
	   
   }
   
   private static class ViewHolder {
	   
		TextView appName;
		TextView cacheSize;
		ImageView icon;
		CheckBox cb_status;
		
		
	}

@Override
public void onClick(View v) {
	if (v.getId() == mJiasu.getId()) {
		if (mBtn == false) {
			//执行加速清理	
			mFlag = false;
			mBtn = true;
			mBfb.setVisibility(View.VISIBLE);
			deleteAllCache();
			mJiasu.setEnabled(false);
			mProgressBar.setMax(mCacheInfos.size());
		} else {
			mBtn = false;
			mProgressBar.setProgress(mLengs);
			mBfb.setVisibility(View.INVISIBLE);
			mJiasu.setText(R.string.Clear);
			mSy_frame.setVisibility(View.VISIBLE);
			mScanning.setVisibility(View.VISIBLE);
			mList_frame.setVisibility(View.GONE);
			mNohc_ts.setVisibility(View.GONE);
			getCaChe();//重新扫描   
		}
	}
	
}

/**
 * 清理缓存
 */
private void deleteAllCache() {  
    Method[] methods = PackageManager.class.getMethods();  
    Long localLong = Long.valueOf(getEnvironmentSize() - 1L);  
    Object[] arrayOfObject = new Object[2];  
    arrayOfObject[0] = localLong; 
    for(Method method : methods){  
        if( "freeStorageAndNotify".equals(method.getName())  ) {  
            try {  
                method.invoke(mPm, localLong, new MyPackageDataObserver());//Integer.MAX_VALUE * 100
               
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return;  
        }  
    }  
}

/**
 * 所有缓存数据
 * @return
 */
private long getEnvironmentSize() {
	File localFile = Environment.getDataDirectory();  
    long l1;  
    if (localFile == null)  
        l1 = 0L;  
    while (true) {  
        String str = localFile.getPath();  
        StatFs localStatFs = new StatFs(str);  
        long l2 = localStatFs.getBlockSize();  
        l1 = localStatFs.getBlockCount() * l2;  
        return l1;  
    }  
} 


private class MyPackageDataObserver extends IPackageDataObserver.Stub{  
    @Override  
    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {  
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				//要清理的缓存
				List<CacheInfo> claerCacheInfos = new ArrayList<CacheInfo>();
				for (CacheInfo info : mCacheInfos) {//遍历集合时不能改变集合的大小
//			if (info.isChecked()) {
//				count++;
//				savemem += info.getCacheSize();
//				claerCacheInfos.add(info);
//			}
					mCount++;
					mSavemem += info.getCacheSize();
					claerCacheInfos.add(info);
					
				}
				for (CacheInfo cacheInfo : claerCacheInfos) {
					mCacheInfos.remove(cacheInfo);// 默认清理所有
					mLengs++;
					Message msg = Message.obtain();
					msg.what = CACHE_INFOS;
					msg.obj = cacheInfo;
					mHandler.sendMessage(msg);
					SystemClock.sleep(360);
				}
				Message msg = Message.obtain();
				msg.what = CACHE_OVER;
				mHandler.sendMessage(msg);
				claerCacheInfos = null;
			}
		}).start();
    	
           
    }  
}  
	
}
