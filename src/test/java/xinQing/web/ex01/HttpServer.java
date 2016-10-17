package xinQing.web.ex01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xuan on 16-10-13.
 */
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                HttpRequest request = new HttpRequest(socket.getInputStream());
                request.parse();
                HttpResponse response = new HttpResponse(socket.getOutputStream());
                response.setRequest(request);
                response.sendStaticResource();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
