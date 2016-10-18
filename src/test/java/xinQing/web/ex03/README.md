#	ex03说明

对于这个章节，主要是使用了连接器、以及解析http。我没有过多的去看如何解析`http`中的每个部分(`content-type`、`cookies`、`uri`等)，只是看如何设计、大体的脉络，学习它的设计思想。

##	执行流程

* `Bootstrap`类中`main`方法启动。位于`ex03.startup`这个包下。这个`HttpConnector`类，类似上章节中的`HttpServer`类，主要是创建`ServerSocket`，监听端口，等待`socket`的连接。

  ```java
  public static void main(String[] args) {
  	new HttpConnector().start();
  }
  ```


* `HttpConnector`类实现了`Runnable`接口。`run`方法如下:

  ```java
  public void run() {
          ServerSocket serverSocket = null;
          int port = 8080;
          try {
              serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
          } catch (IOException e) {
              e.printStackTrace();
              System.exit(1);
          }
          while (!stopped) {
              // Accept the next incoming connection from the server socket
              Socket socket = null;
              try {
                  socket = serverSocket.accept();
              } catch (Exception e) {
                  e.printStackTrace();
                  continue;
              }
              // Hand this socket off to an HttpProcessor
              // 处理连接的socket
              new HttpProcessor(this).process(socket);
          }
      }
  ```

  主要是监听`8080`端口，等待`socket`连接，然后创建一个`HttpProcessor`类，处理这个`socket`连接。

* `HttpProcessor`类，会获取`socket`的`inputstream`，解析请求头信息，然后根据`uri`判断是调用`Servlet`还是处理静态资源。`process`方法如下:

  ```java
  public void process(Socket socket) {
          SocketInputStream input = null;
          OutputStream output = null;
          try {

              //----------------------------解析 socket 中的 InputStream 的头信息 start
              input = new SocketInputStream(socket.getInputStream(), 2048);
              output = socket.getOutputStream();

              // create HttpRequest object and parse
              request = new HttpRequest(input);

              // create HttpResponse object
              response = new HttpResponse(output);
              response.setRequest(request);

              response.setHeader("Server", "Pyrmont Servlet Container");

              parseRequest(input, output);
              parseHeaders(input);
              //----------------------------解析 socket 中的 InputStream 的头信息 end

              //check if this is a request for a servlet or a static resource
              //a request for a servlet begins with "/servlet/"
              // 判断是调用servlet还是处理静态资源
              if (request.getRequestURI().startsWith("/servlet/")) {
                  ServletProcessor processor = new ServletProcessor();
                  processor.process(request, response);
              } else {
                  StaticResourceProcessor processor = new StaticResourceProcessor();
                  processor.process(request, response);
              }

              // Close the socket
              socket.close();
              // no shutdown for this application
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  ```

  其中解析http的头信息，就不做具体的分析了。

* `ServletProcessor`类是处理调用`Servlet`，主要是使用`URLClassLoader`类加载器加载这个`Serlvet`，然后通过反射创建一个实例，调用这个`Servlet`的`Service`方法。

  **其中要注意的是，调用`Service`方法传入的参数为`HttpRequestFacade`和`HttpResponseFacade`。它们分别是`HttpRequest`和`HttpResponse`的包装类，都分别实现了`HttpServletRequest`和`HttpServletResponse`接口。使用包装类的原因是，`HttpRequest`和`HttpResponse`这两个类都直接实现了`HttpServletRequest`和`HttpServletResponse`接口，里面有一些方法，这些方法不希望暴露出来，只希望暴露`HttpServletRequest`和`HttpServletResponse`接口的方法。因此，采用了装饰器。**

* `StaticResourceProcessor`类是处理静态资源，根据路径创建一个`File`对象，如果存在就使用输入流读取文件，然后写入到`HttpResponse`；否则就发送`404`到响应。



##	总结

跟第二章相比，第三章用连接器，更加的细化了`HttpServer`。添加了解析`Socket`的`Http`相关的类，我为了更好的掌握这个脉络，并没有去剖析这个具体的解析过程，有兴趣可以去探究。是连接到`ServerSocket`就完全解析这些信息呢，还是根据`Servlet`中调用的具体头信息的时候再去延迟解析？