import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrElement;
//import net_demo.Client;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Base64;


public class IKG {

    public static Element g;
    public static Element h;
    int id;
    Element [] S1;
    Element [] S2;

    Element [] C;

    Element A0;

    Pp pp;

    public IKG (int id) {
        this.id = id;
    }
    public void setup() throws InterruptedException {

        GSetup gsetup = new GSetup();
        gsetup.goGSetup();
        Pp pp = gsetup.getPp();
        this.pp = pp;
        //System.out.println(pp.pp_ps.Zr.getOrder());

//        // 创建多项式模
//        PolyModFactory polyModFactory = new PolyModFactory(Zq, q, PolyMod.ModPType.BASIC);
//        PolyMod polyMod = polyModFactory.createPolyMod(degree);
//
//        // 生成随机多项式
//        Poly poly = polyMod.createRandomElement(true);

/*        System.out.println(pp.pp_ps.G1.getOrder());
        System.out.println(pp.pp_ps.G2.getOrder());
        System.out.println(pp.pp_ps.GT.getOrder());
        System.out.println(pp.pp_ps.Zr.getOrder());

        Element z1 = pp.pp_ps.Zr.newRandomElement();
        System.out.println(z1);
        System.out.println(z1.getClass().getTypeName());*/

        //Issuer生成两个随机多项式,f1[].f2[]存放各自系数
        Element [] f1 = new Element[pp.t_I+1];
        for (int i = 0; i<=pp.t_I; i++) {
            f1[i] = pp.pp_ps.Zr.newRandomElement();
            //System.out.println(f1[i].getClass().getTypeName());
            //System.out.println("f"+i+ ":"+f1[i]);
        }
        Element [] f2 = new Element[pp.t_I+1];
        for (int i = 0; i<=pp.t_I; i++) {
            f2[i] = pp.pp_ps.Zr.newRandomElement();
            //System.out.println("f'"+i+ ":"+f2[i]);
            //System.out.println(f2[i].getClass().getTypeName());
            //System.out.println(f2[i]);
        }
//

        Element zi = f1[0];

        Element[] C = new Element[pp.t_I+1];
        Element g = pp.pp_ps.g2;
        //byte[] elementBytes = new byte[] {16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        //Element h = pp.pp_ps.G1.newElement();

        Element A0 = g.duplicate().powZn(zi);

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

//        Element h = pp.pp_ps.G1.newRandomElement();
//        byte[] x = encoder.encode(h.toBytes());
//        System.out.println(x);
//        System.out.println(x.length);
//        String filePath = "./parms_h.bin";
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(x);
//            System.out.println("Successfully written to file.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("h:"+h);

        String filePath3 = "./parms_h.bin";
        byte[] x3 = new byte[172];

        try (FileInputStream fis = new FileInputStream(filePath3)) {
            int bytesRead = fis.read(x3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] y3 = decoder.decode(x3);
        Element h = pp.pp_ps.G2.newElementFromBytes(y3);


        for (int i = 0; i<=pp.t_I; i++) {
            Element tmp1 = g.duplicate().powZn(f1[i]);
            System.out.println(tmp1);
            Element tmp2 = h.duplicate().powZn(f2[i]);
            System.out.println(tmp2);

            C[i] =  tmp1.duplicate().mul(tmp2);
            System.out.println("C"+i+ ":"+C[i]);
        }

        this.C = C;
        this.g = g;
        this.h = h;
        this.A0 = A0;

        //S_ij
        Element [] S1 = new Element[pp.n_I+1];
        for (int i = 0; i < S1.length; i++) {
            S1[i] = pp.pp_ps.Zr.newZeroElement();
        }
        for (int i = 1; i < pp.n_I+1; i++) {
            for(int j = 0; j < pp.t_I+1; j++) {
                Element tmp = pp.pp_ps.Zr.newElement((int) Math.pow(i,j));
                //System.out.println(tmp);
                S1[i] = S1[i].duplicate().add(f1[j].duplicate().mul(tmp));

            }
         }
        //System.out.println("s1");
        //System.out.println(S1[1]);


        Element [] S2 = new Element[pp.n_I+1];
        for (int i = 0; i < S2.length; i++) {
            S2[i] = pp.pp_ps.Zr.newZeroElement();
        }
        for (int i = 1; i < pp.n_I+1; i++) {
            for(int j = 0; j < pp.t_I+1; j++) {
                Element tmp = pp.pp_ps.Zr.newElement((int) Math.pow(i,j));
                //System.out.println(tmp);
                S2[i] = S2[i].duplicate().add(f2[j].duplicate().mul(tmp));
                // System.out.println(S2[i]);
            }
        }
       // System.out.println("s2");
        //System.out.println(S2[1]);

        this.S1 = S1;
        this.S2 = S2;



        /*Element a = f1[0].duplicate().add(f1[1]);
        System.out.println(f1[0]);
        System.out.println(f1[1]);
        System.out.println(a);*/

        /*for (int i = 0; i<=pp.t_I; i++) {

        }*/
       /* Client issuer1 = new Client();
        while(true){
            Thread.sleep(2000);
            issuer1.setup(7777, pp.g1.toString());
        }*/






    }

    public Element [] getS1() { return S1; }
    public Element [] getS2() { return S2; }

    public Element [] getC() { return C; }
    public Pp getPp() { return pp; }
    public Element getA0() { return A0; }
}
