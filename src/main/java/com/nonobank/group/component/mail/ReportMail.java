package com.nonobank.group.component.mail;

public interface ReportMail {
	
	public String getName();
	
	public String getHost();
	
	public String getFromAddress();

	public String getFromPassword();

	public String getToAddress();

	public String getSubject();

	public String getJobTime();
}
