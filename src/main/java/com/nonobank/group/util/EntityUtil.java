package com.nonobank.group.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.entity.remote.TestCase;
import org.aspectj.weaver.ast.Test;

import java.util.ArrayList;
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
        List<TestCase> testCases = testGroup.getTestCaseList().stream().filter(tc->tc.isChecked()).collect(Collectors.toList());
        runGroupData.setTotalSize(testCases.size());
        runGroupData.setTestCases(testCases);
        return  runGroupData;

    }

    public static void main(String[] args) {
        RunGroupData runGroupData = new RunGroupData();
        runGroupData.setGroupId(1);
        runGroupData.setEnv("SIT");
        List<TestCase> cases = new ArrayList<>();
        for (int i=0;i<3;i++){
            TestCase testCase = new TestCase();
            testCase.setId(i);
            testCase.setChecked(i%2==0);
            testCase.setCaseType((short)(i%2));
            cases.add(testCase);
        }
        runGroupData.setTestCases(cases);
        runGroupData.setTotalSize(3);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(runGroupData);
        System.out.println(jsonObject.toJSONString());



    }
}
