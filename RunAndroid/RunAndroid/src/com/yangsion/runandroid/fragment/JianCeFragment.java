package com.yangsion.runandroid.fragment;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.yangsion.runandroid.MyApplication;
import com.yangsion.runandroid.R;
import com.yangsion.runandroid.activity.Login_RegisterActivity;
import com.yangsion.runandroid.adapter.CpuNamesAdapter;
import com.yangsion.runandroid.adapter.CpuValuesAdapter;
import com.yangsion.runandroid.adapter.MemNamesAdapter;
import com.yangsion.runandroid.adapter.MemValuesAdapter;
import com.yangsion.runandroid.domen.JsonInfo;
import com.yangsion.runandroid.domen.UserInfo;
import com.yangsion.runandroid.util.ReducedUtil;
/**
 * 手机检测  Test cell phone information
 * @author stabilit.yang
 */
@SuppressLint("NewApi")
public class JianCeFragment extends BaseFragment implements OnClickListener {
	public static final String TAG = "JianCeFragment";

	protected static final int MSG1 = 0;
	protected static final int MSG2 = 1;
//	private Dialog mDialog;
//	private GifView mGif_bg;
	private ListView mListView, mList_cpu, mList_cpu2, mList_mem, mList_mem2;
	private ScrollView mList_sys_cv, mList_bat_cv;
	private HorizontalScrollView mList_sensor_cv;
	private TextView mList_sys, mList_bat, mList_sys2, mList_bat2, mList_sim_tv1, mList_sim_tv2;
	private FrameLayout mKsjc_frame;
	private ImageView mImageViewJdt;
	private Button mBtn_sys, mBtn_cpu, mBtn_mem, mBtn_bat, mBtn_more, mBtn_sim, mBtn_sensor;
	private TextView mTextViewJds, mTextViewSmz, mTexts, mBtn_back, mBtn_back2;//mTvt, mTvm,
	private ProgressBar mProgressBar;
	private RelativeLayout mRelativeLayoutJdBg, mBtnrl;
	private LinearLayout mList_cpu_cv, mList_mem_cv, mMore_view, mList_sensor, mList_sim_cv;
	private Button mButtonJc, mBtnjc;//, mBtnpf
	private Animation mTa;
	private JsonInfo mInfos;
	private ListInfoAdapter mListInfoAdapter;
	private int mPro = 0;
	private int mText = -1;
	private boolean mBc = false;
	private boolean mSys = false;
	private boolean mCpu = false;
	private boolean mMem = false;
	private boolean mBat = false;
	private boolean mSim = false;
	private boolean mSensor = false;
	private List<String> mCpuInfos1 = null;
	private List<String> mCpuInfos2 = null;
	private List<String> mMemInfos1 = null;
	private List<String> mMemInfos2 = null;

	// 创建一个Handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 处理消息
			switch (msg.what) {
			case MSG1:
				mProgressBar.setProgress(mPro);
				mTextViewJds.setText(Integer.toString(mPro*4-4) + "%");
				mTexts.setText(VALUE[mText]);
				break;
			case MSG2:
				mRelativeLayoutJdBg.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				mBtnrl.setVisibility(View.VISIBLE);
				mPro = 0;
				mText = -1;
				break;
			default:
				break;
			}
		}
	};
	
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_jiance, null);

	}
	

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mListView = (ListView) getActivity().findViewById(R.id.list);
		mList_sys_cv = (ScrollView) getActivity().findViewById(R.id.list_sys_cv);
		mList_cpu_cv = (LinearLayout) getActivity().findViewById(R.id.list_cpu_cv);
		mList_mem_cv = (LinearLayout) getActivity().findViewById(R.id.list_mem_cv);
		mMore_view = (LinearLayout) getActivity().findViewById(R.id.more_view);
		mList_bat_cv = (ScrollView) getActivity().findViewById(R.id.list_bat_cv);
		mList_sensor_cv = (HorizontalScrollView) getActivity().findViewById(R.id.list_sensor_cv);
		mKsjc_frame = (FrameLayout) getActivity().findViewById(R.id.ksjc_frame);

		mBtn_sys = (Button) getActivity().findViewById(R.id.btn_sys);
		mBtn_cpu = (Button) getActivity().findViewById(R.id.btn_cpu);
		mBtn_mem = (Button) getActivity().findViewById(R.id.btn_mem);
		mBtn_bat = (Button) getActivity().findViewById(R.id.btn_bat);
		mBtn_more = (Button) getActivity().findViewById(R.id.btn_more);
		mBtn_sim = (Button) getActivity().findViewById(R.id.btn_sim);
		mBtn_sensor = (Button) getActivity().findViewById(R.id.btn_sensor);
		mBtn_back = (TextView) getActivity().findViewById(R.id.btn_back);
		mBtn_back2 = (TextView) getActivity().findViewById(R.id.btn_back2);
		
		mButtonJc = (Button) getActivity().findViewById(R.id.ksjc);
		mBtnjc = (Button) getActivity().findViewById(R.id.btnjc);
		//mBtnpf = (Button) getActivity().findViewById(R.id.btnpf);
		mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

		mList_sys = (TextView) getActivity().findViewById(R.id.list_sys);
		mList_sys2 = (TextView) getActivity().findViewById(R.id.list_sys2);
		mList_cpu = (ListView) getActivity().findViewById(R.id.list_cpu);
		mList_cpu2 = (ListView) getActivity().findViewById(R.id.list_cpu2);
		mList_mem = (ListView) getActivity().findViewById(R.id.list_mem);
		mList_mem2 = (ListView) getActivity().findViewById(R.id.list_mem2);
		mList_bat = (TextView) getActivity().findViewById(R.id.list_bat);
		mList_bat2 = (TextView) getActivity().findViewById(R.id.list_bat2);
		mList_sim_tv1 = (TextView) getActivity().findViewById(R.id.list_sim_tv1);
		mList_sim_tv2 = (TextView) getActivity().findViewById(R.id.list_sim_tv2);
		mList_sim_cv = (LinearLayout) getActivity().findViewById(R.id.list_sim_cv);
		mList_sensor = (LinearLayout) getActivity().findViewById(R.id.list_sensor);
		
		mTextViewSmz = (TextView) getActivity().findViewById(R.id.smz);
		mTexts = (TextView) getActivity().findViewById(R.id.texts);
		mTextViewJds = (TextView) getActivity().findViewById(R.id.numberText);
		mImageViewJdt = (ImageView) getActivity().findViewById(R.id.jdt);
		mRelativeLayoutJdBg = (RelativeLayout) getActivity().findViewById(R.id.jdt_bg);
		mBtnrl = (RelativeLayout) getActivity().findViewById(R.id.btnrl);
		
		
		mButtonJc.setOnClickListener(this);
		mBtnjc.setOnClickListener(this);
		//mBtnpf.setOnClickListener(this);
		mBtn_sys.setOnClickListener(this);
		mBtn_cpu.setOnClickListener(this);
		mBtn_mem.setOnClickListener(this);
		mBtn_bat.setOnClickListener(this);
		mBtn_more.setOnClickListener(this);
		mBtn_sim.setOnClickListener(this);
		mBtn_sensor.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		mBtn_back2.setOnClickListener(this);
		
		addListFooter();
		mListInfoAdapter = new ListInfoAdapter();
		mListView.setAdapter(mListInfoAdapter);
		
		VALUE[8] = infos[5].toString() + "/" + infos[6].toString() + (String)getActivity().getString(R.string.This_moment_value);
		mTa = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		// 设置滚动条可见
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		cpuInfo();
		memInfo();
		simInfo();
	}



	/**
	 * 开发人员选项btn
	 */
	private void addListFooter() {
		View view = View.inflate(mBaseContext, R.layout.list_footer, null);
		((Button) view.findViewById(R.id.btn_kfxx)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//if (未登录) {去登陆界面
				UserInfo myUser = BmobUser.getCurrentUser(getActivity(), UserInfo.class);
				if (myUser != null) {//登录过
					mKsjc_frame.setVisibility(View.VISIBLE);
				}
				else{
					toast(getString(R.string.Not_logged_in));
					startActivity(new Intent(getActivity(), Login_RegisterActivity.class));
				}
			}
		});
		
		mListView.addFooterView(view);
	}
	
	
	/**
	 * mem信息/开发人员参考信息
	 */
	private void memInfo() {
		mMemInfos1 = new ArrayList<String>();
		mMemInfos2 = new ArrayList<String>();
		List<String> memInfos = getMemInfo();
		for (String memInfo : memInfos) {
			mMemInfos1.add(memInfo.substring(0,memInfo.indexOf(":")+1));
			mMemInfos2.add(memInfo.substring(memInfo.indexOf(":")+1).trim());
		}
		
		mList_mem.setAdapter(new MemNamesAdapter() {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mMemInfos1.size();
			}
			
			ViewHolder holder = null;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					holder = new ViewHolder();
					LayoutInflater mInflater = LayoutInflater.from(getActivity());
					convertView = mInflater.inflate(R.layout.item_list_mem_names, null);
					holder.mem_names = (TextView) convertView.findViewById(R.id.mem_name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.mem_names.setText(mMemInfos1.get(position));

				return convertView;
			}
		});
		mList_mem2.setAdapter(new MemValuesAdapter() {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mMemInfos2.size();
			}
			
			ViewHolder holder = null;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					holder = new ViewHolder();
					LayoutInflater mInflater = LayoutInflater.from(getActivity());
					convertView = mInflater.inflate(R.layout.item_list_mem_values, null);
					holder.mem_values = (TextView) convertView.findViewById(R.id.mem_value);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.mem_values.setText(mMemInfos2.get(position));
				
				return convertView;
			}
		});
	}
	
	
	/**
	 * cpu信息/开发人员参考信息
	 */
	private void cpuInfo() {
		mCpuInfos1 = new ArrayList<String>();
		mCpuInfos2 = new ArrayList<String>();
		List<String> cpuInfos = getCpuInfo();
		for (String cpuInfo : cpuInfos) {
			mCpuInfos1.add(cpuInfo.substring(0,cpuInfo.indexOf(":")+1));
			mCpuInfos2.add(cpuInfo.substring(cpuInfo.indexOf(":")+1));
		}
		mList_cpu.setAdapter(new CpuNamesAdapter() {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCpuInfos1.size();
			}
			
			ViewHolder holder = null;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					holder = new ViewHolder();
					LayoutInflater mInflater = LayoutInflater.from(getActivity());
					convertView = mInflater.inflate(R.layout.item_list_cpu_names, null);
					holder.cpu_names = (TextView) convertView.findViewById(R.id.cpu_name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.cpu_names.setText(mCpuInfos1.get(position));

				return convertView;
			}
		});
		mList_cpu2.setAdapter(new CpuValuesAdapter() {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCpuInfos2.size();
			}
			
			ViewHolder holder = null;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					holder = new ViewHolder();
					LayoutInflater mInflater = LayoutInflater.from(getActivity());
					convertView = mInflater.inflate(R.layout.item_list_cpu_values, null);
					holder.cpu_values = (TextView) convertView.findViewById(R.id.cpu_value);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.cpu_values.setText(mCpuInfos2.get(position));

				return convertView;
			}
		});
	}
	
	/**
	 * sim信息/开发人员参考信息
	 */
	private void simInfo() {
		
		String DeviceId = null;
		int CallState = 0;
		int DataState = 0;
		String DeviceSoftwareVersion = null;
		String GroupIdLevel1 = null;
		String Line1Number = null;
		String MmsUAProfUrl = null;
		String MmsUserAgent = null;
		String NetworkCountryIso = null;
		String NetworkOperator = null;
		String NetworkOperatorName = null;
		int NetworkType = 0;
		int PhoneType = 0;
		String SimCountryIso = null;
		String SimOperator = null;
		String SimOperatorName = null;
		String SimSerialNumber = null;
		int SimState = 0;
		String SubscriberId = null;
		String VoiceMailAlphaTag = null;
		String VoiceMailNumber = null;
		String CellLocation = null;
		
		if (tm.getDeviceId() != null) {
			DeviceId = tm.getDeviceId();
		}
		CallState = tm.getCallState();
		DataState = tm.getDataState();
		if (tm.getDeviceSoftwareVersion() != null) {
			DeviceSoftwareVersion = tm.getDeviceSoftwareVersion();
		}
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 18) {
			if (tm.getGroupIdLevel1() != null) {
				GroupIdLevel1 = tm.getGroupIdLevel1();
			}
		}
		if (tm.getLine1Number() != null) {
			
			Line1Number = tm.getLine1Number();
		}
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 19) {
			if (tm.getMmsUAProfUrl() != null) {
				MmsUAProfUrl = tm.getMmsUAProfUrl();
			}
			if (tm.getMmsUserAgent() != null) {
				MmsUserAgent = tm.getMmsUserAgent();
			}
		}
		if (tm.getNetworkCountryIso() != null) {
			
			NetworkCountryIso = tm.getNetworkCountryIso();
		}
		if (tm.getNetworkOperator() != null) {
	
			NetworkOperator = tm.getNetworkOperator();
		}
		if (tm.getNetworkOperatorName() != null) {
			
			NetworkOperatorName = tm.getNetworkOperatorName();
		}
			NetworkType = tm.getNetworkType();
			PhoneType = tm.getPhoneType();
		if (tm.getSimCountryIso() != null) {
			
			SimCountryIso = tm.getSimCountryIso();
		}
		if (tm.getSimOperator() != null) {
			
			SimOperator = tm.getSimOperator();
		}
		if (tm.getSimOperatorName() != null) {
			
			SimOperatorName = tm.getSimOperatorName();
		}
		if (tm.getSimSerialNumber() != null) {
			
			SimSerialNumber = tm.getSimSerialNumber();
		}
		SimState = tm.getSimState();
		if (tm.getSubscriberId() != null) {
			SubscriberId = tm.getSubscriberId();
		}
		if (tm.getVoiceMailAlphaTag() != null) {
			
			VoiceMailAlphaTag = tm.getVoiceMailAlphaTag();
		}
		if (tm.getVoiceMailNumber() != null) {
			
			VoiceMailNumber = tm.getVoiceMailNumber();
		}
		if (tm.getCellLocation().toString() != null) {
			CellLocation = tm.getCellLocation().toString();
		}
		
		
		mList_sim_tv1.setText("DeviceId"+"：\n"+
				"CallState"+"：\n"+
				"DataState"+"：\n"+
				"DeviceSoftwareVersion"+"：\n"+
				"GroupIdLevel1"+"：\n"+
				"Line1Number"+"：\n"+
				"MmsUAProfUrl"+"：\n"+
				"MmsUserAgent"+"：\n"+
				"NetworkCountryIso"+"：\n"+
				"NetworkOperator"+"：\n"+
				"NetworkOperatorName"+"：\n"+
				"NetworkType"+"：\n"+
				"PhoneType"+"：\n"+
				"SimCountryIso"+"：\n"+
				"SimOperator"+"：\n"+
				"SimOperatorName"+"：\n"+
				"SimSerialNumber"+"：\n"+
				"SimState"+"：\n"+
				"SubscriberId"+"：\n"+
				"VoiceMailAlphaTag"+"：\n"+
				"VoiceMailNumber"+"：\n"+
				"CellLocation"+"：\n"
				);
		mList_sim_tv2.setText(DeviceId+"\n"+
				CallState+"\n"+
				DataState+"\n"+
				DeviceSoftwareVersion+"\n"+
				GroupIdLevel1+"\n"+
				Line1Number+"\n"+
				MmsUAProfUrl+"\n"+
				MmsUserAgent+"\n"+
				NetworkCountryIso+"\n"+
				NetworkOperator+"\n"+
				NetworkOperatorName+"\n"+
				NetworkType+"\n"+
				PhoneType+"\n"+
				SimCountryIso+"\n"+
				SimOperator+"\n"+
				SimOperatorName+"\n"+
				SimSerialNumber+"\n"+
				SimState+"\n"+
				SubscriberId+"\n"+
				VoiceMailAlphaTag+"\n"+
				VoiceMailNumber+"\n"+
				CellLocation+"\n"
				);
		
	}

	
	
	/**
	 * 系统信息/开发人员参考信息
	 */
	private void systemInfo() {
		
		String MANUFACTURER = null;
		String BOARD = null;
		String BOOTLOADER = null;
		String BRAND = null;
		String CPU_ABI = null;
		String CPU_ABI2 = null;
		String DEVICE = null;
		String DISPLAY = null;
		String FINGERPRINT = null;
		String HARDWARE = null;
		String HOST = null;
		String ID = null;
		String MODEL = null;
		String PRODUCT = null;
		String RADIO = null;
		String SERIAL = null;
		String TAGS = null;
		Long TIME = 0L;
		String TYPE = null;
		String UNKNOWN = null;
		String USER = null;
		String[] SUPPORTED_32_BIT_ABIS = null;
		String[] SUPPORTED_64_BIT_ABIS = null;
		String[] SUPPORTED_ABIS = null;
		
		try {
				MANUFACTURER = android.os.Build.MANUFACTURER;
				BOARD = android.os.Build.BOARD;
				BOOTLOADER = android.os.Build.BOOTLOADER;
				BRAND = android.os.Build.BRAND;
				CPU_ABI = android.os.Build.CPU_ABI;
				CPU_ABI2 = android.os.Build.CPU_ABI2;
				DEVICE = android.os.Build.DEVICE;
				DISPLAY = android.os.Build.DISPLAY;
				FINGERPRINT = android.os.Build.FINGERPRINT;
				HARDWARE = android.os.Build.HARDWARE;
				HOST = android.os.Build.HOST;
				ID = android.os.Build.ID;
				MODEL = android.os.Build.MODEL;
				PRODUCT = android.os.Build.PRODUCT;
				RADIO = android.os.Build.RADIO;
				SERIAL = android.os.Build.SERIAL;
				TAGS = android.os.Build.TAGS;
				TIME = android.os.Build.TIME;
				TYPE = android.os.Build.TYPE;
				UNKNOWN = android.os.Build.UNKNOWN;
				USER = android.os.Build.USER;
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 21) {
					SUPPORTED_32_BIT_ABIS = android.os.Build.SUPPORTED_32_BIT_ABIS;
					SUPPORTED_64_BIT_ABIS = android.os.Build.SUPPORTED_64_BIT_ABIS;
					SUPPORTED_ABIS = android.os.Build.SUPPORTED_ABIS;
				}
			
			VALUE[0] = android.os.Build.MANUFACTURER + "/" + android.os.Build.BRAND;
			VALUE[1] = android.os.Build.MODEL;
			VALUE[2] = android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK + ")";
			
			
			mList_sys.setText(
					"MANUFACTURER：\n"+
					"BOARD：\n"+
					"BOOTLOADER：\n"+
					"BRAND：\n"+
					"CPU_ABI：\n"+
					"CPU_ABI2：\n"+
					"DEVICE：\n"+
					"DISPLAY：\n"+
					"FINGERPRINT：\n"+
					"HARDWARE：\n"+
					"HOST：\n"+
					"ID：\n"+
					"MODEL：\n"+
					"PRODUCT：\n"+
					"RADIO：\n"+
					"SERIAL：\n"+
					"TAGS：\n"+
					"TIME：\n"+
					"TYPE：\n"+
					"UNKNOWN：\n"+
					"USER：\n"+
					"SUPPORTED_32_BIT_ABIS：\n"+
					"SUPPORTED_64_BIT_ABIS：\n"+
					"SUPPORTED_ABIS：\n"
			);
			
			mList_sys2.setText(
					MANUFACTURER+"\n"+
					BOARD+"\n"+
					BOOTLOADER+"\n"+
					BRAND+"\n"+
					CPU_ABI+"\n"+
					CPU_ABI2+"\n"+
					DEVICE+"\n"+
					DISPLAY+"\n"+
					FINGERPRINT+"\n"+
					HARDWARE+"\n"+
					HOST+"\n"+
					ID+"\n"+
					MODEL+"\n"+
					PRODUCT+"\n"+
					RADIO+"\n"+
					SERIAL+"\n"+
					TAGS+"\n"+
					TIME+"\n"+
					TYPE+"\n"+
					UNKNOWN+"\n"+
					USER+"\n"+
					SUPPORTED_32_BIT_ABIS+"\n"+
					SUPPORTED_64_BIT_ABIS+"\n"+
					SUPPORTED_ABIS+"\n");
		} 
		catch (Error e) {
			e.printStackTrace();
		} 
		
	}
	
	
	/** 
     * 判断WiFi功能是否可用 
     * 
     * @param inContext 
     * @return 
     */  
	private void isWiFiActive() {  
		WifiManager wifiManager = (WifiManager) mBaseContext.getSystemService(Context.WIFI_SERVICE);
		 //通过反射调用设置热点  
        Method method;
		try {
			method = wifiManager.getClass().getMethod(  
			        "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			if (!method.getName().isEmpty()) {
				VALUE[19] = (String)getActivity().getString(R.string.Support);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
        Context apContext = mBaseContext.getApplicationContext();  
        ConnectivityManager connectivity = (ConnectivityManager) apContext  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if (info != null) {  
                for (int i = 0; i < info.length; i++) {  
                    if (info[i].getTypeName().equals("WIFI") || info[i].getTypeName().equals("wifi")) {  
                    	VALUE[18] = (String)getActivity().getString(R.string.Support);
                    }  
                }  
            }  
        }  
         
    }
	
	

	/**
	 * 感应Info // 通信 // 电池
	 */
	private void inductionInfo() {
		
		
		BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
		if (blueadapter.getName() != null) {// 蓝牙
			VALUE[20] = (String)getActivity().getString(R.string.Support);
		}
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter != null) {// NFC近场通讯
			VALUE[21] = (String)getActivity().getString(R.string.Support);
		}
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 14) {// OTG
			VALUE[22] = (String)getActivity().getString(R.string.Support);
		} else {
			VALUE[22] = (String)getActivity().getString(R.string.Does_not_support);
		}

		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		try {// GPS
			locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
			VALUE[11] = (String)getActivity().getString(R.string.Support);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ContentResolver cv = getActivity().getContentResolver();
		String strTimeFormat2 = android.provider.Settings.System.getString(cv,
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);
		if (strTimeFormat2.equals("0")) {// 光线
			VALUE[13] = (String)getActivity().getString(R.string.Does_not_support);
		} else {
			VALUE[13] = (String)getActivity().getString(R.string.Support);
		}

		// 实例化传感器管理者
		SensorManager mSensorManager = (SensorManager) getActivity()
				.getSystemService(Context.SENSOR_SERVICE);
		// 得到设置支持的所有传感器的List
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensorList) {
			if (mBc == false) {//是否保存过
				//添加到sensor info
				TextView tv = new TextView(getActivity());
				tv.setText(sensor.getName());
				mList_sensor.addView(tv);
			}
			// 判断是否支持相应的感应功能
			if (sensor.getName().indexOf("Gravity") != -1 || sensor.getName().indexOf("gravity") != -1) {//重力Gravity Sensor /旋转Rotation Sensor
				VALUE[12] = (String)getActivity().getString(R.string.Support);
			} else if (sensor.getName().indexOf("Proximity") != -1 || sensor.getName().indexOf("proximity") != -1) {// GP2A Proximity sensor 距离
				VALUE[14] = (String)getActivity().getString(R.string.Support);
			} else if (sensor.getName().indexOf("Magnetic") != -1
					|| sensor.getName().indexOf("Field") != -1 || sensor.getName().indexOf("magnetic") != -1
					|| sensor.getName().indexOf("field") != -1) {// BOSCH BMC150 Magnetic Field Sensor 磁场
				VALUE[15] = (String)getActivity().getString(R.string.Support);
			} else if (sensor.getName().indexOf("Orientation") != -1 || sensor.getName().indexOf("orientation") != -1) {// BOSCH Orientation Sensor 方向
				VALUE[16] = (String)getActivity().getString(R.string.Support);
			} else if (sensor.getName().indexOf("Acceleration") != -1 || sensor.getName().indexOf("accel") != -1  || sensor.getName().indexOf("acceleration") != -1) {// BOSCH BMC150 Acceleration Sensor 加速度
				VALUE[17] = (String)getActivity().getString(R.string.Support);
			}
		}
		mBc = true;//已保存
		
		for (int i = 0; i < VALUE.length; i++) {
			if (VALUE[i].equals("")) {
				VALUE[i] = (String)getActivity().getString(R.string.Does_not_support);
			}
		}

	}
	
	public String[] infos = getInfos();
	public String[] VALUE = {

			"",// 厂商
			"",// 型号
			"",// 系统版本
			// cpu info
			infos[1].toString(),// 加上核数
			infos[2].toString(), 
			infos[3].toString(), 
			infos[4].toString(),
			// android.os.Build.CPU_ABI+"/"+android.os.Build.CPU_ABI2,//CPU
//			infos[7].toString(),// 手机制式支持
//			infos[0].toString(),// 运营商
			infos[9].toString(),// 机身内存
			"no",// 运行内存
			"",// 屏幕尺寸
			"",// PPI（像素密度/每英寸）分辨率
			"",// GPS模块 [13]
			"",// 重力感应 [14]
			"",// 光线感应 [15]
			"",// 距离感应 [16]
			"",// 磁场感应 [17]
			"",// 方向感应 [18]
			"",// 加速度感应 [19]
			"",// WiFi
			"",// WiFi热点
			"",// 蓝牙
			"",// NFC（近场通讯）
			"",// OTG
			"",// 电池类型
			"",// 电池状态
			"",// 电池温度
//			"",// 电池容量（mAh）嵌入计算apk 未ko
	};

	
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (mListInfoAdapter != null) {
			mListInfoAdapter.notifyDataSetChanged();
		} 
		getActivity().registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	/**
	 * 电池信息/开发人员参考信息
	 * “status”（int类型）…状态，定义值是BatteryManager.BATTERY_STATUS_XXX。
	 * “health”（int类型）…健康，定义值是BatteryManager.BATTERY_HEALTH_XXX。
	 * “present”（boolean类型）
	 * “level”（int类型）…电池剩余容量 
	 * “scale”（int类型）…电池最大值。通常为100。
	 * “icon-small”（int类型）…图标ID。
	 * “plugged”（int类型）…连接的电源插座，定义值是BatteryManager.BATTERY_PLUGGED_XXX。
	 * “voltage”（int类型）…mV。
	 * “temperature”（int类型）…温度，0.1度单位。例如 表示197的时候，意思为19.7度。
	 * “technology”（String类型）…电池类型，例如，Li-ion等等。
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int status = intent.getIntExtra("status", 0);
				int health = intent.getIntExtra("health", 0);
				boolean present = intent.getBooleanExtra("present", false);
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 0);
				int icon_small = intent.getIntExtra("icon-small", 0);
				int plugged = intent.getIntExtra("plugged", 0);
				int voltage = intent.getIntExtra("voltage", 0);
				int temperature = intent.getIntExtra("temperature", 0);
				
				String technology = intent.getStringExtra("technology");

				String statusString = "";

				switch (status) {
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					statusString = "unknown";
					break;
				case BatteryManager.BATTERY_STATUS_CHARGING:
					statusString = "charging";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					statusString = "discharging";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					statusString = "not charging";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					statusString = "full";
					break;
				}

				String healthString = "";

				switch (health) {
				case BatteryManager.BATTERY_HEALTH_UNKNOWN:
					healthString = "unknown";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					healthString = "good";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					healthString = "overheat";
					break;
				case BatteryManager.BATTERY_HEALTH_DEAD:
					healthString = "dead";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					healthString = "voltage";
					break;
				case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
					healthString = "unspecified failure";
					break;
				}

				String acString = "";

				switch (plugged) {
				case BatteryManager.BATTERY_PLUGGED_AC:
					acString = "plugged ac";
					break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					acString = "plugged usb";
					break;
				}

				String valueOf = String.valueOf(temperature);
				String newTemperature = valueOf.substring(0, valueOf.length() - 1) + "." + valueOf.substring(valueOf.length() - 1, valueOf.length());
				VALUE[23] = technology;
				VALUE[24] = healthString;
				VALUE[25] = newTemperature+"°";
				
				mList_bat.setText(
						"status:\n"+
						"health:\n"+
						"present:\n"+
						"level:\n"+
						"scale:\n"+
						"icon_small:\n"+
						"plugged:\n"+
						"voltage:\n"+
						"temperature:\n"+
						"technology:\n"
						);
				mList_bat2.setText(
						statusString+"\n"+
						healthString+"\n"+
						String.valueOf(present)+"\n"+
						String.valueOf(level)+"\n"+
						String.valueOf(scale)+"\n"+
						String.valueOf(icon_small)+"\n"+
						acString+"\n"+
						String.valueOf(voltage)+"\n"+
						String.valueOf(temperature)+"\n"+
						technology
						);
			}
		}
	};
	
	
	
	/**
	 * 屏幕尺寸/分辨率
	 */
	@SuppressLint("NewApi")
	private void getScreenSizeOfDevice() {
		Point point = new Point();
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 13 && Integer.parseInt(android.os.Build.VERSION.SDK) < 17) {
			getActivity().getWindowManager().getDefaultDisplay().getSize(point);
		} else if(Integer.parseInt(android.os.Build.VERSION.SDK) >= 17) {
			getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);
		} 
		DisplayMetrics dm = getResources().getDisplayMetrics();
		double x = Math.pow(point.x / dm.xdpi, 2);
		double y = Math.pow(point.y / dm.ydpi, 2);
		double screenInches1 = Math.sqrt(x + y);
		DecimalFormat df1 = new DecimalFormat("0.0");
		String ScreenSize1 = df1.format(screenInches1);
		///////////////////////////尺寸适配未ko////////////////////////////////////
		VALUE[9] = ScreenSize1 + (String)getActivity().getString(R.string.Accurate_fit_resolution_other_resolution_are_trying_to_fit);
		VALUE[10] = point.toString();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mButtonJc.getId()) {//检测
			if (MyApplication.isNetWorkAvailable(getActivity()) == true) {
				mProgressBar.setMax(ReducedUtil.NAME.length);
				systemInfo();
				isWiFiActive();
				inductionInfo();
				getScreenSizeOfDevice();
				start();
				Animation ta1 = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);
				Animation ta2 = AnimationUtils.loadAnimation(getActivity(),R.anim.set1);
				mButtonJc.setVisibility(View.GONE);
				mRelativeLayoutJdBg.setVisibility(View.VISIBLE);
				mImageViewJdt.startAnimation(mTa);
				mTextViewSmz.startAnimation(ta1);
				mTexts.startAnimation(ta2);
				mListInfoAdapter.notifyDataSetChanged();
			}
			else {
				toast(getString(R.string.On_network));
			}
			
		} 
		else if (v.getId() == mBtnjc.getId()) {//重新检测
			if (MyApplication.isNetWorkAvailable(getActivity()) == true) {
			mListView.setVisibility(View.GONE);
			mBtnrl.setVisibility(View.GONE);
			systemInfo();
			isWiFiActive();
			inductionInfo();
			getScreenSizeOfDevice();
			start();
			Animation ta1 = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);
			Animation ta2 = AnimationUtils.loadAnimation(getActivity(),R.anim.set1);
			mButtonJc.setVisibility(View.GONE);
			mRelativeLayoutJdBg.setVisibility(View.VISIBLE);
			mImageViewJdt.startAnimation(mTa);
			mTextViewSmz.startAnimation(ta1);
			mTexts.startAnimation(ta2);
			mListInfoAdapter.notifyDataSetChanged();
			}
			else {
				toast(getString(R.string.On_network));
			}
		} 
//		else if (v.getId() == mBtnpf.getId()) {//评分
//			mDialog = new AlertDialog.Builder(getActivity()).create();
//			mDialog.show();// 这方法必须在setContentView之前用
//			mDialog.getWindow().setContentView(R.layout.pf_dialog2);
//			loadPF();
//		} 
		else if (v.getId() == mBtn_sys.getId()){//sys信息
			if (mSys == false) {
				mSys = true;
				mList_sys_cv.setVisibility(View.VISIBLE);
				return;
			} else {
				mSys = false;
				mList_sys_cv.setVisibility(View.GONE);
			}
 		}
		else if (v.getId() == mBtn_cpu.getId()){//cpu信息
			if (mCpu == false) {
				mCpu = true;
				mList_cpu_cv.setVisibility(View.VISIBLE);
			} else {
				mCpu = false;
				mList_cpu_cv.setVisibility(View.GONE);
			}
		}
		else if (v.getId() == mBtn_mem.getId()){//mem信息
			if (mMem == false) {
				mMem = true;
				mList_mem_cv.setVisibility(View.VISIBLE);
			} else {
				mMem = false;
				mList_mem_cv.setVisibility(View.GONE);
			}
		}
		else if (v.getId() == mBtn_bat.getId()){//bat信息
			if (mBat == false) {
				mBat = true;
				mList_bat_cv.setVisibility(View.VISIBLE);
			} else {
				mBat = false;
				mList_bat_cv.setVisibility(View.GONE);
			}
		}
		else if (v.getId() == mBtn_more.getId()){//更多信息
			mMore_view.setVisibility(View.VISIBLE);	
		}
		else if (v.getId() == mBtn_sim.getId()){//sim信息
			//判断sim卡是否插入#未ko
			if (mSim == false) {
				mSim = true;
				mList_sim_cv.setVisibility(View.VISIBLE);
			} else {
				mSim = false;
				mList_sim_cv.setVisibility(View.GONE);
			}
		}
		else if (v.getId() == mBtn_sensor.getId()){//sensor信息
			if (mSensor == false) {
				mSensor = true;
				mList_sensor_cv.setVisibility(View.VISIBLE);
			} else {
				mSensor = false;
				mList_sensor_cv.setVisibility(View.GONE);
			}
		}
		else if (v.getId() == mBtn_back.getId()){//返回
			mKsjc_frame.setVisibility(View.GONE);
		}
		else if (v.getId() == mBtn_back2.getId()){//返回
			mMore_view.setVisibility(View.GONE);
		}
	}

//	/**
//	 * 加载评分
//	 */
//	private void loadPF() {
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				
//			}
//		}).start();
//	}

	/**
	 * 开始扫描
	 */
	private void start() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int max = mProgressBar.getMax();
				try {
					// 子线程循环间隔消息
					while (mPro < max) {
						mPro += 1;
						mText += 1;
						int time = 0;
						Random random = new Random();
						for (int i = 66; i <= 666; i++) {
							time = random.nextInt(i);
						}
						Thread.sleep(time);
						
						Message msg = new Message();
						msg.what = MSG1;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (mPro == max) {
					
					Message msg = new Message();
					msg.what = MSG2;
					
				}
			}
		}).start();
	}

	public class ListInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// notifyDataSetChanged();
			return ReducedUtil.NAME.length;
		}

		@Override
		public Object getItem(int position) {
			return getItem(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(getActivity());
				convertView = mInflater.inflate(R.layout.item_list_iphone_info,null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.value = (TextView) convertView.findViewById(R.id.value);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(ReducedUtil.NAME[position]);
			holder.value.setText(VALUE[position]);

			return convertView;
		}
	}

	private static class ViewHolder {
		TextView name;
		TextView value;
		TextView cpu_names;
		TextView mem_names;
		TextView cpu_values;
		TextView mem_values;
	}

}
