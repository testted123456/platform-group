package com.nonobank.group.component.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DailyReportMail implements ReportMail{
	
	private String name = "dailyReportMail";
	
	@Value("${mail.host}")
	private String host;

	@Value("${mail.fromAddress}")
	private String fromAddress;

	@Value("${mail.fromPassword}")
	private String fromPassword;

	@Value("${mail.toAddress}")
	private String toAddress;

	@Value("${mail.subject}")
	private String subject;
	
	@Value("${mail.job.time}")
	private String jobTime;
	
	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getFromAddress() {
		return fromAddress;
	}

	@Override
	public String getFromPassword() {
		return fromPassword;
	}

	@Override
	public String getToAddress() {
		return toAddress;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getJobTime() {
		return jobTime;
	}

}
