package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * 响应对象：
 * 每一个实例用于表示一个实际发送给客户端的响应内容。
 */
public class HttpResponse {
	/*
	 * 响应头相关信息定义
	 * Key：响应头的名字
	 * value：响应头对应的值
	 */
	private Map<String,String>headers = new HashMap<String,String>();

	/*
	 * 响应正文对应的实体文件。
	 * 当设置了该文件，那么将来该文件的数据就会作为当前响应的响应正文内容被发送给客户端。
	 */
	private File entity;
	private Socket socket;
	private OutputStream out;   //该输出流通过socket获得，用于给客户端回复内容

	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out =socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void flush(){   //将当前响应内容以HTTP响应的格式回复给客户端
		/*
		 * 回复客户端需要按照HTTP的响应格式来发送
		 * 1：发送状态行
		 * 2：发送响应头
		 * 3：发送响应正文 
		 */
		sendStatusLine();   //调用下面的方法发送状态行
		sendHeader();		  //调用下面的方法发送响应头
		senContent();		  //调用下面的方法发送状响应正文

	}
	private void sendStatusLine() {    //发送状态行
		try {
			String line = "Http/1.1 200 OK";	           //发送消息行内容
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void sendHeader() {		  //发送响应头
		try {
			/*
			 * 发送响应头：
			 * 遍历headers这个Map将所有头发送 
			 */
			for (Entry <String,String>header : headers.entrySet()) {
				String name = header.getKey();
				String value = header.getValue();
				println(name+":"+value);

			}
			println("");	//发送一个空字符串在此处代表单独发送CRLF

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void senContent() {	     //发送状态行
		try(   //将流写在try()括号里面，出异常会自动关闭流，此特性需要JDK1.7版本及以上
				FileInputStream fis = new FileInputStream(entity);  //创建一个文件输入流
				) {
			byte [] data = new byte[1024*10];						//创建一个缓冲区 1024KB
			int len = -1;		
			while((len = fis.read(data))!=-1) {						//判断读取到的字节不等于-1;
				out.write(data,0,len);									//写出字节文件
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void println(String line) {  //向客户端发送一行字符串（自动以CRLF结尾）
		try {
			out.write(line.getBytes("ISO8859-1"));  	  //定义http协议唯一指定字符集 ISO8859-1
			out.write(13);  //written  CR
			out.write(10);  //written  LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getEntity() {
		return entity;
	}
	/*
	 * 设置响应的实体对象，设置后会自动添加两个头信息：
	 * Content-Type 和 Content-Length
	 * 若文件没有后缀名，则COntent-Type不会被添加
	 */
	public void setEntity(File entity) {
		/*
		 * 当设置一个响应实体文件时，自动添加针对该文件的两个头信息：
		 * Content-Type 和 Content-Length
		 */
		//                      .html   .mp3  .3gp
		if(entity.getName().matches(".+\\.[a-zA-Z0-9]")) { //  【.表示任意字符】 【\\.表示.】【+表示字符出现一次及一次以上】 
			String ext = entity.getName().split("\\.")[1];    //获取该文件的后缀名
			headers.put("Content-Type", HttpContext.getMimeType(ext));
		}
		headers.put("Content-Length",entity.length()+"");

		this.entity = entity;
	}
	/* 设置一个消息头*/
	public void putHeader(String name,String value) {
		this.headers.put(name, value);
	}
	public String getHeader(String name) {
		return this.headers.get(name);
	}

}
