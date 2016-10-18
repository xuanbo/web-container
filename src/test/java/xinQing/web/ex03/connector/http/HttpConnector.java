package xinQing.web.ex03.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 连接器
 * 创建一个服务器套接字用来等待前来的 HTTP 请求
 */
public class HttpConnector implements Runnable {

    boolean stopped;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }

    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stopped) {
            // Accept the next incoming connection from the server socket
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            // Hand this socket off to an HttpProcessor
            // 处理连接的socket
            new HttpProcessor(this).process(socket);
        }
    }

    /**
     * 启动
     */
    public void start() {
        new Thread(this).start();
    }
}