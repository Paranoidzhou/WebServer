package com.tedu.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.tedu.webserver.http.HttpRequest;

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
			HttpRequest request = new HttpRequest(socket); //创建request对象，把socket传过来就可以了
			
			
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
