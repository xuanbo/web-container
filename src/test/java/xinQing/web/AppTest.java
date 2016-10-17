package xinQing.web;

import org.junit.Test;
import xinQing.web.ex02.HttpServer;

/**
 * Created by xuan on 16-10-15.
 */
public class AppTest {

    @Test
    public void start() {
        HttpServer server = new HttpServer(8080);
        server.start();
    }
}
