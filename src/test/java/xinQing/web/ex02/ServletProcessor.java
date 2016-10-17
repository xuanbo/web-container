package xinQing.web.ex02;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by xuan on 16-10-15.
 */
public class ServletProcessor {

    public void process(Request request, Response response) throws IOException {
        String uri = request.getUri();
        // uri:/servlet/servletName
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            // the forming of repository is taken from the
            // createClassLoader method in
            // org.apache.catalina.startup.ClassLoaderFactory
            String repository = (new URL("file", null, classPath.getCanonicalPath() +
                    File.separator)).toString();
            // the code for forming the URL is taken from
            // the addRepository method in
            // org.apache.catalina.loader.StandardClassLoader.
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);

            // 由于使用了自定义的类加载器，因此要把Servlet放到跟本包不同的位置，以免运行测试时系统加载器加载了Servlet，从而引起自定义类加载器加载异常。
            Class myClass = loader.loadClass(servletName);
            Servlet servlet = (Servlet) myClass.newInstance();
            // 如果开发人员知道内部原理，就可以把request转型为Request，从而可以调用parse方法、Response的sendStaticResources方法
            // 这样会有危害性，因此，可以使用装饰器模式，包装Request和Response。
            servlet.service(request, response);
        } catch (Exception e) {
            // 系统内部错误
            String errorMessage = "HTTP/1.1 500\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 14\r\n" +
                    "\r\n" +
                    "<h1>error</h1>";
            PrintWriter writer = response.getWriter();
            writer.append(errorMessage);
            writer.flush();

            e.printStackTrace();
        }
    }
}
