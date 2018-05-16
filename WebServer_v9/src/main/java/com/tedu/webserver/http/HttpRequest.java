package com.tedu.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 *  Http请求：
 *   该类的每一个实例用于表示客户端发送过来的一个请求内容。
 */
public class HttpRequest {

	private Socket socket;    //存一个客户端传来的信息
	private InputStream in;	  //存输入流

	//请求行相关信息定义：
	private String method;    //请求的方式
	private String url;		  //请求的资源路径
	private String protocol;  //请求所使用的协议版本

	//消息头相关信息定义：
	private Map<String,String> headers = new HashMap();  

	public HttpRequest(Socket socket) {  //构造方法      初始化
		try {

			this.socket = socket;  //将socket传进来	
			this.in = socket.getInputStream();   //通过socket获取输入流读取客户端发送的请求内容
			/*
			 * 开始解析请求内容：
			 * 1：解析请求行
			 * 2：解析消息头
			 * 3：解析消息正文
			 */
			parseRequestLine();     //调用下面的方法实现第一步：解析请求行
			parseHeaders();			//调用下面的方法实现第二部：解析消息头

		} catch (Exception e) {

		}
	}
	private void parseHeaders(){  //解析消息头
		System.out.println("开始解析消息头！");
		/*
		 *    循环读取每一行（若干消息头），当读取的这行字符串是空字符串时，说明单独读取了CRLF，那么
		 *就可以停止读取消息头操作。
		 * 
		 *    每读取一个消息头时，将消息头的名字作为Key消息头的值作为value，存入到headers这个map
		 *中，最终完成解析消息头工作。
		 */
		String line = null;
		while(true) {
			line = readLine();
			if("".equals(line)) {
				break;
			}
			String [] arr = line.split(":\\s");
			headers.put(arr[0], arr[1]);
		}
		System.out.println("headers:"+headers);
		System.out.println("解析消息头完毕！");
	}

	private void parseRequestLine() {   //新定义一个方法，专门用来解析请求行
		/*
		 * 第一步：通过输入流读取一行字符串，相当于读取了请求行内容。
		 * 第二步：按照空格拆分请求行，可以得到对应的三部分内容。
		 * 第三步：分别将methid、url、protocol设置到对应的属性上完成请求行的解析工作
		 */	
		String line = readLine();
		System.out.println("请求行："+line);

		String [] data = line.split("\\s");
		/*
		 * 这里可能出现下标越界错误，后期优化
		 */
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];

		//		System.out.println("method:"+method);
		//		System.out.println("url:"+url);
		//		System.out.println("protocol:"+protocol);

	}
	private String readLine() {   //读取一行字符串，以CRLF结尾为一行
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

	public String getMethod() {    //获取请求的方式
		return method;
	}

	public String getUrl() {	    //获取请求的资源路径
		return url;
	}

	public String getProtocol() {  //获取请求使用的协议版本
		return protocol;
	}


}
