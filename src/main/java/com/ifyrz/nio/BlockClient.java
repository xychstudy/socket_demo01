package com.ifyrz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * program: socket_demo
 * <p>
 * 功能描述:
 *
 * @author : yuanzhan
 * @since : 2023-02-14 21:44
 **/
public class BlockClient {
    public static void main(String[] args) throws IOException {
        // 获取通道
        final SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8989));
        // 发送一张图片给服务端
        final FileChannel fileChannel = FileChannel.open(Paths.get("C:\\Users\\YUAN\\Pictures\\Saved Pictures\\1.jpg"), StandardOpenOption.READ);
        // 创建一个缓冲区
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        // fileChannel.read(buffer) 向buffer中写入数据
        while (fileChannel.read(buffer)!=-1){
            // 切换读模式
            buffer.flip();
            // 写数据
            socketChannel.write(buffer);
            // 切换写模式
            buffer.clear();
        }
        socketChannel.close();
        fileChannel.close();
    }
}
