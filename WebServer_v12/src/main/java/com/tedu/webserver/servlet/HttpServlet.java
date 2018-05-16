package com.tedu.webserver.servlet;
/*
 * 所有Servlet的超类，规定所有Servlet都应具备的功能.
 */
import java.io.File;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

public abstract class HttpServlet {
	/* 该方法用来处理业务逻辑*/
	public abstract void service(HttpRequest request,HttpResponse response);

	public void forward(String url,HttpRequest request,HttpResponse response) {
		File file = new File("webapps"+url);
		response.setEntity(file);
	}
}
