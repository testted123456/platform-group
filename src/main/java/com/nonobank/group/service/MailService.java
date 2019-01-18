package com.nonobank.group.service;

import javax.mail.Message;

import com.nonobank.group.component.mail.ReportMail;

public interface MailService {

	Message getMessage(ReportMail reportMail);
	
	void sendHtmlMail(ReportMail reportMail);
	
	String generateHtmlReport();
}
