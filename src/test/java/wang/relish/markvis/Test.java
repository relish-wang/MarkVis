package wang.relish.markvis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author relish
 * @since 2017/08/31
 */
public class Test {

    static AtomicLong aLong = new AtomicLong(0);
    static long count = 0;

    @org.junit.Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                count++;
                aLong.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                count++;
                aLong.incrementAndGet();
            }
        });
        t1.start();
        t2.start();

        Thread.sleep(3000);
        System.out.println("count = " + count);
        System.out.println("AtomicCount = " + aLong.get());
    }
}
