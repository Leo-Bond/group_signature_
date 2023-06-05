import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GJoinU {
    static byte[] id = {0,0,0,1};

    public static void main(String[] args) throws IOException {

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();


        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();
        Element sk_id = pp.pp_ps.Zr.newRandomElement();


        //生成h，a‘
        Element h = pp.pp_ps.G1.newElement().setFromHash(id, 0, id.length);
        System.out.println(h);


        Element h_sk = h.duplicate().powZn(sk_id);
        Element g_sk = pp.g1.duplicate().powZn(sk_id);
        System.out.println(g_sk);
        Element r = pp.pp_ps.Zr.newRandomElement();

        Element x = pp.g1.duplicate().powZn(r);
        System.out.println(x);
        //byte[] result = new byte[x.toBytes().length + g_sk.toBytes().length];
        //System.arraycopy(x.toBytes(), 0, result, 0, x.toBytes().length);
        //System.arraycopy(g_sk.toBytes(), 0, result, x.toBytes().length, g_sk.toBytes().length);
        String result = x.toString().concat(g_sk.toString());
        System.out.println(result);
        Element c = pp.pp_ps.Zr.newElement().setFromHash(result.getBytes(), 0, result.length());
        System.out.println(c);

        Element y = r.duplicate().add(c.duplicate().mul(sk_id));
        System.out.println(y);

        //pai = (x, y)    
        if (pp.g1.duplicate().powZn(y).isEqual(x.duplicate().mul(g_sk.duplicate().powZn(c)))){
            System.out.println("零知识验证成功");
        }

        //生成一个随机多项式P
        Element [] P = new Element[pp.t_I+1];
        for (int i = 1; i<=pp.t_I; i++) {
            P[i] = pp.pp_ps.Zr.newRandomElement();
            System.out.println(P[i]);
        }
        P[0] = sk_id;


        //s[i]是当多项式P的自变量为i时的函数值
        Element [] s = new Element[pp.n_I+1];
        for (int i = 0; i < s.length; i++) {
            s[i] = pp.pp_ps.Zr.newZeroElement();
        }
        for (int i = 1; i <= pp.n_I; i++) {
            for(int j = 0; j < pp.t_I+1; j++) {
                Element tmp = pp.pp_ps.Zr.newElement((int) Math.pow(i,j));
                //System.out.println(tmp);
                s[i] = s[i].duplicate().add(P[j].duplicate().mul(tmp));

            }
        }
        File file_reg = new File("reg.txt");
        if(!file_reg.exists()){
            file_reg.createNewFile();
        }
        //true = append file
        BufferedWriter writter_reg = new BufferedWriter(new FileWriter(file_reg));
        for (int i = 1; i <= pp.n_I; i++) {
            writter_reg.write(new String(encoder.encode(s[i].toBytes()), StandardCharsets.UTF_8) + "\n");
        }
        writter_reg.close();
        System.out.println("Done");




        //hl[]是h的多项式系数次方
        Element [] hl = new Element[pp.t_I+1];
        for (int i = 1; i<=pp.t_I; i++) {
            hl[i] = h.duplicate().powZn(P[i]);
        }

        Element [] R = new Element[pp.n_O+1];
        Element [][] C = new Element[pp.n_O+1][2];
//        for (int i = 1; i <= pp.n_O; i++) {
//            R[i] = pp.pp_ps.Zr.newRandomElement();
//            C[i][0] = pp.pp_ps.g2.duplicate().powZn(R[i]);
//            C[i][1] = ;
//        }











        File file = new File("GjoinU.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        //true = append file
        BufferedWriter writter = new BufferedWriter(new FileWriter(file));
        writter.write(new String(encoder.encode(g_sk.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.write(new String(encoder.encode(h_sk.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.close();
        System.out.println("Done");

        File file2 = new File("skid.txt");
        if(!file2.exists()){
            file2.createNewFile();
        }
        //true = append file
        BufferedWriter writter2 = new BufferedWriter(new FileWriter(file2));
        writter2.write(new String(encoder.encode(sk_id.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter2.close();
        System.out.println("Done");


    }









}


