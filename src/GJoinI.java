import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import it.unisa.dia.gas.jpbc.Polynomial;

public class GJoinI {
    public byte[] id;

    public int issuer_id;

    public GJoinI(byte[] id, int issuer_id) {
        this.id = id;
        this.issuer_id = issuer_id;
    }


    public void run() throws IOException {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        //传入g_sk,h_sk
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        File file = new File("GjoinU.txt");

        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        tempString = reader.readLine();
            // 显示行号
        //System.out.println("line " + line + ": " + tempString);
        Element g_sk = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString.getBytes()));
        System.out.println("line " + line + ": " + g_sk);
        line++;
        tempString = reader.readLine();
        //System.out.println("line " + line + ": " + tempString);
        Element h_sk = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString.getBytes()));
        System.out.println("line " + line + ": " + h_sk);

        Element h = pp.pp_ps.G1.newElement().setFromHash(id, 0, id.length);
        System.out.println(h);
        Element a_ = pp.pp_ps.Zr.newElement().setFromHash(id, 0, id.length);
        System.out.println(a_);
        FileAc file_ac = new FileAc("private_key_"+issuer_id+".bin" );
        Element x = file_ac.runZr();
        Element y1 = file_ac.runZr();
        Element y0 = file_ac.runZr();

        Element result = h.duplicate().powZn(x.duplicate().add(y1.duplicate().mul(a_))).mul(h_sk.duplicate().powZn(y0));

        File file_result = new File("./msg_from_gjoini"+issuer_id+".txt");
        if(!file_result.exists()){
            file_result.createNewFile();
        }
        //true = append file
        BufferedWriter writter = new BufferedWriter(new FileWriter(file_result));
        writter.write(new String(encoder.encode(result.toBytes()), StandardCharsets.UTF_8)+"\n");
        writter.close();
        System.out.println("Done");


    }

}
