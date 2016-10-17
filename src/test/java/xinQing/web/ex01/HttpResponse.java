package xinQing.web.ex01;

import java.io.*;
import java.net.URL;

/**
 * Created by xuan on 16-10-13.
 */
public class HttpResponse {

    private static final String head = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 23\r\n" +
            "\r\n";

    private OutputStream outputStream;

    private HttpRequest request;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    /**
     * 发送静态资源
     */
    public void sendStaticResource() throws IOException {
        URL fileURL = getClass().getClassLoader().getResource(request.getUri());
        if (fileURL == null) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            outputStream.write(errorMessage.getBytes());
            return;
        }
        File file = new File(fileURL.getPath());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[1024];
        int len = 0;
        String head = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + file.length() + "\r\n" +
                "\r\n";
        StringBuilder stringBuilder = new StringBuilder(head);
        while ((len = bufferedInputStream.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, len));
        }
        System.out.println(stringBuilder.toString());
        outputStream.write(stringBuilder.toString().getBytes());
        outputStream.flush();
        bufferedInputStream.close();
    }
}
