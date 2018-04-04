package com.nonobank.group.component.timer;

import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.repository.TestGroupRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.nonobank.apps.service.TestCaseService;

/**
 * Created by tangrubei on 2017/2/10.
 */

@Component
public class QuartzManagerComponent implements ApplicationListener<ContextRefreshedEvent> {


    private Scheduler scheduler;


    private final String groupName = "groupExecute";


    public final static String JOB_TIME_SUFFIX = " ?";


    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private SchedulerFactory schedulerFactory;

    @Autowired
    private RemoteComponent remoteComponent;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
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
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addGroupJob(TestGroup testGroup) throws SchedulerException {
        if (CronExpression.isValidExpression(testGroup.getJobTime() + JOB_TIME_SUFFIX)) {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put(GroupJobFactory.KEY_TEST_GROUP,testGroup);
            dataMap.put(GroupJobFactory.KEY_REMOTE_COMPONENT,remoteComponent);
            this.addJob(String.valueOf(testGroup.getId()), testGroup.getJobTime() + JOB_TIME_SUFFIX, GroupJobFactory.class, dataMap);
        }
    }


    private void addJob(String jobName, String jobTime, Class classz, Map<String, Object> dataMap) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(classz).withIdentity(jobName, groupName).build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
        if (dataMap != null) {
            dataMap.forEach((k, v) -> {
                job.getJobDataMap().put(k, v);
            });
        }
        scheduler.scheduleJob(job, trigger);
    }

    private void deleteJob(String jobName) throws SchedulerException {
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
