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