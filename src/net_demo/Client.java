package net_demo;

import java.io.*;
import java.net.*;

public class Client {

    public String info = "";
    public void setup(int port, String str){
        try {
            // 先建立连接，发送一段数据给端口
            Socket socket = new Socket("localhost",port);    // 与服务器建立连接
            OutputStream os = socket.getOutputStream(); // 向服务器发送数据
            PrintWriter pw = new PrintWriter(os);
            pw.write(str);
            pw.flush(); // 刷新缓冲
            socket.shutdownOutput();    // 关闭输出流

            InputStream is = socket.getInputStream();   // 获取输入流
            InputStreamReader isr = new InputStreamReader(is);  // 将字节流转换为字符流
            BufferedReader br = new BufferedReader(isr);    // 为输入流添加缓冲
            String info = null;
            while((info = br.readLine()) != null){  // 循环读取服务器端的数据
                this.info = info;
            }

            // 关闭资源
            br.close();
            isr.close();
            is.close();

            pw.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("客户端异常：" + e.getMessage());
        }
    }
    public String getInfo() { return info; }

}
