import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.math.BigInteger;

public class  Test {

    public static void main(String[] args) throws InterruptedException, IOException {
        int id = 1;
        IKG issuer1_setup = new IKG(1);
        issuer1_setup.setup();


        Element [] S1 = issuer1_setup.S1;
        Element [] S2 = issuer1_setup.S2;
        Element [] C = issuer1_setup.C;
        Element g = issuer1_setup.g;
        Element h = issuer1_setup.h;

        Pp pp = issuer1_setup.getPp();

        System.out.println(S1[1]);
        System.out.println(S2[1]);
        Element tmp1 = g.duplicate().powZn(S1[1]);
        Element tmp2 = h.duplicate().powZn(S2[1]);
        Element tmp = tmp1.duplicate().mul(tmp2);
        Element tmp3 = pp.pp_ps.G1.newOneElement();
        for (int k=0; k<=pp.t_I; k++){
            BigInteger x = BigInteger.valueOf ((int)Math.pow(id, k));
            System.out.println(x);
            Element tmp4 = C[k].duplicate().pow(x);
            System.out.println(tmp4);
            tmp3 = tmp3.duplicate().mul(tmp4);
            System.out.println(tmp3);
        }
        if(tmp.isEqual(tmp3)) {
            System.out.println("认证成功");

        }
        else{
            System.out.println("认证失败");
        }
        System.out.println(tmp);
        System.out.println(tmp3);

    }
}