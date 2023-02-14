package com.ifyrz.http;

import cn.hutool.aop.ProxyUtil;

import cn.hutool.json.JSONUtil;
import com.ifyrz.entity.Request;
import com.ifyrz.utils.HttpUtil;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * program: socket_demo
 * <p>
 * 功能描述:
 *
 * @author : yuanzhan
 * @since : 2023-02-13 21:09
 **/
public class HttpTask implements Runnable{
    private Socket socket;
    
    public HttpTask(Socket socket){
        this.socket = socket;
    }
   
    @SneakyThrows
    @Override
    public void run() {
        if(socket== null){
            throw new IllegalArgumentException("socket can't be null");
        }
        try(
                final OutputStream outputStream = socket.getOutputStream();
                final PrintWriter out = new PrintWriter(outputStream);
                final InputStream inputStream = socket.getInputStream();
        ){
            Request httpRequest = HttpUtil.parse2request(inputStream);
                
            try {
                // 根据请求结果进行响应，省略返回
                String result = "";
               
                String httpRes = HttpUtil.buildResponse(httpRequest, result);
                out.print(httpRes);
            } catch (Exception e) {
                String httpRes = HttpUtil.buildResponse(httpRequest, e.toString());
                out.print(httpRes);
            }
            out.flush();

        }
    }

   
}
