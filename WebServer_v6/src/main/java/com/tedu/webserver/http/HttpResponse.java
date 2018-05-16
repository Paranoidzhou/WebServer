package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
 * 响应对象：
 * 每一个实例用于表示一个实际发送给客户端的响应内容。
 */
public class HttpResponse {
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
			String line = "Content-Type: text/html";           //发送消息行内容
			println(line);
			
			line = "Content-Length: "+entity.length();    //获取字节长度
			println(line);	
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

	public void setEntity(File entity) {
		this.entity = entity;
	}

}
