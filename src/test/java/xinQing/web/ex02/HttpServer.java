package xinQing.web.ex02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xuan on 16-10-15.
 */
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Request request = new Request(socket.getInputStream());
                request.parse();
                Response response = new Response(socket.getOutputStream());
                response.setRequest(request);
                // 判断是静态资源还是Servlet
                if (request.getUri().startsWith("/servlet/")) {
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request, response);
                } else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
