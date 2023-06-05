//import it.unisa.dia.gas.jpbc.Element;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//public class FileAc {
//    public void readin(String file) {
//        Base64.Encoder encoder = Base64.getEncoder();
//        Base64.Decoder decoder = Base64.getDecoder();
//        File file = new File("GjoinU.txt");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//        //true = append file
//        BufferedWriter writter = new BufferedWriter(new FileWriter(file));
//        writter.write(new String(encoder.encode(g_sk.toBytes()), StandardCharsets.UTF_8)+"\n");
//        writter.write(new String(encoder.encode(h_sk.toBytes()), StandardCharsets.UTF_8)+"\n");
//        writter.close();
//        System.out.println("Done");
//    }
//
//    public void writeout() {
//        BufferedReader reader = null;
//        reader = new BufferedReader(new FileReader(file));
//        String tempString = null;
//        int line = 1;
//        // 一次读入一行，直到读入null为文件结束
//        while ((tempString = reader.readLine()) != null) {
//            // 显示行号
//            System.out.println("line " + line + ": " + tempString);
//            Element tmp = pp.pp_ps.G1.newElementFromBytes(decoder.decode(tempString.getBytes()));
//            System.out.println("line " + line + ": " + tmp);
//            line++;
//        }
//    }
//}
import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileAc {

    public String filePath;
    public FileAc(String filePath) {this.filePath = filePath;}
    public Element runG1() {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] x = new byte[172];

        try (FileInputStream fis = new FileInputStream(filePath)) {
            int bytesRead = fis.read(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y = decoder.decode(x);
        return pp.pp_ps.G1.newElementFromBytes(y);
    }

    public Element runG2() {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] x = new byte[172];

        try (FileInputStream fis = new FileInputStream(filePath)) {
            int bytesRead = fis.read(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y = decoder.decode(x);
        return pp.pp_ps.G2.newElementFromBytes(y);
    }

    public Element runZr() {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] x = new byte[28];

        try (FileInputStream fis = new FileInputStream(filePath)) {
            int bytesRead = fis.read(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y = decoder.decode(x);
        return pp.pp_ps.Zr.newElementFromBytes(y);
    }
}