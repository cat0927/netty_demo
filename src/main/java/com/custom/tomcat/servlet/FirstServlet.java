package com.custom.tomcat.servlet;

/**
 * DATE 2020-05-07
 *
 */
public class FirstServlet extends GServlet{
    @Override
    public void doGet(GRequest request, GResponse response) throws Exception {
        this.doPost(request, response);
    }

    @Override
    public void doPost(GRequest request, GResponse response) throws Exception {
        //response.write("This is First Servlet");
        response.writeNetty("This is First Servlet");
    }
}
