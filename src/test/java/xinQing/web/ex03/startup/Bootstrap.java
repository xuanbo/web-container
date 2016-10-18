package xinQing.web.ex03.startup;

import xinQing.web.ex03.connector.http.HttpConnector;

/**
 * 启动容器
 *
 * Created by xuan on 16-10-17.
 */
public class Bootstrap {

    public static void main(String[] args) {
        new HttpConnector().start();
    }

}
