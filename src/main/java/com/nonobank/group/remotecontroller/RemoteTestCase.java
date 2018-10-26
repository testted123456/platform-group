package com.nonobank.group.remotecontroller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nonobank.group.component.result.Result;
import com.nonobank.group.entity.remote.RunGroupData;

@FeignClient(value="PLATFORM-TESTCASE")
public interface RemoteTestCase {
		
		@PostMapping(value="/group/execute", consumes=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		Result runGroup(@RequestBody RunGroupData runGroupData); 
}
