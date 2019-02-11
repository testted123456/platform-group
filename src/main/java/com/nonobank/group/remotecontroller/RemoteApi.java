package com.nonobank.group.remotecontroller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nonobank.group.component.result.Result;

@FeignClient(value="PLATFORM-INTERFACE")
public interface RemoteApi {

	@GetMapping(value="/statis/groupStatisDetail", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result groupStatisDetail();
	
	@GetMapping(value="/statis/statisGroupCaseByAuthor", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result statisGroupCaseByAuthor();
	
	@GetMapping(value="/jenkins/getPackageResult", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result getPackageResult();
}
