package com.nonobank.group.component.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.service.TestGroupService;
import com.nonobank.group.util.EntityUtil;

//import com.nonobank.apps.service.TestCaseService;

/**
 * Created by tangrubei on 2017/2/10.
 */
public class GroupJobFactory implements Job {
	
	public static Logger logger = LoggerFactory.getLogger(GroupJobFactory.class);

    public static final String KEY_REMOTE_COMPONENT = "remoteComponent";

    public static final String KEY_TEST_GROUP = "testGroup";

    @Autowired
    RemoteComponent remoteComponent;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        RemoteComponent remoteComponent = (RemoteComponent) context.getMergedJobDataMap().get(KEY_REMOTE_COMPONENT);
       
    	logger.info("开始定执行定时任务...");
    	TestGroup testGroup = (TestGroup) context.getMergedJobDataMap().get(KEY_TEST_GROUP);
    	TestGroupService testGroupService = (TestGroupService)context.getMergedJobDataMap().get(KEY_REMOTE_COMPONENT);
        Integer groupId = testGroup.getId();
        testGroup = testGroupService.getById(groupId);
    	EntityUtil.converCaseStr2CaseList(testGroup);
        RunGroupData runGroupData = EntityUtil.setRunGroupDataValue(testGroup);
        
        testGroupService.runGroup(runGroupData);
//        try {
//            remoteComponent.runGroup(testGroup);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // TODO: 2018/3/30 add error log
//        }

    }


}
