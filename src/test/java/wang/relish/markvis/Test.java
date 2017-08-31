package wang.relish.markvis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author relish
 * @since 2017/08/31
 */
public class Test {

    static AtomicLong aLong = new AtomicLong(0);
    static volatile long count = 0L;

    @org.junit.Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                f();
                aLong.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                f();
                aLong.incrementAndGet();
            }
        });
        t1.start();
        t2.start();

        Thread.sleep(3000);
        System.out.println("count = " + count);
        System.out.println("AtomicCount = " + aLong.get());
    }

    synchronized void f() {
        count++;
    }

    @org.junit.Test
    public void sd() throws IllegalAccessException, InstantiationException {

        Class<?> cla = String.class;
        Class<?> arrCla = String[].class;


        System.out.println(arrCla.isArray());

        Class<?> componentType = arrCla.getComponentType();

        System.out.println(componentType.getSimpleName());
        System.out.println(arrCla.getSimpleName());
//        System.out.println(os.getClass().getSimpleName());
//        System.out.println(os.getClass()==arrClazz);
    }
}
