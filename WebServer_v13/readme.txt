2018.04.17_Day01: 【V1版本】
一、
本版本搭建WebServer服务端基本结构。
创建com.tedu.webserver.core包，并在包中条加主类：WebServer

该类负责循环接受客户端的连接.并启动线程处理某个客户端的交互操作.该结构与之前聊天室服务端结构一致。
线程任务由CilentHandler类完成.并定义在core包中.

二、
在ClientHandler的run方法中读取客户端发送过来的内容并输出查看。

三、
Http请求（Request）
请求有客户端发送至服务端.具体格式在Http协议中有规定.
一个请求会包含三部分：
1：请求行
2：消息头
3：消息正文

1）请求行：
请求行是一行字符串，格式为：method url protocol(CRLF)
例如: GET /index.html HTTP/1.1(CRLF)

method:请求的方式，常见的GER，POST.
GET:地址栏请求，通常用户传体数据会直接体现在url部分.
POST:用户传第的数据包含在消息正文部分，传附件时一定会只用POST形式

url:客户端希望请求的资源的路径.
protocol:客户端发起的请求使用的HTTP协议版本.

CR：回车符，对应的编码：13
LF：换行符号，对应的编码：10

2）消息头：
消息头由若干行组成，每行都以CRLF结尾.每一行为一个具体的消息头内容.
格式为： name : value(CRLF)
Host: localhost:8080(CRLF)
Connection: keep-alive(CRLF)
Upgrade-Insecure-Requests: 1 (CRLF)
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36(CRLF)
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8(CRLF)
Accept-Encoding: gzip, deflate, sdch, br(CRLF)
Accept-Language: zh-CN,zh;q=0.8(CRLF)

最后一个消息头后面会再跟以个单独的CRLF，表示消息头部分完毕。
消息头内容是客户端发送给服务端的一些附带信息，比如告知服务端客户端的相关消息，是否含有消息正文内容等等...

3）消息正文：
消息正文是2进制数据，是请求中附带的用户提交的数据.它可能是用户上传的附件，注册信息等...
一个请求可能不包含消息正文部分，当一个请求包含消息正文部分时，通常消息头中会包含两个用于说明消息正文的头消息：
Content-Type:用于说明消息正文的数据时什么.
Content-Length:用于说明消息正文共多少字节. 

Http协议中的请求行和消息头部分是文本数据，但是字符集只能时ISO8859-1规定字符.所以是不能直接附带汉字信息的.

一个标准的(不含消息正文)请求如下：
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
Accept-Encoding: gzip, deflate, sdch, br
Accept-Language: zh-CN,zh;q=0.8


2018.04.17_Day01: 【V2版本】
本次改动，在ClientHandler中添加一个方法readLine用于按行读取字符串（以CRLF结尾）。
因为请求中的请求行与消息头都是以CRLF结尾一行一行的字符串。


2018.04.18_Day02: 【V3版本】
本次改动，将用户的请求解析为一个HttpRequest实例.
由于一个请求会包含很多信息，请求行的三个信息，若干的消息头信息，以及可能存在的消息正文内容.
为此我们设计一个类HttpRequest,根据请求的内容对应定义响应属性.这样我们就可以使用每个HttpRequest的
实例表示一个具体的请求的内容，便于后面使用.

1：在com.teddu.webserver包中添加一个子包：http
2：在http包中定义一个类HttpRequest,并更具请求的结构定义对应的属性，ger、ser方法以及解析方法等。
3：在ClientHandler的run方法中上来就根据socket解析请求并生成HttpRequest对象。


2018.04.18_Day02: 【V4版本】
本次改动，将用户在浏览器请求的页面响应给客户端：
1：在项目目录下新建一个目录webapps,用这个目录保存所有网站需要用到的资源.

2：在webapps下再新建一个子目录myweb，存放我们测试用的资源.

3：在myweb目录下新建一个index.html文件并完成页面内容.

4：在ClientHandler中从HttpRequest中获取url,得到用户想请求的资源的路径.

5：对应的从webapps目录中找到该资源.
例如：
浏览器输入的地址： http://localhost:8080/myweb/index.html
那么在请求行中，可以获取的url部分内容为：/myweb/index.html

ClientHandler在得到该url后，对应的从webapps目录找
File file = new File("webapps"+request.getUrl())

6：用一个Http标准的响应将该资源回复客户端.


2018.04.18_Day02: 【V5版本】
本次改动，结构调整.将响应也定义为一个类HttpResponse,然后将ClientHandler中响应客户端的细节
操作全部移动到HttpResponse中进行。

1：在http包中定义一个类HttpResponse定义构造方法，参数为Socket.

2：在HttpResponse中定义flush方法，用于响应客户端

3：定义属性entity,该属性用来记录这个响应要给客户端实际发送的文件内容（发送响应正文中的内容）

4：ClientHandler中调整代码，上来解析完request后先创建好response对象，以便将需要给客户端
发送的内容设置到响应对象上.在最后调用flush方法响应客户端.


2018.04.19_Day03: 【V6版本】
本次改动，完成HttpRequest中对请求中的消息头的解析工作。

1：在HttpRequest中添加一个属性：private Map<String,String> headers用于保存该请求中所
由的消息头内容。

2：添加一个方法：parseHeaders，在该方法中读取所有的消息头，并存入到headers中。

3：在构造方法中解析完请求行后，调用parseHeaders来解析消息头。

4：对外提供一个getHeader(String name)方法，可以通过消息头的名字获取其对应的值。


2018.04.19_Day03: 【V7版本】
本次改动，让响应中的响应头：Content-Type可以根据客户端请求的实际资源的类型来匹配对应的值。
例如：
请求页面xxx.html时，Content-Type: text/html
请求图片xxx.png时，Content-Type: image/png

思路：
创建一个Map(哈系表/散列表)：Key保存资源的后缀名，value保存对应的Content-Type的值.将来得到用户请求的资源
文件后，根据名字的后缀作为key取出对应的Content-Type值响应给客户端.

1：在Http包中定义一个类：HttpContext、这个类中保存HTTP协议的相关内容

2：在HttpContext中定义一个静态属性：MIME_MAPPING.         【注：MIME 是描述消息内容类型的英特网标准W3C制定.】
它时一个Map,Key保存资源后缀名，value保存Content-Type对应的值.     【相关介绍参看网站:www.w3school.com.cn】

3：在HttpContext的静态代码块中作初始化静态属性操作.

4：改动HttpResponse,发送响应头改为可以进行设置的.在HttpResponse中定义一个Map类型的属性headers并提供对
应的get,set方法

5：修改sendHeaders方法，发送响应头只需要遍历Headers中的头消息并全部发送给客户端.

6：在setEntity方法中做一个操作，当设置了一个要响应的实体文件后，将Content-Type与Content-Length这两个头设置好.



2018.04.22_Day04: 【V8版本】
本次改动，使用tomcat安装目录中conf/web.xml文件，该文件有所有介质类型映射信息.将这些信息读取出来，初始化HttpContext
中的静态属性：MIME_MAPPING.

1：在当前项目目录中新建一个目录：conf

2：将tomcat安装目录中conf/web.xml赋值到我们的conf中

3：修改HttpCOntext初始化介质类型映射的方法：
initMimeMapping()



2018.04.23_Day05: 【V9版本】
本次改动，添加404响应：当客户端请求的路径无效果时，服务端会回复404错误，表示客户端请求无效。
响应中第一行为状态行，其中的状态代码404就表示客户端请求无效。

1：在webapps目录中添加一个目录 sys 这里面存放一些共用的资源.比如404错误页面，无论哪个应用
(web,myweb)对应的资源没有找到，都使用这个页面响应。

2：将HttpResponse的状态代码改变为可以进行设置的.
   2.1：添加属性：int statusCode.
        添加属性：String statusReason
   2.2：添加设置状态代码的方法： void setStatusCode(int code).
   		设置给定的状态代码，并根据状态代码从HttpContext中找到对应的描述设置到属性上.
   2.3：修改sendStatusLine()方法，将设置的状态代码与状态描述发送.

3：在HttpContext中添加一个静态属性：
  private static Map<Integer,String> STATUS_CODE_REASON_MAPPING 
  
4：在HttpContext中定义一个静态方法：
  private static void initStatusCodeReasonMapping()
   初始化状态代码与描述的映射关系.
   
5：在HttpContext中定义一个静态方法：
  public static String getStatusReason(int code)  
   可以根据给定的状态代码获取对应的状态描述.
   
6：在ClientHandler中添加响应404的分支.   



2018.04.23_Day05: 【V10版本】
本次改动、完成处理业务操作：注册功能.

通过本版本更新，理解服务端如何处理一个业务的流程.

1：在webapps/myweb/下提供注册页面reg.html

2：当用户注册提交时，GET形式提交会将用户数据包含在url部分提交过来，所以在解析请求时，要对请求行
中的url部分做进一步解析工作.将请求的路径与用户传递的数据拆分出来，分别保存在HttpRequest中.
    2.1：在HttpRequest中定义三个新的属性：
      String requestURL:请求路径部分.
      String queryString:参数部分.
     	Map<String,String> parameters:每个具体参数
     	例如：
     	/myweb/reg?username=123&password=123
     	请求的路径部分	          参数部分
     	注：在一个URL中规定了使用"？"部分进行分割.
     	
    2.2:添加一个方法：parseURL
        该方法对请求行中url部分进行进一步的解析工作，并将解析出来的内容对应的设置到2.1定义的相关属
        性上.
        
    2.3:在parseRequestList方法中得到url后，调用2.2定义的parseURL,对请求进行进一步解析.    
    
3：修改ClientHanler获取请求路径的方式，将原有的
    调用String url = request.getUrl();
    改为String url = request.getRequestURI();
    
4：针对用户请求添加新的分支判断，在ClientHandler原有只判断请求的静态资源是否存在之前添加判断是否请求的为业务.若
为某个业务，则调用业务处理类完成该业务操作.

5：新建一个包com.tedu.webserver.servlet,并在该包中定义一个用于处理注册业务的类：RegServlet,并完成service
方法的功能.当完成注册功能后跳转注册成功页面：reg_success.html

6：在webapps/myweb/下面新建页面reg_success.html



2018.04.24_Day06: 【V11版本】
一、本次改动，完成处理业务操作：登录功能.

1：在webapps/myweb目录下创建用于登录功能的页面：
  login.html   登录页                            
    (1)form表单中只需要用户名及密码输入框.
  	 (2)action指定地址"login"
  	 (3)这样当点击登录时，form提交的路径应当为：/myweb/login?username=xxx&password=xxx
  
 
  login_cuccess.html  登录成功提示页           
  login_fail.html  登录失败的提示页
  
2：在com.tedu.webserver.servlet包中新建一个类：
  LoginServlet并定义方法：
  public void service(HttpRequest request,HttpResponse response)
  
3：在ClientHandler中添加一个新的分支，当请求的是登录业务时，实例化LoginServlet并调用service方法.

4：完成LoginServlet的service方法：
   4.1：先从request中获取login.html表单中用户输入的用户名以及密码
   4.2：使用RandomAccessFile读取user.dat文件，首先读取32字节判断用户名，若不是、则直接移动指针到下一条记录，直
   到找到该用户.若找到该用户后，再读取32字节比对密码，若密码输入一致则设置response跳转登录成功页面，否则跳转登录失
   败页面.
   
二、抽象出一个HttpServlet,作为所有Servlet的超类.定义共性:
    比如：所有Servlet都有service方法，以及都会进行跳转页面操作.
    
   HttpServlet定义为抽象类.
   因为所有派生类都有service方法，但是里面处理的业务不同，代码不同，所以service方法时和定义为抽象方法.
   而所有派生类需要跳转页面，这个操作大家都一致，那么就定义一个实现方法，供派生类使用.
   
   
   
2018.04.25_Day07: 【V12版本】
本次改动，使用反射机制，完成Servlet的动态创建.这样做可以使得请求与对应的Servlet变为可以配置的.而不需要
因为一个新的业务修改ClientHandler添加分支的做法.

1：在com.tedu.webserver.core中添加一个新的类：ServerContext,用于保存服务端配置的信息。

2：在ServerContext中添加一个静态属性：
  private static Map<String,String> SERVLET_MAPPING;
  
3：定义一个初始化Servlet映射信息的方法：
  private static void initServletMapping();
  
4：添加静态代码块，初始化ServerContext相关信息. 

5：对外提供用于获取Servlet信息的相关方法

完善：  
1：在conf目录下新建一个servlets.xml文件，并定义好所有的Servlet的配置信息。

2：修改ServerConext的initServletMapping()方法,改为加载conf/servlets.xml文件，将所有Servlet配置
信息设置到SERVLET——MAPPING属性中.

3：完成后，添加修改用户密码功能.
    3.1：在weapps/myweb/下新建update.html页面，该页面要求用户输入用户名及新的密码
    然后form表单actions="update";
    
    3.2：在com.tedi.webserver.servlet包中添加：UpdateServlet，继承自HttpServlet并完成service功能
    
    3.3：在conf/servlets.xml文件中添加该Servlet配置信息，如：
    <servlet>
    		<url>/myweb/update</rul>
    		<className>com.tedu.webserver.servlet.Update</className>
    </servlet>
    

    
2018.04.25_Day07: 【V13版本】
本次改动、支持空请求：
Http协议中规定，客户端可以发送空请求，即：
连接服务端后，不发送任何内容（没有请求行，消息头，消息正文）.对于这样的操作，服务端直接断开连接即可.
无需回应客户端。