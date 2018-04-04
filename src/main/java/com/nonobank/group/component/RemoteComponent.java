package com.nonobank.group.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.group.entity.db.TestGroup;
import com.nonobank.group.entity.remote.TestCase;
import org.apache.http.HttpException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangrubei on 2018/3/15.
 */
@Component
public class RemoteComponent {


    @Value("${httpServer.case.baseUrl}")
    private String caseBaseUrl;


    @Value("${httpServer.case.runPath}")
    private String runCasePath;



    private static final String KEY_CASE_ID = "id";

    private static final String KEY_CASE_NAME = "name";

    private static final String KEY_DATA = "data";

    @Autowired
    private HttpClient httpClient;


    /**
     * 根据id字符串来查询id
     * @param ids
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public List<TestCase> getCasesByIds(String ids) throws IOException, HttpException {
//        CloseableHttpClient client = httpClient.getHttpClient();
//        String getUrl = caseServerUrl+getCasePath+ids;
//        String rep = HttpClient.getResBody(httpClient.doGetSend(client,null,getUrl,null));
//        JSONArray jsonArray = JSON.parseArray(rep);
//        List<TestCase> reList = new ArrayList<>();
//        for(Object obj:jsonArray){
//            JSONObject jobj = (JSONObject) obj;
//            TestCase reqTestCase = new TestCase();
//            reqTestCase.setId((Integer) jobj.get(KEY_CASE_ID));
//            reqTestCase.setName(String.valueOf(jobj.get(KEY_CASE_NAME)));
//            reList.add(reqTestCase);
//        }


        return null;
    }


    /**
     * 发送执行请求
     * @param testGroup
     */
    public void runGroup(TestGroup testGroup) throws IOException {
        CloseableHttpClient client = httpClient.getHttpClient();
        String runUrl = caseBaseUrl+caseBaseUrl;
        httpClient.doPostSendJson(client,null,runUrl, String.valueOf(JSONObject.toJSON(testGroup)));
    }




}
