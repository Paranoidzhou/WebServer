package com.tedu.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
			InputStream in = socket.getInputStream();
		int d = -1;
		while((d = in.read())!=-1) {
			System.out.print((char)d);
		}

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
