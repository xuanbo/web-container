import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xuan on 16-10-15.
 */
public class HelloServlet implements Servlet {
    public void init(ServletConfig config) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String message = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 20\r\n" +
                "\r\n" +
                "<h1>Hello World</h1>";
        System.out.println(message);
        PrintWriter writer = res.getWriter();
        writer.append(message);
        // 不加flush，不显示内容
        writer.flush();
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
