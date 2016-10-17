package xinQing.web.ex01;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuan on 16-10-13.
 */
public class HttpRequest {

    private InputStream inputStream;

    private String uri;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getUri() {
        return uri;
    }

    public void parse() {
        byte[] bytes = new byte[2048];
        try {
            int len = inputStream.read(bytes);
            String requestString = new String(bytes, 0, len);
            System.out.println(requestString);
            uri = parseUri(requestString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从请求中获取uri
     * 例如：
     * GET /index.jsp HTTP/1.1
     *
     * @param requestString
     * @return index.jsp
     */
    public String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 2, index2);
        }
        return null;
    }

}
