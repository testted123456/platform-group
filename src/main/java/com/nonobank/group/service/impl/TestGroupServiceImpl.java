package com.nonobank.group.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.component.result.Result;
import com.nonobank.group.component.result.ResultCode;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.remotecontroller.RemoteTestCase;
import com.nonobank.group.repository.TestGroupRepository;
import com.nonobank.group.service.TestGroupService;

@Service
public class TestGroupServiceImpl implements TestGroupService {
	
	@Autowired
	RemoteTestCase remoteTestCase;

	@Autowired
	TestGroupRepository testGroupRepository;
	
	@Override
	public boolean runGroup(RunGroupData runGroupData) {
		
		Result result = remoteTestCase.runGroup(runGroupData);
		
		if(!ResultCode.SUCCESS.getCode().equals(result.getCode())){
			return false;
		}
		
		return true;
	}

	@Override
	public TestGroup getById(Integer id) {
		return testGroupRepository.findByIdAndOptstatusNot(id, (short)2);
	}

	@Override
	public List<TestGroup> findAll() {
		// TODO Auto-generated method stub
		return testGroupRepository.findByOptstatusNot((short)2);
	}

	@Override
	public boolean isCaseInGroup(Integer caseId) {
		// TODO Auto-generated method stub
		List<TestGroup> testGroups = findAll();
    	
    	Optional<JSONArray> jsonArr = testGroups.stream().map(TestGroup::getCases)
    			.filter(x->{return x != null;})
    			.map(JSONObject::parseArray)
    			.filter(x->{
    		int size = x.size();
    		for(int i=0;i<size;i++){
    			JSONObject jsonObject = x.getJSONObject(i);
    			Integer id = jsonObject.getInteger("id");
    			if(id.equals(caseId)){
    				return true;
    			}
    		}
    		return false;
    	}).findAny();
    	
    	if(jsonArr.isPresent() == true){
    		return true;
    	}else{
    		return false;
    	}
	}
}
