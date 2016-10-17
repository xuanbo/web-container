package xinQing.web.ex02;

import java.net.URL;

/**
 * Created by xuan on 16-10-15.
 */
public class Constants {
    public static String WEB_ROOT;

    static {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        WEB_ROOT = url.getPath();
    }
}
