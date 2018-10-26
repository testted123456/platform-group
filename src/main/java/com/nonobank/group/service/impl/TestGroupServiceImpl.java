package com.nonobank.group.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.group.component.result.Result;
import com.nonobank.group.component.result.ResultCode;
import com.nonobank.group.entity.remote.RunGroupData;
import com.nonobank.group.remotecontroller.RemoteTestCase;
import com.nonobank.group.service.TestGroupService;

@Service
public class TestGroupServiceImpl implements TestGroupService {
	
	@Autowired
	RemoteTestCase remoteTestCase;

	@Override
	public boolean runGroup(RunGroupData runGroupData) {
		
		Result result = remoteTestCase.runGroup(runGroupData);
		
		if(!ResultCode.SUCCESS.getCode().equals(result.getCode())){
			return false;
		}
		
		return true;
	}
}
