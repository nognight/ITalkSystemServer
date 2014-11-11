package com.nognight.entity;

public class Account {
	private int acc_id;
	private String username;
	private String password;

	public int getId() {
		return acc_id;
	}

	public void setId(int acc_id) {
		this.acc_id = acc_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
