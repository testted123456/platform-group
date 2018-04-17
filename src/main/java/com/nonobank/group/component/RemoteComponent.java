package com.nonobank.group.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.group.component.exception.GroupException;
import com.nonobank.group.component.result.ResultCode;
import com.nonobank.group.entity.remote.RunGroupData;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tangrubei on 2018/3/15.
 */
@Component
public class RemoteComponent {


    @Value("${httpServer.case.baseUrl}")
    private String caseBaseUrl;


    @Value("${httpServer.case.runPath}")
    private String runCasePath;

    @Value("${httpServer.user.baseUrl}")
    private String userBaseUrl;

    @Value("${httpServer.user.roleUrlpath}")
    private String getRoleUrlMapPath;

    private static final String KEY_DATA = "data";

    private static final String KEY_CODE = "code";

    private static final String VALUE_SUCCESS_CODE = "10000";


    @Autowired
    private HttpClient httpClient;


    /**
     * 依据系统名称来获取url映射关系
     *
     * @return
     */
    public Map<String, String> getUrlMap() throws IOException, HttpException {
        CloseableHttpClient client = httpClient.getHttpClient();
        String getMappurl = userBaseUrl + getRoleUrlMapPath;
        CloseableHttpResponse closeableHttpResponse = httpClient.doGetSend(client, null, getMappurl, null);
        String repstr = HttpClient.getResBody(closeableHttpResponse);
        JSONObject reobj = JSON.parseObject(repstr);
        Map<String, String> remap = (Map) JSON.parse(String.valueOf(reobj.get(KEY_DATA)));
        return remap;

    }


    /**
     * 发送执行请求
     *
     * @param runGroupData
     */
    public void runGroup(RunGroupData runGroupData) throws IOException, HttpException {
        CloseableHttpClient client = httpClient.getHttpClient();
        String runUrl = caseBaseUrl + runCasePath;
        CloseableHttpResponse closeableHttpResponse = httpClient.doPostSendJson(client, null, runUrl, String.valueOf(JSONObject.toJSON(runGroupData)));
        String repstr = HttpClient.getResBody(closeableHttpResponse);
        JSONObject jsonObject = JSON.parseObject(repstr);
        if (jsonObject != null) {
            String code = String.valueOf(jsonObject.get(KEY_CODE));
            if (VALUE_SUCCESS_CODE.equals(code)) {
                return;
            }
            String message = String.valueOf(jsonObject.get(KEY_DATA));
            throw new GroupException(Integer.parseInt(code), message);
        }
        throw new GroupException(ResultCode.UNKOWN_ERROR.getCode(), ResultCode.UNKOWN_ERROR.getMsg());
    }


}
