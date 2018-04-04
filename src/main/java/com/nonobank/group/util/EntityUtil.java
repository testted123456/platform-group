package com.nonobank.group.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.TestCase;

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
}
