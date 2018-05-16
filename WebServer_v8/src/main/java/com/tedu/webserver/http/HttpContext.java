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
	static {  //初始化
		initMimeMapping();
	}
	private static void initMimeMapping(){  //初始化介质类型映射
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
				
				String key = ele.elementText("extension");    //
				
				String value = ele.elementText("mime-type");
				
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
	public static void main(String[] args) {
		String type = HttpContext.getMimeType("png");
		System.out.println(type);
		type = HttpContext.getMimeType("css");
		System.out.println(type);
		type = HttpContext.getMimeType("les");
		System.out.println(type);
	}
}
