package com.example.administrator.steps_count.model;


import java.io.Serializable;

public class Adress implements Serializable{
	private String id;
private String clientname;
private String sender;
private String clienttel;
private String clientaddress;
public boolean ischeck;
public Adress() {
	
}

	public Adress(String sender, String clienttel, String clientaddress) {
		this.sender = sender;
		this.clienttel = clienttel;
		this.clientaddress = clientaddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientname() {
	return clientname;
}
public void setClientname(String clientname) {
	this.clientname = clientname;
}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public String getSender() {
	return sender;
}
public void setSender(String sender) {
	this.sender = sender;
}
public String getClienttel() {
	return clienttel;
}
public void setClienttel(String clienttel) {
	this.clienttel = clienttel;
}
public String getClientaddress() {
	return clientaddress;
}
public void setClientaddress(String clientaddress) {
	this.clientaddress = clientaddress;
}

}
