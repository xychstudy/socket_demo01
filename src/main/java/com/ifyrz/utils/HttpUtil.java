package com.ifyrz.utils;

import com.ifyrz.entity.Request;
import com.sun.deploy.util.StringUtils;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * program: socket_demo
 * <p>
 * 功能描述:
 *
 * @author : yuanzhan
 * @since : 2023-02-13 20:53
 **/
public class HttpUtil {

    /**
     * 根据标准的http协议，解析请求行 
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestLine(BufferedReader reader, Request request) throws IOException {
        String[] strs = StringUtils.splitString(reader.readLine()," ");
        assert  strs.length == 3;
        request.setMethod(strs[0]);
        request.setUri(strs[1]);
        request.setVersion(strs[2]);
    }
    /**
     * 根据标准http协议，解析请求头
     *
     * @param reader
     * @param request
     * @throws IOException
     */
    private static void decodeRequestHeader(BufferedReader reader,Request request) throws IOException {
        Map<String,String> headers = new HashMap<>(16);
        String line = reader.readLine();
        String[] kv;
        while(!"".equals(line)){
            kv = StringUtils.splitString(line,":");
            assert kv.length == 2;
            headers.put(kv[0].trim(),kv[1].trim());
            line = reader.readLine();
        }
        request.setHeaders(headers);
    }
    
    private static void decodeRequestMessage(BufferedReader reader,Request request) throws IOException {
        final int contenlen = Integer.parseInt(request.getHeaders().getOrDefault("Content-Length", "0"));
        if(contenlen==0){
            // 表示没有数据，直接返回
            return;
        }
        char[] message = new char[contenlen];
        reader.read(message);
        request.setMessage(new String(message));
    }


    /**
     * http的请求可以分为三部分
     *
     * 第一行为请求行: 即 方法 + URI + 版本
     * 第二部分到一个空行为止，表示请求头
     * 空行
     * 第三部分为接下来所有的，表示发送的内容,message-body；其长度由请求头中的 Content-Length 决定
     *
     * 几个实例如下
     *
     * @param reqStream
     * @return
     */
    public static Request parse2request(InputStream reqStream) throws IOException {
        BufferedReader httpReader = new BufferedReader(new InputStreamReader(reqStream, "UTF-8"));
        Request httpRequest = new Request();
        decodeRequestLine(httpReader, httpRequest);
        decodeRequestHeader(httpReader, httpRequest);
        decodeRequestMessage(httpReader, httpRequest);
        return httpRequest;
    }
    @Data
    public static class Response {
        private String version;
        private int code;
        private String status;

        private Map<String, String> headers;

        private String message;
    }

    public static String buildResponse(Request request, String response) {
        Response httpResponse = new Response();
        httpResponse.setCode(200);
        httpResponse.setStatus("ok");
        httpResponse.setVersion(request.getVersion());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(response.getBytes().length));
        httpResponse.setHeaders(headers);

        httpResponse.setMessage(response);

        StringBuilder builder = new StringBuilder();
        buildResponseLine(httpResponse, builder);
        buildResponseHeaders(httpResponse, builder);
        buildResponseMessage(httpResponse, builder);
        return builder.toString();
    }


    private static void buildResponseLine(Response response, StringBuilder stringBuilder) {
        stringBuilder.append(response.getVersion()).append(" ").append(response.getCode()).append(" ")
                .append(response.getStatus()).append("\n");
    }

    private static void buildResponseHeaders(Response response, StringBuilder stringBuilder) {
        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        stringBuilder.append("\n");
    }

    private static void buildResponseMessage(Response response, StringBuilder stringBuilder) {
        stringBuilder.append(response.getMessage());
    }
}
