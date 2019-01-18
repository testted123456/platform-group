package com.nonobank.group.component.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.component.mail.ReportMail;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.repository.TestGroupRepository;
import com.nonobank.group.service.MailService;
import com.nonobank.group.service.TestGroupService;

//import com.nonobank.apps.service.TestCaseService;

/**
 * Created by tangrubei on 2017/2/10.
 */

@Component
public class QuartzManagerComponent implements ApplicationListener<ContextRefreshedEvent> {

	public static Logger logger = LoggerFactory.getLogger(QuartzManagerComponent.class);
	
    private Scheduler scheduler;


    private final String groupName = "groupExecute";


    public final static String JOB_TIME_SUFFIX = " ?";


    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private SchedulerFactory schedulerFactory;

    @Autowired
    private RemoteComponent remoteComponent;
    
    @Autowired
    TestGroupService testGroupService;

    @Autowired
    MailService mailService;
    
    @Autowired
    ReportMail reportMail;
  
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	ApplicationContext  ac = event.getApplicationContext();
    	
    	ApplicationContext sub = ac.getParent().getParent();
    	
    	if(null != sub){
    		return;
    	}
    	
    	try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            
            List<TestGroup> testGroupList = testGroupRepository.findByOptstatusNotAndJobTimeIsNotNull((short) 2);
            testGroupList.forEach(testGroup -> {
                try {
                    this.addGroupJob(testGroup);
                } catch (SchedulerException e) {
//                    todo need error log
                    e.printStackTrace();
                }
            });
            
            this.addMailJob(reportMail);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    //邮件定时任务
    public void addMailJob(ReportMail reportMail) throws SchedulerException{
    	Map<String,Object> dataMap = new HashMap<>();
    	dataMap.put(MailJobFactory.KEY_MAIL_COMPONENT, reportMail);
    	dataMap.put(MailJobFactory.KEY_MAIL_SERVICE, mailService);
    	this.addJob(reportMail.getName(), reportMail.getJobTime(), MailJobFactory.class, dataMap);
    }

    public void addGroupJob(TestGroup testGroup) throws SchedulerException {
    
        if (CronExpression.isValidExpression(testGroup.getJobTime() + JOB_TIME_SUFFIX)) {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put(GroupJobFactory.KEY_TEST_GROUP,testGroup);
//            dataMap.put(GroupJobFactory.KEY_REMOTE_COMPONENT,remoteComponent);
            dataMap.put(GroupJobFactory.KEY_REMOTE_COMPONENT,testGroupService);
            this.addJob(String.valueOf(testGroup.getId()), testGroup.getJobTime() + JOB_TIME_SUFFIX, GroupJobFactory.class, dataMap);
        }
    }

    private void addJob(String jobName, String jobTime, Class classz, Map<String, Object> dataMap) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(classz).withIdentity(jobName, groupName).build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
        if (dataMap != null) {
            dataMap.forEach((k, v) -> {
            	if(null != v && !"".equals(String.valueOf(v))){
            		job.getJobDataMap().put(k, v);
            	}
            });
        }
        scheduler.scheduleJob(job, trigger);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
        if (triggerKey != null) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            JobKey jobKey = JobKey.jobKey(groupName, groupName);
            scheduler.deleteJob(jobKey);
        }
    }

    public void updateJob(String jobName, String jobTime, Class classz, Map<String, Object> dataMap) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
        this.deleteJob(jobName);
        this.addJob(jobName, jobTime, classz, dataMap);
    }


}
