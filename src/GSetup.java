import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
class Pp {
    Pp_ps pp_ps;
    Element g1;
    int n_I;
    int n_O;
    int t_I;
    int t_O;
}
class Pp_ps {
    Pairing pairing;
    Field G1, G2, GT, Zr;
    Element g2;
}

public class GSetup {

    private Pp pp;
    public void goGSetup() {
        Pairing pairing;
        Field G1, G2, GT, Zr;

        pairing = PairingFactory.getPairing("a.properties");//利用参数文件生成配对群

        // 初始化群元素
        G1 = pairing.getG1();
        G2 = pairing.getG2();
        GT = pairing.getGT();
        Zr = pairing.getZr();

        // 生成随机元素


        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        // 创建G1群的元素
//        Element g1 = G1.newRandomElement();
//        byte[] x = encoder.encode(g1.toBytes());
//        System.out.println(x);
//        System.out.println(x.length);
//        String filePath = "./parms.bin";
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(x);
//            System.out.println("Successfully written to file.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String filePath1 = "./parms_g1.bin";
        byte[] x1 = new byte[172];

        try (FileInputStream fis = new FileInputStream(filePath1)) {
            int bytesRead = fis.read(x1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y1 = decoder.decode(x1);
        Element g1 = G1.newElementFromBytes(y1);

//        Element g2 = G2.newRandomElement();
//        byte[] x2 = encoder.encode(g2.toBytes());
//        System.out.println(x2);
//        System.out.println(x2.length);
//        String filePath2 = "./parms_g2.bin";
//        try (FileOutputStream fos = new FileOutputStream(filePath2)) {
//            fos.write(x2);
//            System.out.println("Successfully written to file.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String filePath2 = "./parms_g2.bin";
        byte[] x2 = new byte[172];

        try (FileInputStream fis = new FileInputStream(filePath2)) {
            int bytesRead = fis.read(x2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y2 = decoder.decode(x2);
        Element g2 = G1.newElementFromBytes(y2);


        //System.out.println("g1"+":"+g1);
        //System.out.println("g2"+":"+g2);
        // 计算双线性配对
        Pp_ps pp_ps = new Pp_ps();
        pp_ps.G1 = G1;
        pp_ps.G2 = G2;
        pp_ps.Zr = Zr;
        pp_ps.GT = GT;
        pp_ps.g2 = g2;
        pp_ps.pairing = pairing;
        int n_I = 5;
        int n_O = 5;
        int t_I = 3;
        int t_O = 3;

        this.pp = new Pp();
        this.pp.pp_ps = pp_ps;
        this.pp.g1 = g1;
        this.pp.n_I = n_I;
        this.pp.n_O = n_O;
        this.pp.t_I = t_I;
        this.pp.t_O = t_O;

    }

    public Pp getPp(){
        return pp;
    }

}
