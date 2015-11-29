package com.cgnfex.bean;

import java.util.List;
import java.util.Map;

public class Content {
	private List<String> emails;
	private String subject;
	private Map<String, String> body;

	
	
	public Content() {
	}

	public Content(List<String> emails, String subject, Map<String, String> body) {
		super();
		this.emails = emails;
		this.subject = subject;
		this.body = body;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}

}
