package com.nonobank.group.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.http.HttpException;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nonobank.group.component.exception.GroupException;
import com.nonobank.group.component.mail.ReportMail;
import com.nonobank.group.component.result.Result;
import com.nonobank.group.component.result.ResultCode;
import com.nonobank.group.component.result.ResultUtil;
import com.nonobank.group.component.timer.QuartzManagerComponent;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.repository.TestGroupRepository;
import com.nonobank.group.service.MailService;
import com.nonobank.group.service.TestGroupService;
import com.nonobank.group.util.EntityUtil;
import com.nonobank.group.util.UserUtil;

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
    private MailService mailService;
    
    @Autowired
    QuartzManagerComponent quartzManagerComponent;

    @Autowired
    private TestGroupService testGroupService;
    
    @Autowired
    private ReportMail reportMail;

    private final static String SUFFIX = " ?";

    public void checkJobTime(String expression) {
        if (CronExpression.isValidExpression(expression + SUFFIX)) {
            return;
        }
        throw new GroupException(ResultCode.VALIDATION_ERROR.getCode(), expression + "不符合语法");
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

    private void getAllGroupsByPid(int pid, List<TestGroup> testGroups) {
        List<TestGroup> tempGroups = testGroupRepository.findByPIdAndOptstatusEquals(pid, (short) 0);
        if (tempGroups != null && tempGroups.size() > 0) {
            testGroups.addAll(tempGroups);
        }
        for (TestGroup testGroup : tempGroups) {
            this.getAllGroupsByPid(testGroup.getId(), testGroups);
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
    public Result saveGroup( @RequestBody @Valid TestGroup testGroup, BindingResult bindingResult) {
    	 String userName = UserUtil.getUser();
    	 
        if (bindingResult.hasErrors()) {
            String erroMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            logger.error("接口校验失败：{}", erroMsg);
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), erroMsg);
        } else {
        	if(false == testGroup.getType()){//文件夹
        		testGroup.setOptstatus((short)0);
        		testGroup = testGroupRepository.save(testGroup);
        		return ResultUtil.success();
        	}
        	
            if (testGroup.getTestCaseList() != null) {
                JSONArray jsonArray = (JSONArray) JSON.toJSON(testGroup.getTestCaseList());
                testGroup.setCases(jsonArray.toJSONString());
            }
            
            String jobTime = testGroup.getJobTime();
            String oldJobTime = null;
            
            if (testGroup.getId() != null) {//update group
                testGroup.setUpdatedBy(userName);
                testGroup.setUpdatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                TestGroup tg = testGroupRepository.findByIdAndOptstatusNot(testGroup.getId(), (short)2);
                oldJobTime = tg.getJobTime();
            } else {//new group
                testGroup.setCreatedBy(userName);
                testGroup.setCreatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            }
            if (testGroup.getJobTime() != null && testGroup.getJobTime().length() > 0) {
                this.checkJobTime(testGroup.getJobTime());
            }
            
            testGroup.setOptstatus((short) 0);
            testGroup = testGroupRepository.save(testGroup);
            
            if(null != oldJobTime && !"".equals(oldJobTime)){
            	if(!oldJobTime.equals(jobTime)){//delete old job time, then add job time
            		try {
    					quartzManagerComponent.deleteJob(String.valueOf(testGroup.getId()));
    					quartzManagerComponent.addGroupJob(testGroup);
    				} catch (SchedulerException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            }else{
            	if(null != jobTime || !"".equals(jobTime)){//add job time
            		try {
    					quartzManagerComponent.addGroupJob(testGroup);
    				} catch (SchedulerException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            }
        }
        return ResultUtil.success(testGroup);
    }

    @GetMapping(value = "runGroup")
    @ResponseBody
    public Result run(@RequestParam Integer id) throws IOException, HttpException {
        TestGroup testGroup = testGroupRepository.findByIdAndOptstatusNot(id, (short) 2);
        RunGroupData runGroupData = EntityUtil.setRunGroupDataValue(testGroup);
        if (runGroupData.getTestCases() == null || runGroupData.getTestCases().size() == 0) {
            throw new GroupException(ResultCode.EMPTY_ERROR.getCode(), "未找到需要执行的case");
        }
        testGroupService.runGroup(runGroupData);
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
        this.getAllGroupsByPid(testGroup.getId(), testGroups);
        testGroups.forEach(tg -> {
            tg.setOptstatus((short) 2);
        });
        testGroups.add(testGroup);
        testGroupRepository.save(testGroups);
        return ResultUtil.success();
    }
    
    @GetMapping(value="isCaseInGroup")
    @ResponseBody
    public Result isCaseInGroup(@RequestParam(required=true) Integer caseId){
    	
    	boolean result = testGroupService.isCaseInGroup(caseId);
    	
    	if(result == true){
    		return ResultUtil.success();
    	}else{
    		return ResultUtil.error(ResultCode.EMPTY_ERROR.getCode(), "case id: " +caseId + " 没有被引用！");
    	}
    }

    @GetMapping(value = "getSessionId")
    @ResponseBody
    public String getSession(HttpServletRequest request) {
        logger.info("aaaa");
        logger.error("bbbb");
        return request.getSession().getId();
    }

    @GetMapping(value = "sendMail")
    @ResponseBody
    public Result sendMail() {
//    	mailService.sendHtmlMail(reportMail);
    	mailService.generateHtmlReport();
        return ResultUtil.success();
    }

}
