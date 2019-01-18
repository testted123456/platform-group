package com.nonobank.group.component.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nonobank.group.component.mail.ReportMail;
import com.nonobank.group.service.MailService;

public class MailJobFactory implements Job {
	
	public static Logger logger = LoggerFactory.getLogger(MailJobFactory.class);

    public static final String KEY_MAIL_COMPONENT = "mailJobComponent";

    public static final String KEY_MAIL_SERVICE = "mailJobService";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	logger.info("开始定执行发送邮件定时任务...");
    	MailService mailService = (MailService) context.getMergedJobDataMap().get(KEY_MAIL_SERVICE);
    	ReportMail reportMail = (ReportMail)context.getMergedJobDataMap().get(KEY_MAIL_COMPONENT);
    	mailService.sendHtmlMail(reportMail);
    }
}
