package com.telno.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/number")
public class SelectNoControlle {
	@RequestMapping("/select")
	@ResponseBody
	public Object select(String telNumber){
		Map<String,Object> map = new HashMap<String,Object>();
		String url = "http://www.ip138.com:8080/search.asp?action=mobile&mobile="+telNumber;
		url = String.format(url, telNumber);
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements els = doc.getElementsByClass("tdc2");
			map.put("address", els.get(1).text());
			map.put("numberType", els.get(2).text());
			map.put("telNumber", telNumber);
			/*System.out.println("归属地：" + els.get(1).text());
			System.out.println("类型：" + els.get(2).text());
			System.out.println("区号：" + els.get(3).text());
			System.out.println("邮编：" + els.get(4).text().substring(0, 6));*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping("/send")
	@ResponseBody
	public Object sendMessage(String telNumber,String mesContent) throws UnsupportedEncodingException, IOException{
		Map<String,Object> map = new HashMap<String,Object>();
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn"); 
		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
		NameValuePair[] data ={ 
				new NameValuePair("Uid", "1217118869@qq.com"),
				new NameValuePair("Key", "eb8b0041e1439fba566a"),
				new NameValuePair("smsMob",telNumber),
				new NameValuePair("smsText",mesContent)
		};
		post.setRequestBody(data);

		client.executeMethod(post);
		//Header[] headers = post.getResponseHeaders();
		//int statusCode = post.getStatusCode();
		//System.out.println("statusCode:"+statusCode);
		/*for(Header h : headers){
			System.out.println(h.toString());
		}*/
		String result = new String(post.getResponseBodyAsString().getBytes("gbk")); 
		System.out.println(result); //打印返回消息状态
		map.put("result", result);
		post.releaseConnection();
		return map;
	}
}
