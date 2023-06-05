package socketdemo;

import java.io.*;
import java.net.*;
public class SocketThread extends Thread{
    private Socket socket;
    private String message;
    public SocketThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String temp = null;
            String info="";

            while((temp = reader.readLine()) != null){
                info+=temp;
                System.out.println("Server:     Message:" + temp + "， ip adress:" + socket.getInetAddress().getHostAddress());

            }
            this.message = info;
            //writer.print("receive the message");
            //writer.flush();
            socket.shutdownOutput();
            //writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessage() { return this.message; }
}

