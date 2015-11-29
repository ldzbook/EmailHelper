package com.cgnfex.bean;

public class Config {
	private String host;
	private String protocol;
	private String sendEmail;
	private String password;
	
	
	
	public Config() {
	}
	public Config(String host, String protocol, String sendEmail,
			String password) {
		super();
		this.host = host;
		this.protocol = protocol;
		this.sendEmail = sendEmail;
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
