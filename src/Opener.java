import it.unisa.dia.gas.jpbc.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Opener {
    public int id;
    public Opener(int id) {
        this.id = id;
    }

    public void run() {
        OKG okg1 = new OKG();
        okg1.setup();
        Element zi = okg1.getZi();
        Element fi = okg1.getFi();
        Element reg;

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        byte[] x_in = encoder.encode(zi.toBytes());
        System.out.println(x_in);
        System.out.println(x_in.length);
        String filePath = "./okg_private_key_"+id+".bin";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(x_in);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }


        byte[] x_in2 = encoder.encode(fi.toBytes());
        System.out.println(x_in2);
        System.out.println(x_in2.length);
        String filePath2 = "./okg_public_key_"+id+".bin";
        try (FileOutputStream fos = new FileOutputStream(filePath2)) {
            fos.write(x_in2);
            System.out.println("Successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
