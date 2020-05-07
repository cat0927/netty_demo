package com.custom.tomcat.servlet;

/**
 * DATE 2020-05-07
 * 自定义
 */
public abstract class GServlet {

    public void service(GRequest request, GResponse response) throws Exception {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(GRequest request, GResponse response) throws Exception;

    public abstract void doPost(GRequest request, GResponse response) throws Exception;
}
