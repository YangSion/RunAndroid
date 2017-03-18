package com.yangsion.runandroid.util;
//listValues.add(tm.getDeviceId());//获取设备编号
//listValues.add(tm.getSimCountryIso());//获取SIM卡国别
//listValues.add(tm.getSimSerialNumber());//获取SIM卡序列号    
//listValues.add(simState[tm.getSimState()]);//获取SIM卡状态
//listValues.add((tm.getDeviceSoftwareVersion()!=null?tm.getDeviceSoftwareVersion():"未知"));    //获取软件版本
//listValues.add(tm.getNetworkOperator());//获取网络运营商代号
//listValues.add(tm.getNetworkOperatorName());//获取网络运营商名称
//listValues.add(phoneType[tm.getPhoneType()]);//获取手机制式
//listValues.add(tm.getCellLocation().toString());//获取设备当前位置
import android.content.Context;  
import android.telephony.TelephonyManager;  
  
/** 
 * Wrapper class read the Sim card information
 * @copy 读取Sim卡信息<BR> 
 * PS： 必须加入各种权限 <BR> 
 */  
public class SimInfoUtil {  
    /** 
     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。 
     * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类 
     * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。 
     */  
    private TelephonyManager telephonyManager;  
    /** 
     * 国际移动用户识别码 
     */  
    private String IMSI;  
    
    public SimInfoUtil(Context context) {  
        telephonyManager = (TelephonyManager) context  
                .getSystemService(Context.TELEPHONY_SERVICE);  
    }  
  
    /** 
     * Role:获取当前设置的电话号码 
     *  
     */  
    public String getNativePhoneNumber() {  
        String NativePhoneNumber=null;  
        NativePhoneNumber=telephonyManager.getLine1Number();  
        return NativePhoneNumber;  
    }  
  
    /** 
     * Role:Telecom service providers获取手机服务商信息 <BR> 
     * 需要加入权限<uses-permission 
     * android:name="android.permission.READ_PHONE_STATE"/> <BR> 
     */  
    public String getProvidersName() {  
        String ProvidersName = null;  
        // 返回唯一的用户ID;就是这张卡的编号等等
        IMSI = telephonyManager.getSubscriberId();///////////////////适配有误
//        Log.v("imsi", IMSI);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
            ProvidersName = "中国移动";  
        } else if (IMSI.startsWith("46001")) {  
            ProvidersName = "中国联通";  
        } else if (IMSI.startsWith("46003")) {  
            ProvidersName = "中国电信";  
        } else if (IMSI.isEmpty()){
        	ProvidersName = "无";
        } else {
        	ProvidersName = "其他/国外运营商";
        }
        return ProvidersName;  
    }  
} 
