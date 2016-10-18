package xinQing.web.ex03;

import xinQing.web.ex03.connector.http.HttpRequest;
import xinQing.web.ex03.connector.http.HttpResponse;

import java.io.IOException;

public class StaticResourceProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
