package com.ifyrz.service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * program: socket_demo
 * <p>
 * 功能描述:
 *
 * @author : yuanzhan
 * @since : 2023-02-13 20:05
 **/
public class ServiceSocketDemo {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8888);
            final Socket socket = serverSocket.accept();
             InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream();
            Scanner scanner = new Scanner(is)
        ){
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "gbk"),true);
            pw.println("欢迎来到java的世界,按exit退出 ");
            boolean done = false;
            while (!done && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                
                if ("exit".equals(line)) {
                    done = true;
                }else {
                    try {
                        // 如果输出的
                        if (Integer.parseInt(line) < 10000) {
                            pw.println("小伙洗洗睡吧");
                        }
                        if(Integer.parseInt(line) > 10000){
                            pw.println("你在想屁");
                        }
                    }catch (Exception e){
                        pw.println("不要乱搞！！！");
                    }
                }
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
