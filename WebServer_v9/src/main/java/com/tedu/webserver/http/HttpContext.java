package com.tedu.webserver.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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

	/*
	 * 状态代码与描述映射：
	 * key:状态代码
	 * value:对应的状态描述
	 */
	private static Map<Integer,String> STATUS_CODE_REASON_MAPPING  = new HashMap<Integer,String>();

	static {  //初始化
		initMimeMapping();
		initStatusCodeReasonMapping();
	}

	/* 初始化状态代码与描述的映射*/
	//init：初始化  Status:状态  Code:代码  Reason:描述
	private static void initStatusCodeReasonMapping() {
		STATUS_CODE_REASON_MAPPING.put(200, "OK");
		STATUS_CODE_REASON_MAPPING.put(302, "Moved Temporarily");
		STATUS_CODE_REASON_MAPPING.put(404, "Not Found");
		STATUS_CODE_REASON_MAPPING.put(500, "Internal Server Error");
	}

	/*初始化介质类型映射*/
	private static void initMimeMapping(){  
		//		MIME_MAPPING.put("html", "text/thml");
		//		MIME_MAPPING.put("jpeg", "image/jpeg");
		//		MIME_MAPPING.put("png", "image/png");
		//		MIME_MAPPING.put("gif", "image/gif");
		//		MIME_MAPPING.put("css", "text/css");
		//		MIME_MAPPING.put("js", "application/javascript");
		/*
		 * 使用DOM4J读取并解析conf/web.xml文件.
		 * 将根据标签中所有名为<mime-mapping>的子标签获取出来，并将其子标签：
		 * <extension>中间的文本作为key.
		 * <mime-type>中间的文本作为value.
		 * 存入到MIME_MAPPING中完成初始化操作.
		 */
		List<String>list = new ArrayList<String>();
		try {
			//创建SAXReader:
			SAXReader reader =  new SAXReader();	  

			//使用SAXReader读取xml文档并得到对应的Document对象:
			Document doc = reader.read(new File("conf/web.xml"));

			//通过Document获取根元素:
			Element root = doc.getRootElement();

			//将根据标签中所有名为<mime-mapping>的子标签获取出来存到list集合:
			List<Element>elemap = root.elements("mime-mapping");

			for (Element ele : elemap) {  //遍历集合

				String key = ele.elementText("extension");    //将遍历到的extension内的文本存到key中

				String value = ele.elementText("mime-type");  //将遍历到的mime-type内的文本存到value中

				MIME_MAPPING.put(key, value);
			}
			System.out.println("初始化完成，总计："+MIME_MAPPING.size()+"个");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*根据给定的资源后缀名获取对应的Content-Type值*/
	public static String getMimeType(String name) {
		return MIME_MAPPING.get(name);
	}

	/*根觉给定的状态代码获取对应的状态描述*/
	public static String getStatusReason(int code) {
		return STATUS_CODE_REASON_MAPPING.get(code);
	}

	public static void main(String[] args) {
		String type = HttpContext.getMimeType("png");
		System.out.println(type);
		String reason = HttpContext.getStatusReason(404);
		System.out.println(reason);

	}
}
