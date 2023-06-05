import it.unisa.dia.gas.jpbc.Element;
import socketdemo.Client;
import socketdemo.Server;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Issuer1 {
    Element [] S;
    int verify;

    Element x;

    Element y;

    public void run() throws InterruptedException, IOException {
        int id = 1;
        IKG issuer1_setup = new IKG(1);
        issuer1_setup.setup();

        //S1是对函数f(x)当x等于issuerid时的函数值
        //S2是对函数f2(x)当x等于issuerid时的函数值
        Element [] S1 = issuer1_setup.S1;
        Element [] S2 = issuer1_setup.S2;
        Element [] C = issuer1_setup.C;
        Element g = issuer1_setup.g;
        Element h = issuer1_setup.h;
        Element A0 = issuer1_setup.A0;

        Pp pp = issuer1_setup.getPp();

        //System.out.println(S1[2]);
        //System.out.println(S2[2]);


//定义四个服务端端口，用于接收来自另外四个issuer的信息
        Server [] servers = new Server[6];

        servers[2] = new Server(8021);
        servers[2].start();
        servers[3] = new Server(8031);
        servers[3].start();
        servers[4] = new Server(8041);
        servers[4].start();
        servers[5] = new Server(8051);
        servers[5].start();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        //开始向其他四个issue发送消息
        Thread.sleep(200000);

        //发送S[j]给第j个issuer
        for(int i=1; i<=5; i++) {
            if(i == id) continue;
            Client [] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("801" + i));//创建客户端连接
            clients[i].send(new String(encoder.encode(S1[i].toBytes()), StandardCharsets.UTF_8));//发送消息
            System.out.println("发送给" + Integer.parseInt("801" + i) + ":  " + S1[i].toString());

        }

        Thread.sleep(50000);

        //j = 1时，s_ij，x=1时各个issuer生成的多项式的值
        Element [] S1_1 = new Element[6];
        S1_1[1]  = S1[1];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S1_1[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S"+i+"_"+id);
            System.out.println(S1_1[i]);
        }

        Thread.sleep(50000);


        for(int i=1; i<=5; i++) {
            if(i == id) continue;

            Client[] clients = new Client[6];
            clients[i] = new Client("localhost", Integer.parseInt("801" + i));
            clients[i].send(new String(encoder.encode(S2[i].toBytes()), StandardCharsets.UTF_8));//发送消息
            System.out.println("发送给" + Integer.parseInt("801" + i) + ":  " + S2[i].toString());

        }

        Thread.sleep(50000);

        Element [] S2_1 = new Element[6];
        S2_1[1]  = S2[1];
        for (int i=1; i<=5; i++) {
            if(i == id)continue;
            S2_1[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
        }
        for(int i=1; i<=5; i++) {
            System.out.println("S'"+i+"_"+id);
            System.out.println(S2_1[i]);
        }


/*        System.out.println(servers[2].getMessage());
        System.out.println(servers[3].getMessage());
        System.out.println(servers[4].getMessage());
        System.out.println(servers[5].getMessage());*/
        Thread.sleep(50000);

        Element [][] Ck = new Element[pp.n_I+1][pp.t_I+1];
        for (int i=0; i<= pp.t_I;i++) {
            Ck[id][i] = C[i];
        }

        for (int j=0; j<=pp.t_I; j++) {
            for(int i=1; i<=5; i++) {
                if(i == id) continue;

                Client[] clients = new Client[6];
                clients[i] = new Client("localhost", Integer.parseInt("801" + i));
                clients[i].send(new String(encoder.encode(C[j].toBytes()), StandardCharsets.UTF_8));//发送消息
                System.out.println("发送给" + Integer.parseInt("801" + i) + "C:  " + C[j].toString());

            }
            Thread.sleep(50000);


            for (int i=1; i<=5; i++) {      
                if(i == id)continue;
                Ck[i][j] = pp.pp_ps.G2.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));

            }

            Thread.sleep(50000);
        }

        for(int i=1; i<=pp.n_I; i++) {
            Element tmp1 = g.duplicate().powZn(S1_1[i]);
            Element tmp2 = h.duplicate().powZn(S2_1[i]);
            Element tmp = tmp1.duplicate().mul(tmp2);
            Element tmp3 = pp.pp_ps.G2.newOneElement();
            for (int k=0; k<=pp.t_I; k++){
                BigInteger x = BigInteger.valueOf ((int)Math.pow(id, k));
                Element tmp5 = pp.pp_ps.Zr.newElement(x);
                Element tmp4 = Ck[i][k].duplicate().powZn(tmp5);
                tmp3 = tmp3.duplicate().mul(tmp4);
            }
            if(tmp.isEqual(tmp3)) {
                System.out.println("认证成功");
                //this.verify = 1;

            }
            else{
                System.out.println("认证失败");
                    //this.verify = 0;
            }
            System.out.println(tmp);
            System.out.println(tmp3);

        }

//        Element A[] = new Element[6];



//        for (int i=1; i<=5; i++) {
//            if(i == id)continue;
//            A[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(servers[i].getMessage().getBytes()));
//        }
//
//        Element Y = pp.pp_ps.G1.newOneElement();
//        for(int i=1; i<=5; i++) {
//            Y = Y.duplicate().mul(A[i]);
//        }

        //生成私钥xi
        Element x = pp.pp_ps.Zr.newZeroElement();
        for(int i=1; i<=5; i++) {
            x = x.duplicate().add(S1_1[i]);
        }
        byte[] x_in = encoder.encode(x.toBytes());
        System.out.println(x_in);
        System.out.println(x_in.length);
        String filePath = "./private_key_1.bin";
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
        String filePath2 = "./public_key_1.bin";
        try (FileOutputStream fos = new FileOutputStream(filePath2)) {
            fos.write(x_in2);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }






       // for(int i=1; i<=5; i++) {
        //    this.S[i] = S1_1[i];
       // }






        //广播C
        /*for(int i=2; i<=5; i++) {
            for(int j=0; j<C.length; j++) {
                Thread.sleep(2000);
                issuer1_client.setup(Integer.parseInt("801" + i), C[j].toString());
                if (issuer1_client.getInfo() != "") {
                    break;
                }
            }
        }*/



       /* //   将$s_{ij},s'_{ij}发送给第j个issuer$

        for(int i=2; i<=2; i++) {

            while (true) {
                String info;
                Thread.sleep(2000);
                issuer1_client.setup(Integer.parseInt("801" + i), S1[i].toString());
                if ((info = issuer1_client.getInfo()) != "") {
                    System.out.println(info);
                    break;
                }

            }

            while (true) {
                String info;
                Thread.sleep(2000);
                issuer1_client.setup(Integer.parseInt("801" + i), S2[i].toString());
                if ((info = issuer1_client.getInfo()) != "") {
                    System.out.println(info);
                    break;
                }
            }
        }
*/








    }

    public Element[] getS() { return S; }

    public Element getY() { return y; }

    public Element getX() { return x; }



}