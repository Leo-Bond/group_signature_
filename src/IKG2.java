import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;



public class IKG2 {
    public static void main(String[] args) {
        // 创建两个线程对象
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Issuer1 issuer1 = new Issuer1();
                try {
                    issuer1.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Issuer2 issuer2 = new Issuer2();
                try {
                    issuer2.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                Issuer3 issuer3 = new Issuer3();
                try {
                    issuer3.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                Issuer4 issuer4 = new Issuer4();
                try {
                    issuer4.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                Issuer5 issuer5 = new Issuer5();
                try {
                    issuer5.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // 启动两个线程
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

    }
}
