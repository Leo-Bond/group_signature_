import it.unisa.dia.gas.jpbc.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GJoinU2 {
    public static void main(String[] args) throws IOException {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        int issuer_id1 = 1;
        int issuer_id2 = 2;
        int issuer_id3 = 3;
        int issuer_id4 = 4;
        int issuer_id5 = 5;


        FileAc file_ac1 = new FileAc("msg_from_gjoini" + issuer_id1 + ".txt");
        FileAc file_ac2 = new FileAc("msg_from_gjoini" + issuer_id2 + ".txt");
        FileAc file_ac3 = new FileAc("msg_from_gjoini" + issuer_id3 + ".txt");
        FileAc file_ac4 = new FileAc("msg_from_gjoini" + issuer_id4 + ".txt");
        FileAc file_ac5 = new FileAc("msg_from_gjoini" + issuer_id5 + ".txt");

        Element [] data_from_I = new Element[6];
        data_from_I[1] = file_ac1.runG1();
        data_from_I[2] = file_ac2.runG1();
        data_from_I[3] = file_ac3.runG1();
        data_from_I[4] = file_ac4.runG1();
        data_from_I[5] = file_ac5.runG1();

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

        Element result = pp.pp_ps.G1.newOneElement();

        for(int i = 1; i < 6; i++) {
            result = result.duplicate().mul(data_from_I[i].duplicate().powZn(Lambda[i]));

        }

        System.out.println(result);

        byte[] id = {0,0,0,1};
        Element h = pp.pp_ps.G1.newElement().setFromHash(id, 0, id.length);
        System.out.println(h);
        Element a_ = pp.pp_ps.Zr.newElement().setFromHash(id, 0, id.length);
        System.out.println(a_);

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        File file_result = new File("./gsk.txt");
        if(!file_result.exists()){
            file_result.createNewFile();
        }
        //true = append file
        BufferedWriter writter = new BufferedWriter(new FileWriter(file_result));
        writter.write(new String(encoder.encode(a_.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.write(new String(encoder.encode(h.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.write(new String(encoder.encode(result.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.close();
        System.out.println("Done");


    }





}
