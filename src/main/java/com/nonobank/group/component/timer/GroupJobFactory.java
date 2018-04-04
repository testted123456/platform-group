package com.nonobank.group.component.timer;

import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.util.EntityUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

//import com.nonobank.apps.service.TestCaseService;

/**
 * Created by tangrubei on 2017/2/10.
 */
public class GroupJobFactory implements Job {

    public static final String KEY_REMOTE_COMPONENT = "remoteComponent";

    public static final String KEY_TEST_GROUP = "testGroup";

    @Autowired
    RemoteComponent remoteComponent;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        RemoteComponent remoteComponent = (RemoteComponent) context.getMergedJobDataMap().get(KEY_REMOTE_COMPONENT);
        TestGroup testGroup = (TestGroup) context.getMergedJobDataMap().get(KEY_TEST_GROUP);
        EntityUtil.converCaseStr2CaseList(testGroup);

//        try {
//            remoteComponent.runGroup(testGroup);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // TODO: 2018/3/30 add error log
//        }

    }


}
