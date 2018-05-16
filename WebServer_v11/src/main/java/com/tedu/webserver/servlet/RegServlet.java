package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/*
 * 处理注册业务：
 */
public class RegServlet extends HttpServlet {
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * 注册业务的处理流程：
		 * 1：通过request获取用户注册信息.
		 * 2：将信息写入文件user.dat中.
		 * 3：设置response对象给客户端响应注册结果.
		 */

		//第一步：通过request获取用户注册信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));

		//第二步：将信息写入文件user.dat中.
		try(
				RandomAccessFile raf = new RandomAccessFile("user.dat","rw");	
				){
			raf.seek(raf.length());
			byte [] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);

			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);

			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);

			raf.writeInt(age);

			System.out.println("注册完毕！！！");

			//第三步：响应注册成功页面：
			forward("/myweb/reg_success.html",request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
