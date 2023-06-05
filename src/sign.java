import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class sign {
    public static void main(String[] args) throws IOException {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        FileAc file_ac = new FileAc("skid.txt");
        Element sk_id = file_ac.runZr();
        System.out.println(sk_id);

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        File file = new File("gsk.txt");

        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        tempString = reader.readLine();
        // 显示行号
        //System.out.println("line " + line + ": " + tempString);
        Element a_ = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(tempString.getBytes()));
        System.out.println("line " + line + ": " + a_);
        line++;
        tempString = reader.readLine();
        //System.out.println("line " + line + ": " + tempString);
        Element h = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString.getBytes()));
        System.out.println("line " + line + ": " + h);
        line++;
        tempString = reader.readLine();
        //System.out.println("line " + line + ": " + tempString);
        Element result = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString.getBytes()));
        System.out.println("line " + line + ": " + result);

        Element r = pp.pp_ps.Zr.newRandomElement();
        Element h_ = h.duplicate().powZn(r);
        Element result_ = result.duplicate().powZn(r);

        Element r_ = pp.pp_ps.Zr.newRandomElement();
        Element h__ = h_.duplicate().powZn(r_);

        File file_sig = new File("sig.txt");
        if(!file_sig.exists()){
            file_sig.createNewFile();
        }
        //true = append file
        BufferedWriter writter_sig = new BufferedWriter(new FileWriter(file_sig));
        writter_sig.write(new String(encoder.encode(h_.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter_sig.write(new String(encoder.encode(result_.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter_sig.close();
        System.out.println("Done");


        FileAc file_ac1 = new FileAc("public_key_1.bin");
        FileAc file_ac2 = new FileAc("public_key_2.bin");
        FileAc file_ac3 = new FileAc("public_key_3.bin");
        FileAc file_ac4 = new FileAc("public_key_4.bin");
        FileAc file_ac5 = new FileAc("public_key_5.bin");

        Element [] y = new Element[6];
        y[1] = file_ac1.runG1();
        y[2] = file_ac2.runG1();
        y[3] = file_ac3.runG1();
        y[4] = file_ac4.runG1();
        y[5] = file_ac5.runG1();

        Element Y = pp.pp_ps.G1.newOneElement();
        for(int i = 1; i < 6; i++) {
            Y = Y.duplicate().mul(y[i]);
        }

        Element e1 = pp.pp_ps.pairing.pairing(h__, Y);
        Element e2 = pp.pp_ps.pairing.pairing(h__, Y);
        Element e = e1.duplicate().mul(e2);
        System.out.println(e);
        Element c = pp.pp_ps.Zr.newElementFromHash(e.toBytes(), 0, e.toBytes().length);
        System.out.println(c);
        Element v_sk = r_.duplicate().sub(c.duplicate().mul(sk_id));
        Element v_a_ = r_.duplicate().sub(c.duplicate().mul(a_));

        Element zero = pp.pp_ps.Zr.newZeroElement();

        Element E1 = pp.pp_ps.pairing.pairing(h_.duplicate().powZn(v_sk), Y);
        Element E2 = pp.pp_ps.pairing.pairing(h_.duplicate().powZn(v_a_), Y);
        Element E3 = pp.pp_ps.pairing.pairing(result_.duplicate().powZn(c), pp.pp_ps.g2);
        Element E4 = pp.pp_ps.pairing.pairing(h_.duplicate().powZn(zero.duplicate().sub(c)), Y);

        Element R = E1.duplicate().mul(E2.duplicate().mul(E3.duplicate().mul(E4)));
        System.out.println(R);

        Element [] t = new Element[10];
        Element [] tag = new Element[10];
        Element [] tag_ = new Element[10];

        for (int i = 0; i < 10; i++) {
            t[i] = pp.pp_ps.G1.newRandomElement();
            Element tmp = pp.pp_ps.Zr.newRandomElement();
            tag[i] = t[i].duplicate().powZn(tmp);
        }

        for(int i = 0; i < 10; i++) {
            tag_[i] = t[i].duplicate().powZn(sk_id);
            System.out.println("tag'"+i+": "+tag_[i]);
        }

        int flag = 0;
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if(tag_[i].isEqual(tag[j]))
                    flag = 1;
            }

        }
        if(flag == 0)
            System.out.println("该用户不在屏蔽列表中");
        else
            System.out.println("该用户在屏蔽列表中");
















    }

}
