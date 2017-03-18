package com.yangsion.runandroid.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


import com.yangsion.runandroid.MyApplication;
import com.yangsion.runandroid.util.SimInfoUtil;
import com.yangsion.runandroid.util.SystemInfoUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
/**
 * super fragment
 * @author stabilit.yang
 *
 */
public abstract class BaseFragment extends Fragment {
	public static final String TAG = "BaseFragment";
	
	public View mBaseView;
	public Context mBaseContext;
	protected TelephonyManager tm;
	
	/**
	 * 总的可用内存
	 */
	protected static long totalAvailMem;

	
	/**
	 * 网络状态
	 * @return
	 */
	protected boolean isNetWorkAvailable(){
		return MyApplication.isNetWorkAvailable(mBaseContext);
	}
	
	
	public void toast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
	
	Toast mToast;

	public void showToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getActivity().getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}
	
	public void showToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity().getApplicationContext(), resId,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
		}
		mToast.show();
	}
	
	public static void showLog(String msg) {
		Log.i("smile", msg);
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tm = (TelephonyManager)MyApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
		mBaseContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		view = initView(inflater);
		return mBaseView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
//		initData(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 设置menu是否可见
	 */
	public void setMenuVisibility(boolean menuvisibile) {
		if (getView() != null) {
			getView().setVisibility(menuvisibile ? View.VISIBLE : View.GONE);
		}
	}

//	/**
//	 * 填充数据
//	 * 
//	 * @param savedInstanceState
//	 */
//	public abstract void initData(Bundle savedInstanceState);
//
//	/**
//	 * 初始化UI
//	 * 
//	 * @param inflater
//	 * @return
//	 */
//	public abstract View initView(LayoutInflater inflater);
	
	
	
	/**
	 * 获取手机信息列表
	 * @return
	 */
	protected String[] getInfos(){
		
		String[] info = {getSimInfo(),getCpuName(),getMaxCpuFreq(),getMinCpuFreq(),getCurCpuFreq(),getTotalMemory(),getAvailMemory(),getPhoneType(),getSDSpace(),getRomSpace()};
		
//		int i = 0;
//		for (int j = 0; j < info.length; j++) {
//			System.out.println(i+":"+ info[j]);
//			i++;
//		}
		
		return info;
	}
	
	
	
	/**@不用
	 * SD卡容量//目前实况适配不了(加上现大多数手机开始不支持外置sd)，取消
	 * @return
	 */
	private String getSDSpace() {
		
		if(Environment.getExternalStorageState().equals    
		        (Environment.MEDIA_MOUNTED)){    
		            File file=Environment.getExternalStorageDirectory();    
		            StatFs statFs=new StatFs(file.getPath());    
		            long blockSize=statFs.getBlockSize();    
		            long totalBlocks=statFs.getBlockCount();    
		            long availableBlocks=statFs.getAvailableBlocks();    
		                
		            String total = Formatter.formatFileSize(MyApplication.applicationContext, totalBlocks*blockSize);    
		            String available = Formatter.formatFileSize(MyApplication.applicationContext,availableBlocks*blockSize);    
		                
		            //Log.v(TAG, total+"/"+available);
		            return total+"/"+available;    
		        }else {    
		        	return "SD卡不存在！";    
		        }
	}
	
	
    
    
	/**
	 * ROM容量
	 * @return
	 */
	 private String getRomSpace() {
		 	File file=Environment.getDataDirectory();   
	        StatFs statFs=new StatFs(file.getPath());    
	        long blockSize=statFs.getBlockSize();    
	        long totalBlocks=statFs.getBlockCount();    
	        long availableBlocks=statFs.getAvailableBlocks();    
	            
	        String total =  Formatter.formatFileSize(MyApplication.applicationContext, totalBlocks*blockSize);    
	        String available = Formatter.formatFileSize(MyApplication.applicationContext,availableBlocks*blockSize);    
	            
	        //Log.v(TAG, total+"/"+available);
	        return total+"/"+available;
	    }
	 
	 
	
	/**
	 * @不用
	 * 手机制式  
	 * @return
	 */
	private String getPhoneType() {
		TelephonyManager tm = (TelephonyManager)MyApplication.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

		int PhoneType = tm.getPhoneType();
		if (PhoneType == TelephonyManager.PHONE_TYPE_CDMA) {
			return "电信";
			
		}
		else if(PhoneType == TelephonyManager.PHONE_TYPE_GSM){
			return "移动/联通";
			
		}
		else if(PhoneType == TelephonyManager.PHONE_TYPE_SIP){
			return "联通";
			
		}
		else if(PhoneType == TelephonyManager.PHONE_TYPE_NONE){
			return "未知";
			
		}
		
			return null;
	}
	
	
	/**@不用
	 * sim info
	 */
	private String getSimInfo() {
		SimInfoUtil info = new SimInfoUtil(MyApplication.applicationContext);
		info.getNativePhoneNumber();
		info.getProvidersName();
		return info.getProvidersName();
	}
	
	/**
	 * cpu info
	 * @return
	 */
	public List<String> getCpuInfo(){
		 List<String> strings = new ArrayList<String>();
		 FileReader fr;
			try {
				fr = new FileReader("/proc/cpuinfo");
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null) {
					strings.add(line);	
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return strings;
		}
	 /**
	  * mem info
	  * @return
	  */
	 public List<String> getMemInfo(){
		 List<String> strings = new ArrayList<String>();
		 FileReader fr;
		 try {
			 fr = new FileReader("/proc/meminfo");
			 BufferedReader br = new BufferedReader(fr);
			 String line = null;
			 while ((line = br.readLine()) != null) {
				 strings.add(line);	
			 }
			 br.close();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return strings;
	 }
	
	/**
	 * cpu name
	 * @return
	 */
	public String getCpuName() {
		 
		 return "（"+getNumCores()+MyApplication.getInstance().mCore + android.os.Build.CPU_ABI;
//	        try {
//	            FileReader fr = new FileReader("/proc/cpuinfo");
//	            BufferedReader br = new BufferedReader(fr);
//	            String text = br.readLine();
//	            String[] array = text.split(":\\s+", 2);
//	            if(array != null){
//	            	if (!array[1].isEmpty() || !"0".equals(array[1]) || !"".equals(array[1])) {
//	            		return "（"+getNumCores()+"核）" + array[1];
//	            	}
//	            } else {
//	            	return "（"+getNumCores()+"核）" + android.os.Build.CPU_ABI;
//	            }
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        return null;
	    }
	 
	 /**
		 * cpu核数
		 * @return
		 */
		private static int getNumCores() {
		    //Private Class to display only CPU devices in the directory listing
		    class CpuFilter implements FileFilter {
		        @Override
		        public boolean accept(File pathname) {
		            //Check if filename is "cpu", followed by a single digit number
		            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
		                return true;
		            }
		            return false;
		        }      
		    }

		    try {
		        //Get directory containing CPU info
		        File dir = new File("/sys/devices/system/cpu/");
		        //Filter to only list the devices we care about
		        File[] files = dir.listFiles(new CpuFilter());
		        //Return the number of cores (virtual CPU devices)
		        return files.length;
		    } catch(Exception e) {
		        e.printStackTrace();
		        //Default to return 1 core
		        return 1;
		    }
		}
	 
	    
	    /**
		    * 获取CPU最大频率（单位KHZ）
		    * @return
		    */
	    public static String getMaxCpuFreq() {
	        String result = "";
	        ProcessBuilder cmd;
	        try {
	        	
	            String[] args = {"/system/bin/cat",
	                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
	            cmd = new ProcessBuilder(args);
	            Process process = cmd.start();
	            InputStream in = process.getInputStream();
	            byte[] re = new byte[24];
	            while (in.read(re) != -1) {
	                result = result + new String(re);
	            }
	            in.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            result = "N/A";
	        }
	        return result.trim() + "Hz";
	    }

	   /**
	    * 获取CPU最小频率（单位KHZ）
	    * @return
	    */
	    public static String getMinCpuFreq() {
	        String result = "";
	        ProcessBuilder cmd;
	        try {
	            String[] args = {"/system/bin/cat",
	                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
	            cmd = new ProcessBuilder(args);
	            Process process = cmd.start();
	            InputStream in = process.getInputStream();
	            byte[] re = new byte[24];
	            while (in.read(re) != -1) {
	                result = result + new String(re);
	            }
	            in.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            result = "N/A";
	        }
	        return result.trim() + "Hz";
	    }

	  /**
	   * 实时获取CPU当前频率（单位KHZ）
	   * @return
	   */
	    public static String getCurCpuFreq() {
	        String result = "N/A";
	        try {
	            FileReader fr = new FileReader(
	                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
	            BufferedReader br = new BufferedReader(fr);
	            String text = br.readLine();
	            result = text.trim() + "Hz";
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	
	    
	    /**
	     * 获取手机运行内存大小/meminfo
	     *
	     * @return
	     */
	    private String getTotalMemory() {
	        return Formatter.formatFileSize(MyApplication.applicationContext, SystemInfoUtils.getTotalMem());// Byte转换为KB或者MB，内存大小规格化
	    }

	    /**
	     * 获取当前可用运行内存大小
	     *
	     * @return
	     */
	    private String getAvailMemory() {
	        return Formatter.formatFileSize(MyApplication.applicationContext, SystemInfoUtils.getAvailMem(MyApplication.applicationContext));
	    }

	
	
	
	
}
