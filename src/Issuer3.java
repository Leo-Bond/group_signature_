import it.unisa.dia.gas.jpbc.Element;
import socketdemo.Client;
import socketdemo.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Issuer3 {

    public void run() throws InterruptedException, IOException {
        int id = 3;
        IKG issuer3_setup = new IKG(2);
        issuer3_setup.setup();
        Element[] S1 = issuer3_setup.S1;
        Element[] S2 = issuer3_setup.S2;
        Element[] C = issuer3_setup.C;
        Element g = issuer3_setup.g;
        Element h = issuer3_setup.h;
        Pp pp = issuer3_setup.getPp();
        Element A0 = issuer3_setup.A0;


        Server[] servers = new Server[6];

        //开启客户端,监听消息
        servers[2] = new Server(8023);
        servers[2].start();
        servers[1] = new Server(8013);
        servers[1].start();
        servers[4] = new Server(8043);
        servers[4].start();
        servers[5] = new Server(8053);
        servers[5].start();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        //开始向其他四个issue发送消息
        Thread.sleep(200000);

        for(int i=1; i<=5; i++) {
            if(i == id) continue;
            Client[] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("803" + i));
            clients[i].send(new String(encoder.encode(S1[i].toBytes()), StandardCharsets.UTF_8));
            System.out.println("发送给" + Integer.parseInt("803" + i) + ":  " + S1[i].toString());

        }

        Thread.sleep(50000);


        //j = 3时，s_ij,x=3时各个issuer生成的多项式的值
        Element [] S1_3 = new Element[6];
        S1_3[3]  = S1[3];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S1_3[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S"+i+"_"+id);
            System.out.println(S1_3[i]);
        }

        Thread.sleep(50000);

        for(int i=1; i<=5; i++) {
            if(i == id) continue;
            Client[] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("803" + i));
            clients[i].send(new String(encoder.encode(S2[i].toBytes()), StandardCharsets.UTF_8));//发送消息
            System.out.println("发送给" + Integer.parseInt("803" + i) + ":  " + S2[i].toString());

        }

        Thread.sleep(50000);

        Element [] S2_3 = new Element[6];
        S2_3[3]  = S2[3];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S2_3[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S'"+i+"_"+id);
            System.out.println(S2_3[i]);
        }

        Thread.sleep(50000);

        Element [][] Ck = new Element[pp.n_I+1][pp.t_I+1];
        for (int i=0; i<= pp.t_I;i++) {
            Ck[id][i] = C[i];
        }

        for (int j=0; j<=pp.t_I; j++) {
            for(int i=1; i<=5; i++) {
                if(i == id) continue;

                Client[] clients = new Client[6];
                clients[i] = new Client("localhost", Integer.parseInt("803" + i));
                clients[i].send(new String(encoder.encode(C[j].toBytes()), StandardCharsets.UTF_8));//发送消息
                System.out.println("发送给" + Integer.parseInt("803" + i) + "C:  " + C[j].toString());

            }
            Thread.sleep(50000);


            for (int i=1; i<=5; i++) {
                if(i == id)continue;
                Ck[i][j] = pp.pp_ps.G2.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
            }
            Thread.sleep(50000);

        }

        //生成私钥xi
        Element x = pp.pp_ps.Zr.newZeroElement();
        for(int i=1; i<=5; i++) {
            x = x.duplicate().add(S1_3[i]);
        }
        byte[] x_in = encoder.encode(x.toBytes());
        System.out.println(x_in);
        System.out.println(x_in.length);
        String filePath = "./private_key_3.bin";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(x_in);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //生成公钥yi
        byte[] x_in2 = encoder.encode(A0.toBytes());
        System.out.println(x_in2);
        System.out.println(x_in2.length);
        String filePath2 = "./public_key_3.bin";
        try (FileOutputStream fos = new FileOutputStream(filePath2)) {
            fos.write(x_in2);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}