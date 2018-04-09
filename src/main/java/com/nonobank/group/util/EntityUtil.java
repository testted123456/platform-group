package com.nonobank.group.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.entity.remote.TestCase;
import org.aspectj.weaver.ast.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tangrubei on 2018/3/29.
 */
public class EntityUtil {

    public static void converCaseStr2CaseList(TestGroup testGroup){
        if(testGroup.getCases()!=null&&testGroup.getCases().length()>0){
            JSONArray jsonArray = JSON.parseArray(testGroup.getCases());
            List<TestCase> reqTestCases = jsonArray.stream().map(obj -> {
                JSONObject jobj = (JSONObject) obj;
                return jobj.toJavaObject(TestCase.class);
            }).collect(Collectors.toList());
            testGroup.setTestCaseList(reqTestCases);
        }
    }


    public static RunGroupData setRunGroupDataValue(TestGroup testGroup){
        if (testGroup==null){
            return null;
        }
        converCaseStr2CaseList(testGroup);
        RunGroupData runGroupData = new RunGroupData();
        runGroupData.setGroupId(testGroup.getId());
        runGroupData.setEnv(testGroup.getEnv());
        if(testGroup.getTestCaseList()!=null){
            runGroupData.setTotalSize(testGroup.getTestCaseList().size());
            List<Integer> ids = testGroup.getTestCaseList().stream().filter(tc->tc.isChecked()).map(t->t.getId()).collect(Collectors.toList());
            Integer[] tcids = new Integer[ids.size()];
            ids.toArray(tcids);
            runGroupData.setTcIDs(tcids);
        }
        return  runGroupData;

    }
}
