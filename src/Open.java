import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class Open {
    public static void main(String[] args) throws IOException{
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        Element [] s = new Element[6];
        File file = new File("reg.txt");

        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        for(int i = 1; i <= pp.n_O; i++) {
            tempString = reader.readLine();
            // 显示行号
            s[i] = pp.pp_ps.Zr.newElementFromBytes(decoder.decode(tempString.getBytes()));
            System.out.println("line " + line + ": " + s[i]);
            line++;
        }

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

        Element [] reg = new Element[6];
        for(int i = 1; i <= pp.n_O; i++) {
            reg[i] = Y.duplicate().powZn(s[i]);
        }

        File file_sig = new File("sig.txt");

        BufferedReader reader_sig = null;
        reader_sig = new BufferedReader(new FileReader(file_sig));
        String tempString_sig = null;
        int line_sig = 1;
        // 一次读入一行，直到读入null为文件结束
        tempString_sig = reader_sig.readLine();
        // 显示行号
        //System.out.println("line " + line + ": " + tempString);
        Element h_ = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString_sig.getBytes()));
        System.out.println("line " + line_sig + ": " + h_);
        line_sig++;
        tempString_sig = reader_sig.readLine();
        //System.out.println("line " + line + ": " + tempString);
        Element result_ = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString_sig.getBytes()));
        System.out.println("line " + line_sig + ": " + result_);

        Element [] T = new Element[6];
        for(int i = 1; i <= pp.n_O; i++) {
            T[i] = pp.pp_ps.pairing.pairing(h_, reg[i]);
        }

        int[] lambda = new int[6];

        for (int j = 1; j < 6; j++) {
            float tmp = 1;
            for(int i = 1; i < 6; i++) {
                if(i != j) {
                    tmp = tmp * ((float) (0 - i) / (float)(j - i));
                }
            }
            lambda[j] = (int)tmp;
        }
        System.out.println(lambda[3]);

        Element [] Lambda = new Element[6];
        for (int i = 1; i < 6; i++) {
            Lambda[i] = pp.pp_ps.Zr.newElement(lambda[i]);
        }

        byte[] id = {0,0,0,1};

        Element a_ = pp.pp_ps.Zr.newElement().setFromHash(id, 0, id.length);
        System.out.println(a_);

        Element e1 = pp.pp_ps.pairing.pairing(result_, pp.pp_ps.g2);
        System.out.println(e1);
        Element temp = pp.pp_ps.pairing.pairing(h_, Y.duplicate().mul(Y.duplicate().powZn(a_)));
        for (int i = 1; i < 6; i++) {
            temp = temp.duplicate().mul(T[i].duplicate().powZn(Lambda[i]));
        }
        System.out.println(temp);






    }

}
