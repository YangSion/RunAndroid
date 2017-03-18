package com.yangsion.runandroid.domen;

import android.graphics.drawable.Drawable;
/**
 * 软件 software bean
 * @author stabilit.yang
 */
public class DataInfo {
	
	
	private String appName;
	private String appVersion;
	private long cacheSize;
	private String packgeName;
	private long dataSize;
	private long codeSize;
	private Drawable icon;
	private boolean checked;
	
	
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}
	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	/**
	 * @return the cacheSize
	 */
	public long getCacheSize() {
		return cacheSize;
	}
	/**
	 * @param cacheSize the cacheSize to set
	 */
	public void setCacheSize(long cacheSize) {
		this.cacheSize = cacheSize;
	}
	/**
	 * @return the packgeName
	 */
	public String getPackgeName() {
		return packgeName;
	}
	/**
	 * @param packgeName the packgeName to set
	 */
	public void setPackgeName(String packgeName) {
		this.packgeName = packgeName;
	}
	/**
	 * @return the dataSize
	 */
	public long getDataSize() {
		return dataSize;
	}
	/**
	 * @param dataSize the dataSize to set
	 */
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}
	/**
	 * @return the codeSize
	 */
	public long getCodeSize() {
		return codeSize;
	}
	/**
	 * @param codeSize the codeSize to set
	 */
	public void setCodeSize(long codeSize) {
		this.codeSize = codeSize;
	}
	/**
	 * @return the icon
	 */
	public Drawable getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataInfo [appName=" + appName + ", appVersion=" + appVersion
				+ ", cacheSize=" + cacheSize + ", packgeName=" + packgeName
				+ ", dataSize=" + dataSize + ", codeSize=" + codeSize
				+ ", icon=" + icon + ", checked=" + checked + "]";
	}
	
}
