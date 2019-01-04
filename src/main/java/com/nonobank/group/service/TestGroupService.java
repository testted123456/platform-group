package com.nonobank.group.service;

import java.util.List;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.RunGroupData;

public interface TestGroupService {
	
	public boolean runGroup(RunGroupData runGroupData);
	
	public TestGroup getById(Integer id);
	
	public List<TestGroup> findAll();
	
	public boolean isCaseInGroup(Integer caseId);

}
