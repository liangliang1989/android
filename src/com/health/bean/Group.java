package com.health.bean;

public class Group {

	private String nickName;
	private String userName;
	private String password;
	private String sex;
	private String phone;
	private String address;
	private String token;

	public Group(String nickName, String userName, String token, String password) {
		this.nickName = nickName;
		this.userName = userName;
		this.setPassword(password);
		this.setToken(token);
	}

	public String getNickName() {
		return nickName;
	}

	/**
	 * ҽ���û���
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	public String getSex() {
		return sex;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public void setName(String name) {
		this.nickName = name;
	}

	public void setCount(String count) {
		this.userName = count;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(nickName);
		builder.append(":");
		builder.append(userName);
		builder.append(":");
		builder.append(token);
		builder.append(":");
		builder.append(password);
		String str=builder.toString();
		return str;
	}
	
	public static Group generate(String formatString){
		
		return null;
		//return new Group(formatString);
	}
*/
}
