package com.tedu.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.HttpServlet;
import com.tedu.webserver.servlet.LoginServlet;
import com.tedu.webserver.servlet.RegServlet;


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
			
			String url = request.getRequestURI();      //(2)获取请求的路径
			
			System.out.println("url:"+url);
			System.out.println("【rul长度：】"+url.length());

			if(ServerContext.hasServlet(url)){
				//处理注册业务
				//1：获取该请求对应的Servlet的名字：
				String servletName = ServerContext.getServletByUrl(url);
				
				//2：通过反射机制，加载该Servlet：
				Class cls = Class.forName(servletName);
				
				//3：实例化该Servlet：
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				
				//4：调用service方法处理业务
				servlet.service(request, response);
				
			}else{
				File file = new File("webapps"+url);
				if(file.exists()){
					System.out.println("找到该文件");
					//将客户端请求的文件设置到response中
					response.setEntity(file);				
				}else{
					//响应404错误
					//1设置响应的状态代码为404
					response.setStatusCode(404);
					//2设置错误页面
					File notFoundPage = new File("webapps/sys/404.html");
					response.setEntity(notFoundPage);
				}
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
