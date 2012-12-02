package com.magsambols.yapb.act01;

public class Contact {
	private long id;
	private String name;
	private String phoneNum;

	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id=id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}

	public String toString(){
		return name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}
