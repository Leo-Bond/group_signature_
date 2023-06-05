import it.unisa.dia.gas.jpbc.Element;
import socketdemo.Client;
import socketdemo.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Issuer2 {

    public void run() throws InterruptedException, IOException {
        int id = 2;
        IKG issuer2_setup = new IKG(2);
        issuer2_setup.setup();
        Element[] S1 = issuer2_setup.S1;
        Element [] S2 = issuer2_setup.S2;
        Element [] C = issuer2_setup.C;
        Element g = issuer2_setup.g;
        Element h = issuer2_setup.h;
        Pp pp = issuer2_setup.getPp();
        Element A0 = issuer2_setup.A0;




        Server [] servers = new Server[6];

        //开启客户端,监听消息
        servers[1] = new Server(8012);
        servers[1].start();
        servers[3] = new Server(8032);
        servers[3].start();
        servers[4] = new Server(8042);
        servers[4].start();
        servers[5] = new Server(8052);
        servers[5].start();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        //开始向其他四个issue发送消息
        Thread.sleep(200000);

        for(int i=1; i<=5; i++) {
            if(i == id) continue;
            Client [] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("802" + i));
            clients[i].send(new String(encoder.encode(S1[i].toBytes()), StandardCharsets.UTF_8));
            System.out.println("发送给" + Integer.parseInt("802" + i) + ":  " + S1[i].toString());

        }


        Thread.sleep(50000);


/*      System.out.println(servers[1].getMessage());
        System.out.println(servers[3].getMessage());
        System.out.println(servers[4].getMessage());
        System.out.println(servers[5].getMessage());*/

        //j = 2时，s_ij,x=2时各个issuer生成的多项式的值
        Element [] S1_2 = new Element[6];
        S1_2[2]  = S1[2];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S1_2[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S"+i+"_"+id);
            System.out.println(S1_2[i]);
        }

        Thread.sleep(50000);

        for(int i=1; i<=5; i++) {
            if(i == id) continue;
            Client [] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("802" + i));
            clients[i].send(new String(encoder.encode(S2[i].toBytes()), StandardCharsets.UTF_8));//发送消息
            System.out.println("发送给" + Integer.parseInt("802" + i) + ":  " + S2[i].toString());

        }

        Thread.sleep(50000);

        Element [] S2_2 = new Element[6];
        S2_2[2]  = S2[2];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S2_2[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S'"+i+"_"+id);
            System.out.println(S2_2[i]);
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
                clients[i] = new Client("localhost", Integer.parseInt("802" + i));
                clients[i].send(new String(encoder.encode(C[j].toBytes()), StandardCharsets.UTF_8));//发送消息
                System.out.println("发送给" + Integer.parseInt("802" + i) + "C:  " + C[j].toString());

            }
            Thread.sleep(50000);


            for (int i=1; i<=5; i++) {
                if(i == id)continue;
                Ck[i][j] = pp.pp_ps.G2.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
            }
            Thread.sleep(50000);

        }

/*        System.out.println(S1[2]);
        System.out.println(S2[2]);


        Server issuer2_server = new Server();
        issuer2_server.setup(8012,"abc");
        String message = issuer2_server.getMessage();
        issuer2_server.message = "";
        System.out.println(message);

        Thread.sleep(2000);
        issuer2_server.setup(8012,"def");
        message = issuer2_server.getMessage();
        issuer2_server.message = "";
        System.out.println(message);*/

        //生成私钥xi
        Element x = pp.pp_ps.Zr.newZeroElement();
        for(int i=1; i<=5; i++) {
            x = x.duplicate().add(S1_2[i]);
        }
        byte[] x_in = encoder.encode(x.toBytes());
        System.out.println(x_in);
        System.out.println(x_in.length);
        String filePath = "./private_key_2.bin";
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
        String filePath2 = "./public_key_2.bin";
        try (FileOutputStream fos = new FileOutputStream(filePath2)) {
            fos.write(x_in2);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }



        


    }


}