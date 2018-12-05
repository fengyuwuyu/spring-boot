package com.ll.spring.boot.aop.model;

public class User {

	private Integer id;
	private String userName;
	private String passwd;

	public User() {
	}

	public User(Integer id, String userName, String passwd) {
		this.id = id;
		this.userName = userName;
		this.passwd = passwd;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", passwd=" + passwd + "]";
	}

}
