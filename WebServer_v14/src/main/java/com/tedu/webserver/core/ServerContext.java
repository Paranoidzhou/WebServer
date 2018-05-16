package com.tedu.webserver.core;

import java.util.HashMap;
import java.util.Map;
/*  服务器相关信息定义*/
public class ServerContext  {
	/*
	 * Servlet映射信息:
	 * key:rul.
	 * value:Servlet名字.
	 */
	private static Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();

	static {   //静态块初始化
		initServletMapping();
	}

	/*初始化Servlet*/
	private static void initServletMapping() {
		SERVLET_MAPPING.put("/myweb/reg","com.tedu.webserver.servlet.RegServlet");
		SERVLET_MAPPING.put("/myweb/login","com.tedu.webserver.servlet.LoginServlet");
	}

	/*检查给定的url是否对应Servlet处理*/
	public static boolean hasServlet(String url) {
		/*用给定的url检查是否作为key存在即可直到是否对应一个Servlet*/
		return SERVLET_MAPPING.containsKey(url);
	}

	public static String getServletByUrl(String url) {
		/*根据给定的url获取对应的Servlet名字*/
		return SERVLET_MAPPING.get(url);
	}

	public static void main(String[] args)throws Exception {

		boolean have = hasServlet("/myweb/reg");
		System.out.println(have);
		
		String name = getServletByUrl("/myweb/reg");
		System.out.println(name);

	}
}
