package com.custom.tomcat.servlet;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;

/**
 * DATE 2020-05-07
 * tomcat 三个阶段
 */
@Slf4j
public class GTomcat {
    private int prot = 8080;
    private ServerSocket server;
    private Map<String, GServlet> servletMap = Maps.newHashMap();

    private Properties webXml = new Properties();

    /**
     * 加载配置
     */
    private void init() {
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");
            webXml.load(fis);
            for (Object k: webXml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$","");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");

                    GServlet obj =  (GServlet)Class.forName(className).newInstance();
                    servletMap.put(url, obj);
                }
            }
        } catch (Exception e) {
            log.error("init, error", e);
        }
    }

    /**
     * 服务就绪阶段
     */
    public void start() {
        init();
        try {
            server = new ServerSocket(this.prot);
            System.out.println("GTomcat 启动，监听端口是：" + this.prot);
            // 等待用户请求
            while (true) {
                Socket client = server.accept();

                process(client);
            }
        } catch (Exception e) {
            log.error("start error", e);
        }
    }

    /**
     * 接收请求
     * @param client
     * @throws Exception
     */
    public void process(Socket client) throws Exception {
        InputStream in =  client.getInputStream();
        OutputStream ou = client.getOutputStream();

        GRequest request = new GRequest(in);
        GResponse response = new GResponse(ou);

        String url = request.getUrl();
        if (servletMap.containsKey(url)) {
            servletMap.get(url).service(request ,response);
        } else {
            response.write("404 response");
        }

        ou.flush();
        ou.close();

        in.close();
        client.close();
    }
}
