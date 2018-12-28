package com.ll.spring.boot.mvc.model;

public class User {

	private Long userId;
	private String userName;
	private String passwd;
	private Long depId;

	public User() {
		super();
	}

	public User(Long userId, String userName, String passwd, Long depId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.passwd = passwd;
		this.depId = depId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", passwd=" + passwd + ", depId=" + depId + "]";
	}

}
