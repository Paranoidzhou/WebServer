package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
/*
 * 处理登录业务：
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * 登录业务的处理流程：
		 * 1：通过request中获取用户输入的用户名以及密码。
		 * 2：使用RandomAccessFile读取user.dat文件，首先读取32字节判断用户名，
		 * 若不是、则直接移动指针到下一条记录，直到找到该用户.若找到该用户后，再读
		 * 取32字节比对密码，若密码输入一致则设置response跳转登录成功页面，否则
		 * 跳转登录失败页面.
		 */
		//第一步：通过request获取用户输入的信息
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try(
				RandomAccessFile raf = new RandomAccessFile("user.dat","r");  //读user.dat获取注册好的用户信息
				) {
				boolean tf = false;		//定义一个开关
				for (int i = 0; i < raf.length()/100;i++){  
					raf.seek(i*100);    //移动指针到开始位置
					byte [] data = new byte[32]; //每次读32个字节
					raf.read(data);   //读字节
					String name = new String (data,"UTF-8").trim(); //将读到的前32个字节赋值给 name
					if(name.equals(username)) {	//判断读到的帐号是否与注册成功的帐号一样
						raf.read(data);   
						String pwd = new String (data,"UTF-8").trim(); //判断帐号相同的之后这一条读密码
						if(pwd.equals(password)) {   //将读到的帐号后面32字节的密码进行判断
							tf = true;			//密码也正确将开关设置 true
						}
						break;
					}
				}
			
			if(tf) {
				forward("/myweb/login_success.html",request,response);   //设置登录成功跳转页面
			}else {
				forward("/myweb/login_fail.html",request,response);      //设置登录失败跳转页面
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
