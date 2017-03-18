package com.yangsion.runandroid.domen;

import android.graphics.drawable.Drawable;


/**
 *	缓存  cache bean  
 * @author stabilit.yang
 */
public class CacheInfo {
	
	
	private String appName;
	private long cacheSize;
	private String packgeName;
	private long dataSize;
	private long codeSize;
	private Drawable icon;
//	private boolean checked;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CacheInfo [appName=" + appName + ", cacheSize=" + cacheSize
				+ ", packgeName=" + packgeName + ", dataSize=" + dataSize
				+ ", codeSize=" + codeSize + ", icon=" + icon + "]";
	}

	
	
	
	
	
//	@Override
//	public String toString() {
//		return "DataInfo [appName=" + appName + ", cacheSize=" + cacheSize
//				+ ", packgeName=" + packgeName + ", dataSize=" + dataSize
//				+ ", codeSize=" + codeSize + ", icon=" + icon + ", checked="
//				+ checked 
//				+ "]";
//	}
//	public String getAppName() {
//		return appName;
//	}
//	public void setAppName(String appName) {
//		this.appName = appName;
//	}
//	public long getCacheSize() {
//		return cacheSize;
//	}
//	public void setCacheSize(long cacheSize) {
//		this.cacheSize = cacheSize;
//	}
//	public String getPackgeName() {
//		return packgeName;
//	}
//	public void setPackgeName(String packgeName) {
//		this.packgeName = packgeName;
//	}
//	public long getDataSize() {
//		return dataSize;
//	}
//	public void setDataSize(long dataSize) {
//		this.dataSize = dataSize;
//	}
//	public long getCodeSize() {
//		return codeSize;
//	}
//	public void setCodeSize(long codeSize) {
//		this.codeSize = codeSize;
//	}
//	public Drawable getIcon() {
//		return icon;
//	}
//	public void setIcon(Drawable icon) {
//		this.icon = icon;
//	}
//	public boolean isChecked() {
//		return checked;
//	}
//	public void setChecked(boolean checked) {
//		this.checked = checked;
//	}
	
}
