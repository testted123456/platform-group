package com.nonobank.group.service.impl;

import static java.util.stream.Collectors.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.group.component.mail.ReportMail;
import com.nonobank.group.component.result.Result;
import com.nonobank.group.remotecontroller.RemoteApi;
import com.nonobank.group.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	
    public static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    RemoteApi remoteApi;
    
	@Override
	public Message getMessage(ReportMail reportMail) {
		// TODO Auto-generated method stub
		final Properties p = System.getProperties();
		p.setProperty("mail.smtp.host", reportMail.getHost());
		p.setProperty("mail.smtp.auth", "true");
		p.setProperty("mail.smtp.user", reportMail.getFromAddress());
		p.setProperty("mail.smtp.pass", reportMail.getFromPassword());

		Session session = Session.getInstance(p, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(reportMail.getFromAddress(), reportMail.getFromPassword());
			}
		});

		session.setDebug(true);
		Message message = new MimeMessage(session);

		try {
			message.setSubject(reportMail.getSubject());
			message.setFrom(new InternetAddress(reportMail.getFromAddress()));
			message.setReplyTo(InternetAddress.parse(reportMail.getFromAddress()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reportMail.getToAddress()));
			message.setSentDate(new Date());
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return message;
	}

	@Override
	public void sendHtmlMail(ReportMail reportMail) {
		// TODO Auto-generated method stub

		Message message = getMessage(reportMail);

		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();

		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();

		// 设置HTML内容
		try {
			// 将MiniMultipart对象设置为邮件内容
			html.setContent(generateHtmlReport(), "text/html; charset=gbk");
			mainPart.addBodyPart(html);
			message.setContent(mainPart);
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	 public String generateHtmlReport(){
	    	Result groupResult = remoteApi.groupStatisDetail();
	    	JSONArray groupResultJson = null;
	    	
	    	if(groupResult != null && groupResult.getCode() == 10000){
	    		Object data = groupResult.getData();
	    		groupResultJson  = JSON.parseArray(JSON.toJSONString(data));
	    	}else{
	    		return null;
	    	}
	    	
	    	Result groupCaseStatis = remoteApi.statisGroupCaseByAuthor();
	    	JSONArray groupCaseStatisJson = null;
	    	
	    	if(groupCaseStatis != null && groupCaseStatis.getCode() == 10000){
	    		Object data = groupCaseStatis.getData();
	    		groupCaseStatisJson  = JSON.parseArray(JSON.toJSONString(data));
	    	}else{
	    		return null;
	    	}
	    	
	    	Result jenkinsResult = remoteApi.getPackageResult();
	    	JSONArray jenkinsResultJson = null;
	    	
	    	if(jenkinsResult != null && jenkinsResult.getCode() == 10000){
	    		Object data = jenkinsResult.getData();
	    		jenkinsResultJson  = JSON.parseArray(JSON.toJSONString(data));
	    	}else{
	    		return null;
	    	}
	    	
	    	StringBuilder html = new StringBuilder();
	    	html.append("<!DOCTYPE html><html><head><meta charset=gbk></head><body>");
	    	html.append("<div><p>Hi all,<br><br>&nbsp&nbsp测试用例及Jenkins打包结果如下：</p></div>");
	    	
	    	//测试集执行结果
	    	html.append("<div>");
	    	html.append("<div style=\"margin-left:10%\">");
	    	html.append("<p><a href=\"" + "http://192.168.1.121:8080/#/home/report/groupDetail" + "\">测试集执行结果：</a></p>");
	    	html.append("</div>");
	    	html.append("<div style=\"margin-left:20%\">");
	    	html.append("<table style=\"width:50%;text-align:left\">");
	    	html.append("<tr>");
	    	html.append("<th>测试集</th>");
	    	html.append("<th>执行时间</th>");
	    	html.append("<th>成功数</th>");
	    	html.append("<th>失败数</th>");
	    	html.append("<th>未执行数</th>");
	    	html.append("</tr>");
	    	html.append("<tr>");
	    	
	    	int size = groupResultJson.size();
	    	
	    	for(int i=0;i<size;i++){
	    		JSONObject jsonObj = groupResultJson.getJSONObject(i);
	    		String name = jsonObj.getString("name");
	    		String createTime = jsonObj.getString("createTime");
	    		String successSize = jsonObj.getString("successSize");
	    		String failSize = jsonObj.getString("failSize");
	    		String skipSize = jsonObj.getString("skipSize");
	    		html.append("<tr>");
	    		html.append("<td>" + name + "</td>");
	    		html.append("<td>" + createTime + "</td>");
	    		html.append("<td>" + successSize + "</td>");
	    		
	    		if("0".equals(failSize)){
	    			html.append("<td>" + failSize + "</td>");
	    		}else{
	    			html.append("<td style=\"background-color:red\">" + failSize + "</td>");
	    		}
	    		html.append("<td>" + skipSize + "</td>");
	    		html.append("</tr>");
	    	}
	    	
	    	html.append("</tr>");
	    	html.append("</table>");
	    	html.append("</div>");
	    	
	    	//按创建人统计
	    	html.append("<div>");
	    	html.append("<div style=\"margin-left:10%\">");
	    	html.append("<p><a href=\"" + "http://192.168.1.121:8080/#/home/report/jenkinsResult" + "\">测试集用例按创建人统计：</a></p>");
	    	html.append("</div>");
	    	html.append("<div style=\"margin-left:20%\">");
	    	html.append("<table style=\"width:40%;text-align:left\">");
	    	html.append("<tr>");
	    	html.append("<th>创建人</th>");
	    	html.append("<th>成功用例数</th>");
	    	html.append("<th>失败用例数</th>");
	    	html.append("</tr>");
	    	
	    	size = groupCaseStatisJson.size();
	    	
	    	for(int i=0;i<size;i++){
	    		JSONObject jsonObj = groupCaseStatisJson.getJSONObject(i);
	    		String createdBy = jsonObj.getString("createdBy");
	    		String successSize = jsonObj.getString("successSize");
	    		String failSize = jsonObj.getString("failSize");
	    		html.append("<tr>");
	    		html.append("<td>" + createdBy + "</td>");
	    		html.append("<td>" + successSize + "</td>");
	    		
	    		if("0".equals(failSize)){
	    			html.append("<td>" + failSize + "</td>");
	    		}else{
	    			html.append("<td style=\"background-color:red\">" + failSize + "</td>");
	    		}
	    		
	    		html.append("</tr>");
	    	}
	    	
	    	html.append("</tr>");
	    	html.append("</table>");
	    	html.append("</div>");
	    	
	    	//jenkins执行结果
	    	html.append("<div>");
	    	html.append("<div style=\"margin-left:10%\">");
	    	html.append("<p><a href=\"" + "http://192.168.1.121:8080/#/home/report/jenkinsResult" + "\">Jenkins打包结果：</a></p>");
	    	html.append("</div>");
	    	html.append("<div style=\"margin-left:20%\">");
	    	html.append("<table style=\"width:40%;text-align:left\">");
	    	html.append("<tr>");
	    	html.append("<th>环境</th>");
	    	html.append("<th>成功数</th>");
	    	html.append("<th>不确定数</th>");
	    	html.append("<th>失败数</th>");
	    	html.append("</tr>");
	    	
	    	Map<String, Map<String, Long>> map =
	    	jenkinsResultJson.stream().map(r->JSONObject.parseObject(JSON.toJSONString(r)))
	    	.collect(groupingBy(x->x.getString("env"), groupingBy(x->x.getString("result"), counting())));
	    	
	    	map.forEach((k,v)->{
	    		html.append("<tr>");
	    		html.append("<td>" + k + "</td>");
	    		
	    		if(v.containsKey("SUCCESS") && v.get("SUCCESS") != null){
	    			html.append("<td>" + v.get("SUCCESS").toString() + "</td>");
	    		}else{
	    			html.append("<td>" + "0" + "</td>");
	    		}
	    		
	    		if(v.containsKey("UNSTABLE") && v.get("UNSTABLE") != null){
	    			html.append("<td style=\"background-color:yellow\">" + v.get("UNSTABLE").toString() + "</td>");
	    		}else{
	    			html.append("<td>" + "0" + "</td>");
	    		}
	    		
	    		if(v.containsKey("FAILURE") && v.get("FAILURE") != null){
	    			html.append("<td style=\"background-color:red\">" + v.get("FAILURE").toString() + "</td>");
	    		}else{
	    			html.append("<td>" + "0" + "</td>");
	    		}
	    		
	    		html.append("</tr>");
	    	});
	    	
	    	html.append("</table>");
	    	html.append("</div>");
	    	
	    	html.append("</body></html>");
	    	
	    	logger.info("生成报告:{}", html.toString());
	    	return html.toString();
	    }
}
