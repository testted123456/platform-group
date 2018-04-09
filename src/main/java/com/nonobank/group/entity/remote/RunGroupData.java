package com.nonobank.group.entity.remote;

/**
 * Created by tangrubei on 2018/4/4.
 */
public class RunGroupData {
    private String env;
    private Integer[] tcIDs = new Integer[0];
    private Integer groupId;
    private Integer totalSize = 0;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer[] getTcIDs() {
        return tcIDs;
    }

    public void setTcIDs(Integer[] tcIDs) {
        this.tcIDs = tcIDs;
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
