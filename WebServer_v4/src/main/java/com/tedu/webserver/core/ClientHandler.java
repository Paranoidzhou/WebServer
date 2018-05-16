package com.tedu.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			HttpRequest request = new HttpRequest(socket); //(1)创建request对象，把socket传过来就可以了
			
			String url = request.getUrl();      //(2)获取请求的路径
			System.out.println("url:"+url);
			
			File file = new File("webapps"+url);  //new一个file实例用来获取括号里的路径文件
			if(file.exists()) {							//判断没有这个文件
				System.out.println("找到该文件！！！");
				/*
				 * 将该文件内容回复给客户端
				 * 通过socket获取输出流，给客户端发送回复。
				 */
				OutputStream out = socket.getOutputStream();//创建一条输出流，用于发送字节数据
				String line = "Http/1.1 200 OK";	           //发送消息行内容
				out.write(line.getBytes("ISO8859-1"));  	  //定义http协议唯一指定字符集 ISO8859-1
				out.write(13);  //written  CR
				out.write(10);  //written  LF
				
				line = "Content-Type: text/html";           //发送消息行内容
				out.write(line.getBytes("ISO8859-1"));  	  //定义http协议唯一指定字符集 ISO8859-1
				out.write(13);  //written  CR
				out.write(10);  //written  LF
				
				line = "Content-Length: "+file.length();    //发送字节长度
				out.write(line.getBytes("ISO8859-1"));      //定义http协议唯一指定字符集 ISO8859-1
				out.write(13);
				out.write(10);
				
				//响应头的尾部必须再写一个CRLF以告知服务器客户端发送完了
				out.write(13);  
				out.write(10);
				
				//发送响应正文：
				FileInputStream fis = new FileInputStream(file);  //创建一个文件输入流
				byte [] data = new byte[1024*10];						//创建一个缓冲区 1024KB
				int len = -1;		
				while((len = fis.read(data))!=-1) {						//判断读取到的字节不等于-1;
					out.write(data,0,len);									//写出字节文件
				}	
				fis.close();													//关闭流
			}else {
				System.out.println("error:404");
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
