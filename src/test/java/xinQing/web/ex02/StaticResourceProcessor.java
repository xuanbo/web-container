package xinQing.web.ex02;

import java.io.IOException;

/**
 * Created by xuan on 16-10-15.
 */
public class StaticResourceProcessor {

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
