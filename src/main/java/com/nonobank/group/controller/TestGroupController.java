package com.nonobank.group.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nonobank.group.component.RemoteComponent;
import com.nonobank.group.component.exception.GroupException;
import com.nonobank.group.component.result.Result;
import com.nonobank.group.component.result.ResultCode;
import com.nonobank.group.component.result.ResultUtil;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.repository.TestGroupRepository;
import com.nonobank.group.util.EntityUtil;
import org.apache.http.HttpException;
import org.quartz.CronExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangrubei on 2018/3/14.
 */


@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestGroupController {

    public static Logger logger = LoggerFactory.getLogger(TestGroupController.class);


    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private RemoteComponent remoteComponent;

    private final static String SUFFIX = " ?";

    public void checkJobTime(String expression) {
        if(CronExpression.isValidExpression(expression+SUFFIX)) {
            return;
        }
        throw new GroupException(ResultCode.VALIDATION_ERROR.getCode(),expression + "不符合语法");
    }

    /**
     * 根据pid来查询子节点
     *
     * @param pid
     * @return
     */
    @GetMapping(value = "getByPid")
    @ResponseBody
    public Result getGroupByPid(@RequestParam Integer pid) {
        pid = pid == null ? 0 : pid;
        List<TestGroup> testGroups = testGroupRepository.findByPIdAndOptstatusEquals(pid, (short) 0);
        return ResultUtil.success(testGroups);
    }

    private void getAllGroupsByPid(int pid,List<TestGroup> testGroups){
        List<TestGroup> tempGroups = testGroupRepository.findByPIdAndOptstatusEquals(pid,(short)0);
        if(tempGroups!=null&&tempGroups.size()>0){
            testGroups.addAll(tempGroups);
        }
        for(TestGroup testGroup:tempGroups){
            this.getAllGroupsByPid(testGroup.getId(),testGroups);
        }

    }

    /**
     * 根据id来查询子节点
     *
     * @param id
     * @return
     */
    @GetMapping(value = "getById")
    @ResponseBody
    public Result getGroupById(@RequestParam(required = true) Integer id) throws IOException, HttpException {
        TestGroup testGroup = (TestGroup) testGroupRepository.findOne(id);
        EntityUtil.converCaseStr2CaseList(testGroup);

        return ResultUtil.success(testGroup);
    }

    @PostMapping(value = "save")
    @ResponseBody
    public Result saveGroup(@CookieValue(value = "nonousername", required = false) String userName, @RequestBody @Valid TestGroup testGroup, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String erroMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.error("接口校验失败：{}", erroMsg);
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), erroMsg);
        } else {
            if(testGroup.getTestCaseList()!=null){
                JSONArray jsonArray = (JSONArray) JSON.toJSON(testGroup.getTestCaseList());
                testGroup.setCases(jsonArray.toJSONString());
            }
            if (testGroup.getId() != null) {
                testGroup.setUpdatedBy(userName);
                testGroup.setUpdatedTime(LocalDateTime.now());
            } else {
                testGroup.setCreatedBy(userName);
                testGroup.setCreatedTime(LocalDateTime.now());
            }
            if(testGroup.getJobTime()!=null&&testGroup.getJobTime().length()>0){
                this.checkJobTime(testGroup.getJobTime());
            }
            testGroup.setOptstatus((short)0);
            testGroupRepository.save(testGroup);
        }
        return ResultUtil.success(testGroup);
    }

    @GetMapping(value = "runGroup")
    @ResponseBody
    public Result run(@RequestParam Integer id) throws IOException, HttpException {
        TestGroup testGroup = testGroupRepository.findByIdAndOptstatusNot(id,(short)2);
        RunGroupData runGroupData = EntityUtil.setRunGroupDataValue(testGroup);
        remoteComponent.runGroup(runGroupData);
        return ResultUtil.success();
    }

    @GetMapping(value = "checkJobTime")
    @ResponseBody
        public Result deleteGroup(@RequestParam(required = true) String jobTime) {
        this.checkJobTime(jobTime);
        return ResultUtil.success();
    }

    @GetMapping(value = "deleteGroup")
    @ResponseBody
    public Result deleteGroup(@CookieValue(value = "nonousername", required = false) String userName, @RequestParam(required = true) Integer id) {
        TestGroup testGroup = testGroupRepository.getOne(id);
        if (testGroup != null) {
            testGroup.setOptstatus((short) 2);
        }
        List<TestGroup> testGroups = new ArrayList<>();
        this.getAllGroupsByPid(testGroup.getId(),testGroups);
        testGroups.forEach(tg->{
            tg.setOptstatus((short)2);
        });
        testGroups.add(testGroup);
        testGroupRepository.save(testGroups);
        return ResultUtil.success();
    }


    @GetMapping(value = "getSessionId")
    @ResponseBody
    public String getSession(HttpServletRequest request) {
        logger.info("aaaa");
        logger.error("bbbb");
        return request.getSession().getId();
    }


    @GetMapping(value = "index")
    @ResponseBody
    public String index() {
        return "hello!";
    }

    public static void main(String[] args) {
        List<TestGroup> testGroups = new ArrayList<>();
        TestGroup reqTestGroup1 = new TestGroup();
        TestGroup reqTestGroup2 = new TestGroup();
        testGroups.add(reqTestGroup1);
        testGroups.add(reqTestGroup2);
        testGroups.forEach(tg->{
            tg.setOptstatus((short)2);

        });
        System.out.println("ok");


//        TestGroup reqTestGroup = new TestGroup();
//        reqTestGroup.setName("mygroup");
//        reqTestGroup.setDescription("mygroupdesc");
//        List<TestCase> testCases = new ArrayList<>();
//        TestCase reqTestCase = new TestCase();
//        reqTestCase.setId(1);
//        reqTestCase.setName("myfirstTestcase");
//        reqTestCase.setChecked(true);
//
//        TestCase reqTestCase2 = new TestCase();
//        reqTestCase2.setId(2);
//        reqTestCase2.setName("mySecondTestcase");
//        reqTestCase2.setChecked(true);
//
//        testCases.add(reqTestCase);
//        testCases.add(reqTestCase2);
//
//        reqTestGroup.setTestCaseList(testCases);
//
//        System.out.println(JSONObject.toJSON(reqTestGroup));


    }


}
