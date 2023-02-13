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
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8),true);
            pw.println("欢迎来到java的世界 ");
            boolean done = false;
            while (!done && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);

                if ("2048".equals(line)) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
