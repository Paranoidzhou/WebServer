package com.tedu.webserver.http;

import java.util.HashMap;
import java.util.Map;

/*
 * Http协议相关内容定义
 */
public class HttpContext {
	/*
	 * 介质类型映射：
	 * key:资源后缀名.
	 * value:Content-Type中对应的值.
	 */
	private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	static {  //初始化
		initMimeMapping();
	}
	private static void initMimeMapping(){  //初始化介质类型映射
		MIME_MAPPING.put("html", "text/thml");
		MIME_MAPPING.put("jpeg", "image/jpeg");
		MIME_MAPPING.put("png", "image/png");
		MIME_MAPPING.put("gif", "image/gif");
		MIME_MAPPING.put("css", "text/css");
		MIME_MAPPING.put("js", "application/javascript");
	}
	public static String getMimeType(String name) {
		return MIME_MAPPING.get(name);
	}
}
