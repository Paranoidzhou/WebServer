package com.tedu.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * WebServer主类
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;

	public WebServer() {  	//初始化服务端
		try {
			server = new ServerSocket(8080);  //Tomcat的默认服务端口是8080
			
			threadPool = Executors.newFixedThreadPool(40); //限制最大40个线程
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void start(){    //服务端启动操作
		try {
			while(true) {
				Socket socket = server.accept();  
				ClientHandler handler = new ClientHandler(socket);//启动线程处理该客户端
				threadPool.execute(handler);  //调用线程池处理
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
		
	}
}
