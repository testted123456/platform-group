package com.nonobank.group;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.repository.TestGroupRepository;
import com.nonobank.group.util.EntityUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformGroupApplicationTests {

	@Autowired
	private HttpClient httpClient;

	@Autowired
	TestGroupRepository testGroupRepository;

	@Test
	public void contextLoads() throws NoSuchAlgorithmException, KeyManagementException {
//		CloseableHttpClient httpclient = httpClient.getHttpsClient();
//		List<TestGroup> testGroupList = testGroupRepository.findByOptstatusNotAndJobTimeIsNotNull((short) 2);
//
//		System.out.println("ok");
	}

	@Test
	public void savegroup(){
//		TestGroup testGroup = new TestGroup();
//		testGroup.setpId(0);
//		testGroup.setName("name");
//		testGroup.setOptstatus((short)0);
//		testGroupRepository.save(testGroup);
//		System.out.println("ok");

//		TestGroup testGroup = testGroupRepository.findByIdAndOptstatusNot(1,(short)2);
//		EntityUtil.converCaseStr2CaseList(testGroup);
//		System.out.println(((JSONObject)JSONObject.toJSON(testGroup)).toJSONString());

	}
}
