package com.yangsion.runandroid.domen;

import java.util.Arrays;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息  UserInfo bean
 * @author stabilit.yang
 *
 */
public class UserInfo extends BmobUser {
	private static final long serialVersionUID = 1L;
	
	private Integer age;
	private Integer num;
	private Boolean sex;
	private String deviceId;
	private String nickname;
	private String hobbysignature;
	private String time;
	private String[] pickfile;
	
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * @return the sex
	 */
	public Boolean getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
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
	 * @return the hobbysignature
	 */
	public String getHobbysignature() {
		return hobbysignature;
	}
	/**
	 * @param hobbysignature the hobbysignature to set
	 */
	public void setHobbysignature(String hobbysignature) {
		this.hobbysignature = hobbysignature;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the pickfile
	 */
	public String[] getPickfile() {
		return pickfile;
	}
	/**
	 * @param pickfile the pickfile to set
	 */
	public void setPickfile(String[] pickfile) {
		this.pickfile = pickfile;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyUser [age=" + age + ", num=" + num + ", sex=" + sex
				+ ", deviceId=" + deviceId + ", nickname=" + nickname
				+ ", hobbysignature=" + hobbysignature + ", time=" + time
				+ ", pickfile=" + Arrays.toString(pickfile) + "]";
	}
}
