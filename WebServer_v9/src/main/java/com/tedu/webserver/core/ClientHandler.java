package com.tedu.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/*
 *  线程任务类，用于处理某个客户端的请求并予以响应。
 */

public class ClientHandler implements Runnable {
	
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		System.out.println("一个客户端连接了！！！");
		try {
			/*
			 * 处理客户端请求的流程：
			 * 第一步：解析请求
			 * 第二步：处理请求
			 * 第三步：给予响应
			 */
			HttpRequest request = new HttpRequest(socket); //(1)创建request对象，把socket传过来就可以了
			HttpResponse response = new HttpResponse(socket);
			
			String url = request.getUrl();      //(2)获取请求的路径
			System.out.println("url:"+url);
			
			File file = new File("webapps"+url);
			if(file.exists()) {
				System.out.println("找到该文件！！！");
				response.setEntity(file);  //将客户端请求的文件设置到response中
			}else {
				/* 响应404
				 * 1：设置响应的状态代码为404
				 * 2：设置错误页面
				 */
				response.setStatusCode(404);       //第一步：设置响应的状态代码404.
				
				File notFoundPage = new File("webapps/sys/404.html");  //第二步：设置错误页面
				response.setEntity(notFoundPage);
			}
			
			response.flush();     //响应客户端
			
		}catch(Exception e) {
			
		}finally {
			try {			//与客户端断开连接
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
