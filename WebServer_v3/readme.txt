2018.04.18_Day02: 【V3版本】
一、
本次改动，将用户的请求解析为一个HttpRequest实例.
由于一个请求会包含很多信息，请求行的三个信息，若干的消息头信息，以及可能存在的消息正文内容.
为此我们设计一个类HttpRequest,根据请求的内容对应定义响应属性.这样我们就可以使用每个HttpRequest的
实例表示一个具体的请求的内容，便于后面使用.

1：在com.teddu.webserver包中添加一个子包：http
2：在http包中定义一个类HttpRequest,并更具请求的结构定义对应的属性，ger、ser方法以及解析方法等。
3：在ClientHandler的run方法中上来就根据socket解析请求并生成HttpRequest对象。

