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
			String line = redLine(in);
			System.out.println(line);
		}catch(Exception e) {
		}finally {
			try {			//与客户端断开连接
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	private String redLine(InputStream in) {   //读取一行字符串，以CRLF结尾为一行
		try {  //顺序从in中读取每个字符，当连续读取了CR，LF时停止.并将之前读取的字符以一个字符串形式返回即可。
			StringBuilder builder = new StringBuilder();//定义一个对象(StringBuilde)实现字符串拼接.
			int d = -1;											  //定义一个值，用来判定读到的字符
			char c1 = 'a',c2='a';                       //c1用来表示上次读到的字符，c2用来表示本次。
			while((d = in.read())!=-1) {					  //判断是否还有字符可以读
				c2 = (char)d;									  //将本次读到的字符赋值给本次读到的引用类型c2
				if(c1==13&&c2==10) {							  //判断c1是否为CR（编码：13），c2是否为LF（编号10）
					break;										  //如果是，则跳出循环。如果不是则不执行此段代码。
				}
				builder.append(c2);							  //将本次读到的字符写入，然后与下次读到的字符用appebd()方法进行字符串拼接
				c1 = c2;											  //循环最后一步，将本次读到的字符赋值给代表上次读到的字符c2
			}
			return builder.toString().trim();				//将读完的拼接之后的字符串输出，此处的trim（）的目的是去除最后的CR符号（它是一个空白）
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
