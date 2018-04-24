package com.nonobank.group.entity.remote;

import java.util.List;

/**
 * Created by tangrubei on 2018/4/4.
 */
public class RunGroupData {
    private String env;
    private Integer groupId;
    private Integer totalSize = 0;
    private List<TestCase> testCases;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
