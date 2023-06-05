import java.io.IOException;

public class GJoinI2 {
    public static void main(String[] args) throws IOException {
        byte[] id = {0,0,0,1};
        GJoinI issuer1 = new GJoinI(id, 1);
        GJoinI issuer2 = new GJoinI(id, 2);
        GJoinI issuer3 = new GJoinI(id, 3);
        GJoinI issuer4 = new GJoinI(id, 4);
        GJoinI issuer5 = new GJoinI(id, 5);
        issuer1.run();
        issuer2.run();
        issuer3.run();
        issuer4.run();
        issuer5.run();



    }


}
