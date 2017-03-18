package com.yangsion.runandroid.domen;

import java.util.Arrays;

import cn.bmob.v3.BmobObject;
/**
 * 检测   detection bean
 * @author stabilit.yang
 * @return String[bean]
 */
public class JsonInfo extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String deviceId;
	private String[] infos;
	private String phone;
	
	
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}


	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	/**
	 * @return the infos
	 */
	public String[] getInfos() {
		return infos;
	}


	/**
	 * @param infos the infos to set
	 */
	public void setInfos(String[] infos) {
		this.infos = infos;
	}


	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}


	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JsonInfo [deviceId=" + deviceId + ", infos="
				+ Arrays.toString(infos) + ", phone=" + phone + "]";
	}




	
}
