package socketdemo;

import java.io.*;
import java.net.*;
public class Client {
    private Socket socket;

    public Client(){
        try{
            this.socket = new Socket("localhost", 8080);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Client(String host, int port){
        try{
            this.socket = new Socket(host, port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String info){
        try{
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print(info);
            writer.flush();

            socket.shutdownOutput();

            //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

/*            while ((temp = reader.readLine()) != null){
                message += temp;
                System.out.println("Client:     Get message from server: " + message);
            }*/

            writer.close();
            //reader.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
