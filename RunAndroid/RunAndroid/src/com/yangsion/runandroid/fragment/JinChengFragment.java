package com.yangsion.runandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.yangsion.runandroid.R;
import com.yangsion.runandroid.activity.HomeActivity;
import com.yangsion.runandroid.domen.TaskInfo;
import com.yangsion.runandroid.parser.TaskInfoParser;
import com.yangsion.runandroid.util.SystemInfoUtils;

/**
 * 进程管理 Process management
 * @author stabilit.yang
 */
@SuppressLint("NewApi")
public class JinChengFragment extends BaseFragment implements OnClickListener {
	public static final String TAG = "JinChengFragment";
	private LinearLayout mNeiCunView;
	private RelativeLayout mButtons;
	private TextView mTv_running_prcesscount;
	private TextView mTv_ram_info;
	private ListView mLv_taskmanger;
	private Button mKillProcess;
	private Button mSelectOpposite;
	private TextView mOpenSetting;
	private SwipeRefreshLayout mReFresh;
	/**
	 * 所有进程信息的集合
	 */
	private List<TaskInfo> mInfos;
	private List<TaskInfo> mUserTaskInfos;
	private List<TaskInfo> mSystemTaskInfos;
	/**
	 * 正在运行的进程数量
	 */
	private int mRunningProcessCount;

	private TaskManagerAdapter mAdapter;

	private GifView mGif_loding, mGif_bg;
	/**
	 * 是否显示系统进程
	 */
	private boolean mShowsystem = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_jincheng, null);
	}
	

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gifBg();
		gifLoding();
		mButtons = (RelativeLayout) getActivity().findViewById(R.id.buttons);
		mNeiCunView = (LinearLayout) getActivity().findViewById(R.id.neiCunView);
		mReFresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.reFresh);
		mKillProcess = (Button) getActivity().findViewById(R.id.killProcess);
		mSelectOpposite = (Button) getActivity().findViewById(R.id.selectOpposite);
		mOpenSetting = (TextView) getActivity().findViewById(R.id.openSetting);
		mKillProcess.setOnClickListener(this);
		mSelectOpposite.setOnClickListener(this);
		mOpenSetting.setOnClickListener(this);

		mLv_taskmanger = (ListView) getActivity().findViewById(R.id.lv_taskmanger);
		mTv_running_prcesscount = (TextView) getActivity().findViewById(R.id.tv_running_prcesscount);
		mTv_ram_info = (TextView) getActivity().findViewById(R.id.tv_ram_info);

		fillData();

		mReFresh.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						fillData();
						mAdapter.notifyDataSetChanged();
						mReFresh.setRefreshing(false);
					}
				});
			}
		});

		mLv_taskmanger.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				Object obj = mLv_taskmanger.getItemAtPosition(position);
				if (obj != null && obj instanceof TaskInfo) {
					TaskInfo info = (TaskInfo) obj;
//					if (info.getPackname().equals(
//							getActivity().getPackageName())) {
//						// 就是本应用。
//						return;
//					}
					ViewHolder holder = (ViewHolder) view.getTag();
					if (info.isChecked()) {
						holder.cb_status.setChecked(false);
						info.setChecked(false);
					} else {
						holder.cb_status.setChecked(true);
						info.setChecked(true);
					}
				}
			}
		});

	}

	private void gifBg() {
		// 从xml中得到GifView的句柄
		mGif_bg = (GifView) getActivity().findViewById(R.id.gif_bg);
		// 设置Gif图片源
		mGif_bg.setGifImage(R.drawable.jincheng_list_bg);
		// 设置显示的大小，拉伸或者压缩
		mGif_bg.setShowDimension(getActivity().getWindowManager()
				.getDefaultDisplay().getWidth(), getActivity()
				.getWindowManager().getDefaultDisplay().getHeight() - 20);
//		// 添加监听器
//		gif_bg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
//				gif_loding.setGifImageType(GifImageType.COVER);
//			}
//		});
	}

	
	
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			// 通知界面刷新
			mAdapter.notifyDataSetChanged();
		}
	}

	private void gifLoding() {
		// 从xml中得到GifView的句柄
		mGif_loding = (GifView) getActivity().findViewById(R.id.gif_loding);
		// 设置Gif图片源
		mGif_loding.setGifImage(R.drawable.jincheng_loding_bg);
		// 设置显示的大小，拉伸或者压缩
		mGif_loding.setShowDimension(getActivity().getWindowManager()
				.getDefaultDisplay().getWidth(), getActivity()
				.getWindowManager().getDefaultDisplay().getHeight()
				- (getActivity().getWindowManager().getDefaultDisplay()
						.getHeight() / 2 / 2));
		// 添加监听器
		mGif_loding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
				mGif_loding.setGifImageType(GifImageType.COVER);
			}
		});
	}

	/**
	 * 获取所有进程
	 */
	private void fillData() {
		// 设置内存空间的大小 和 正在运行的进程的数量
		totalAvailMem = SystemInfoUtils.getAvailMem(getActivity());
		long totalMem = SystemInfoUtils.getTotalMem();
		mTv_ram_info.setText((String)getActivity().getString(R.string.Available_the_total_memory)
				+ Formatter.formatFileSize(getActivity(), totalAvailMem) + "/"
				+ Formatter.formatFileSize(getActivity(), totalMem));
		mRunningProcessCount = SystemInfoUtils.getRunningPocessCount(getActivity());
		//不包括本应用 (mRunningProcessCount-1)
		mTv_running_prcesscount.setText((String)getActivity().getString(R.string.Run_process) + (mRunningProcessCount-1) + (String)getActivity().getString(R.string.A));
		new Thread() {
			public void run() {
				mInfos = TaskInfoParser.getRunningTaskInfos(getActivity());
				mUserTaskInfos = new ArrayList<TaskInfo>();
				mSystemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo info : mInfos) {
					if (info.isUsertask()) {
						//不添加本应用
						if (!info.getPackname().equals(getActivity().getPackageName())) {
							mUserTaskInfos.add(info);
						}
					} else {
						mSystemTaskInfos.add(info);
					}
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mGif_bg.setVisibility(View.VISIBLE);
						mButtons.setVisibility(View.VISIBLE);
						mNeiCunView.setVisibility(View.VISIBLE);
						mGif_loding.setVisibility(View.GONE);
						mAdapter = new TaskManagerAdapter();
						mLv_taskmanger.setAdapter(mAdapter);
					}
				});
			};
		}.start();
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_size;
		CheckBox cb_status;
	}

	private class TaskManagerAdapter extends BaseAdapter {
		@Override
		public int getCount() {

			if (mShowsystem != false) {
				return mUserTaskInfos.size() + mSystemTaskInfos.size() + 1 + 1;
			} else {
				return mUserTaskInfos.size() + 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo info = null;
			View view = null;

			if (position == 0) {
				Button b = new Button(getActivity().getApplicationContext());
				b.setBackground(getResources().getDrawable(
						R.drawable.btn_pressed));
				b.setTextSize(16);
				b.setTextColor(Color.WHITE);
				b.setText((String)getActivity().getString(R.string.User_process) + mUserTaskInfos.size() + (String)getActivity().getString(R.string.The_operation_can_choose));
				Resources res = getResources();
				Drawable img = res.getDrawable(R.drawable.yh3);
				// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
				img.setBounds(0, 0, img.getMinimumWidth(),
						img.getMinimumHeight());
				b.setCompoundDrawables(img, null, null, null); // 设置左图标
				return b;
			} else if (position == (mUserTaskInfos.size() + 1)) {

				Button b = new Button(getActivity().getApplicationContext());
				b.setBackground(getResources().getDrawable(
						R.drawable.btn_pressed));
				b.setTextSize(16);
				b.setTextColor(Color.WHITE);
				b.setText((String)getActivity().getString(R.string.Sys_process) + mSystemTaskInfos.size() + (String)getActivity().getString(R.string.Please_careful_operation));
				Resources res = getResources();
				Drawable img = res.getDrawable(R.drawable.sz3);
				// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
				img.setBounds(0, 0, img.getMinimumWidth(),
						img.getMinimumHeight());
				b.setCompoundDrawables(img, null, null, null); // 设置左图标
				return b;
			}

			else if (position <= mUserTaskInfos.size()) {
				info = mUserTaskInfos.get(position - 1);
			} else {
				info = mSystemTaskInfos.get(position - 1 - mUserTaskInfos.size()
						- 1);
			}

			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {

				view = View.inflate(getActivity().getApplicationContext(),
						R.layout.item_task_manager, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_size = (TextView) view
						.findViewById(R.id.tv_task_size);
				holder.cb_status = (CheckBox) view
						.findViewById(R.id.cb_task_status);
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppname());
			holder.tv_size.setText((String)getActivity().getString(R.string.Occupation_memory)
					+ Formatter.formatFileSize(getActivity()
							.getApplicationContext(), info.getMemsize()));
			holder.cb_status.setChecked(info.isChecked());
//			if (info.getPackname().equals(getActivity().getPackageName())) {
//				// 就是我们自己。
//				holder.iv_icon.setVisibility(View.GONE);
//				holder.tv_name.setVisibility(View.GONE);
//				holder.cb_status.setVisibility(View.GONE);
//				holder.tv_size.setVisibility(View.GONE);
//			} else {
//				holder.cb_status.setVisibility(View.VISIBLE);
//			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			TaskInfo info;
			if (position == 0) {
				return null;
			} else if (position == (mUserTaskInfos.size() + 1)) {
				return null;
			} else if (position <= mUserTaskInfos.size()) {
				info = mUserTaskInfos.get(position - 1);
			} else {
				info = mSystemTaskInfos.get(position - 1 - mUserTaskInfos.size()
						- 1);
			}
			return info;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == mKillProcess.getId()) {// 清理进程
			int count = 0;
			long savemem = 0;

			List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();

			// 在遍历集合的时候 不可以修改集合的大小
			for (TaskInfo info : mUserTaskInfos) {
				if (info.isChecked()) {
					count++;
					savemem += info.getMemsize();
					((HomeActivity) getActivity()).getAm()
							.killBackgroundProcesses(info.getPackname());
					killedTaskInfos.add(info);
				}
			}

			for (TaskInfo info : mSystemTaskInfos) {
				if (info.isChecked()) {
					count++;
					savemem += info.getMemsize();
					((HomeActivity) getActivity()).getAm()
							.killBackgroundProcesses(info.getPackname());
					killedTaskInfos.add(info);
				}
			}
			for (TaskInfo info : killedTaskInfos) {
				if (info.isUsertask()) {
					mUserTaskInfos.remove(info);
				} else {
					mSystemTaskInfos.remove(info);
				}
			}
			mRunningProcessCount -= count;
			totalAvailMem += savemem;
			// 更新标题
			mTv_running_prcesscount
					.setText((String)getActivity().getString(R.string.Run_process) + (mRunningProcessCount-1) + (String)getActivity().getString(R.string.A));
			mTv_ram_info.setText((String)getActivity().getString(R.string.Available_the_total_memory)
					+ Formatter.formatFileSize(getActivity(), totalAvailMem)
					+ "/"
					+ Formatter.formatFileSize(getActivity(),
							SystemInfoUtils.getTotalMem()));
			toast(getString(R.string.Finish) + count + (String)getActivity().getString(R.string.Process_The_release_of)
							+ Formatter.formatFileSize(getActivity(), savemem)
							+ (String)getActivity().getString(R.string.Memory));
			// 刷新界面
			mAdapter.notifyDataSetChanged();

		} else if (v.getId() == mSelectOpposite.getId()) {// 自由选择/选择全部 item

			if (mShowsystem == false) {
				for (TaskInfo info : mUserTaskInfos) {
					if (info.getPackname().equals(
							getActivity().getPackageName())) {
						continue;
					}
					info.setChecked(!info.isChecked());
				}
				// 通知界面更新
				mAdapter.notifyDataSetChanged();
			} else if (mShowsystem == true) {
				for (TaskInfo info : mUserTaskInfos) {
					if (info.getPackname().equals(
							getActivity().getPackageName())) {
						continue;
					}
					info.setChecked(!info.isChecked());
				}
				for (TaskInfo info : mSystemTaskInfos) {
					info.setChecked(!info.isChecked());
				}
				// 通知界面更新
				mAdapter.notifyDataSetChanged();
			}

		} else if (v.getId() == mOpenSetting.getId()) {// 设置
			if (mShowsystem == false) {
				mOpenSetting.setText(R.string.Hidden_sys_process);
				mShowsystem = true;
			} else {
				mOpenSetting.setText(R.string.Show_sys_process);
				mShowsystem = false;
			}
			fillData();
			// 通知界面刷新
			mAdapter.notifyDataSetChanged();
			
		}

	}

}
