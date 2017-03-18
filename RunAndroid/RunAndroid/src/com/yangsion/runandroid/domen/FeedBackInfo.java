package com.yangsion.runandroid.domen;


import cn.bmob.v3.BmobObject;
/**
 * 反馈   FeedBack bean
 * @author stabilit.yang
 * @return String[bean]
 */
public class FeedBackInfo extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String deviceId;
	private String name;
	private String text;
	private String contact;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}


	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedBackInfo [deviceId=" + deviceId + ", name=" + name
				+ ", text=" + text + ", contact=" + contact + "]";
	}

}
