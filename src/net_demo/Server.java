// 服务端代码
package net_demo;
import java.io.*;
import java.net.*;

public class Server {
    public String message = "";

    public ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
    public void setup(int port, String info) throws IOException, InterruptedException {
        try (ServerSocket server = getServerSocket(port)) {
            System.out.println("服务器启动，等待客户端连接...");
            while(this.message.isEmpty()){

                Socket socket = server.accept();    // 调用accept方法，阻塞等待客户端的连接请求
                System.out.println("" + socket.getInetAddress());
                ServerThread serverThread = new ServerThread(socket, info);   // 启动线程，处理客户端的请求
                serverThread.start();
                serverThread.join();
                this.message = serverThread.getMessage();
                //System.out.println(message);
            }

        }
        /*ServerSocket server = getServerSocket(port);

        System.out.println("服务器启动，等待客户端连接...");
        while(this.message.isEmpty()){

            Socket socket = server.accept();    // 调用accept方法，阻塞等待客户端的连接请求
            System.out.println("" + socket.getInetAddress());
            ServerThread serverThread = new ServerThread(socket);   // 启动线程，处理客户端的请求
            serverThread.start();
            serverThread.join();
            this.message = serverThread.getMessage();
            //System.out.println(message);
        }*/



    }
    public class ServerThread extends Thread {
        private Socket socket;
        private String message;
        private String info;
        public ServerThread(Socket socket, String info){
            this.socket = socket;
            this.info = info;

        }
        @Override
        public void run() {
            try {
                // 获取输入流，读取客户端发送的数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = reader.readLine();
               // System.out.println("收到客户端的消息：" + message);
                this.message = message;

                // 获取输出流，向客户端发送数据
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("客户端已经收到了消息");
                writer.println(info);
                writer.flush();
                // 关闭资源
                writer.close();

                reader.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String getMessage() { return message; }
    }
    public String getMessage() { return message; }


}