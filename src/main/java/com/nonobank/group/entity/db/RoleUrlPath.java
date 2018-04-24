package com.nonobank.group.entity.db;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by tangrubei on 2018/4/10.
 */

@Entity
public class RoleUrlPath {

    @Id
    @GeneratedValue
    Integer id;

    @Column(nullable = false, columnDefinition = "integer COMMENT '角色id'")
    Integer roleId;


    @Column(nullable = false, columnDefinition = "varchar(300) COMMENT '角色名称'")
    String roleName;


    @Column(nullable = false, columnDefinition = "varchar(300) COMMENT '系统名称'")
    String system;

    @Column(nullable = false, columnDefinition = "varchar(300) COMMENT '路径'")
    String urlPath;


    @Column(nullable = true, columnDefinition = "varchar(300) COMMENT '创建人'")
    String createdBy;

    @Column(nullable = true, columnDefinition = "varchar(300) COMMENT '修改人'")
    String updatedBy;


    @Column(columnDefinition = "datetime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createdTime;



    @Column(columnDefinition = " datetime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime updatedTime;

    @Column(nullable = false, columnDefinition = "smallint(1) COMMENT '0:正常，1:已更新，2:已删除'")
    Short optstatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Short getOptstatus() {
        return optstatus;
    }

    public void setOptstatus(Short optstatus) {
        this.optstatus = optstatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
