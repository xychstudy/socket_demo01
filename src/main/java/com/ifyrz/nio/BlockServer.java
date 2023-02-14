package com.ifyrz.nio;

import com.ifyrz.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * program: socket_demo
 * <p>
 * 功能描述:服务端
 *
 * @author : yuanzhan
 * @since : 2023-02-14 22:54
 **/
public class BlockServer {
    public static void main(String[] args) throws IOException {
        // 获取通道
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();

        final FileChannel fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //绑定端口号
        socketChannel.bind(new InetSocketAddress(8989));
        // 获取客户端的连接 (阻塞的 ，同时只能接收一个客户端的连接)
        final SocketChannel client = socketChannel.accept();
        // 5. 要使用NIO，有了Channel，就必然要有Buffer，Buffer是与数据打交道的呢         
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 6.将客户端传递过来的图片保存在本地中         
        while (client.read(buffer) != -1) {

            // 在读之前都要切换成读模式             
            buffer.flip();
            ByteBufferUtil.debugAll(buffer);
            fileChannel.write(buffer);

            // 读完切换成写模式，能让管道继续读取文件的数据             
            buffer.clear();

        }
        fileChannel.close();
        client.close();
        socketChannel.close();
        
        
    }
}
