package socketdemo;

import java.io.*;
import java.net.*;
// 继承 Thread， 作为独立线程
public class Server extends Thread {
    private ServerSocket serverSocket;
    private String message="";

    public Server(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            System.out.println("Server:     Server Listening...");
            while (true) {
                Socket socket = serverSocket.accept();
                // 接收到 Socket 后，独立出线程接受处理
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
                socketThread.join();
                this.message = socketThread.getMessage();
                //System.out.println(this.message);


            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage() { return message; }
}


