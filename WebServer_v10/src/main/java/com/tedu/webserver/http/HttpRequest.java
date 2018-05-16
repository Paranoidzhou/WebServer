package com.tedu.webserver.http;

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
	private String requestURI;//请求路径中的请求部分（rul中"?"左边的内容）
	private String queryString;//请求路径中的参数部分（rul中"?"右边的内容）
	private String protocol;  //请求所使用的协议版本

	//消息头相关信息定义：
	private Map<String,String> headers = new HashMap();  

	/*
	 * 客户端传递过来的参数内容：
	 * key:参数名
	 * value:参数值
	 */
	private Map<String,String> parameters = new HashMap<String,String>();

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

	private void parseRequestLine() {   //定义一个方法，专门用来解析请求行
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

		parseURL();  //调用下面parseURL()方法，进一步解析url
		/*
		 * rul可能存在的两种样子如：
		 * /myweb/reg.html
		 * /myweb/reg?username=123&password=123....
		 */
	}

	private void parseURL() {  //进一步解析请求行中的url部分
		/*
		 *     由于请求有两种情况，带参数或不带参数，那么要先判断url是否带参数，不带则直接将url的
		 * 值赋值给属性requestURL即可.
		 *    若带参数，则需要按照？先拆分url，然后将？左边内容设置到requestURI中，将？右边的内
		 * 容设置到queryString中、并进一步对参数部分解析，将每个参数解析出来，将参数名作为key,
		 * 参数值作为value存入到parameters这个Map类型的属性中.
		 */
		System.out.println("进一步解析url！！");

		if(this.url.contains("?")) {    //contains：包含     本行表示判断是否包含?
			String []data = this.url.split("\\?");  //拆分 ？ 左右两边的值
			this.requestURI = data[0];     
			if(data.length>1){
				queryString = data[1];
				data = queryString.split("&");
				for (String str : data) {
					String [] paraArr = str.split("=");
					if(paraArr.length>1) {
						parameters.put(paraArr[0],paraArr[1]);	
					}else {
						parameters.put(paraArr[0],"");	
					}
				}
			}else {	
				this.queryString = "";
			}
		}else {
			this.requestURI = this.url;
		}

		System.out.println("requestURI:"+requestURI);     //接收URI
		System.out.println("queryString:"+queryString);   //查询字符串
		System.out.println("parameter:"+parameters);      //参数

		System.out.println("进一步解析url完毕！！！");
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

	public String getRequestURI() {//获取接收的URI
		return requestURI;
	}

	public String getQueryString() {//获取查询的字符串
		return queryString;
	}
	public String getParameter(String name) {  //获取给定名字对应的参数
		return this.parameters.get(name);
	}
}
