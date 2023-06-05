import it.unisa.dia.gas.jpbc.*;


public class OKG {

    Element zi;
    Element fi;


    public void setup() {
        GSetup gsetup = new GSetup();
        gsetup.goGSetup();

        Pp pp = gsetup.getPp();

        Element zi = pp.pp_ps.Zr.newRandomElement();
        Element fi = pp.pp_ps.g2.duplicate().powZn(zi);
        this.zi = zi;
        this.fi = fi;
    }

    public Element getZi() { return zi; }
    public Element getFi() { return fi; }


}
