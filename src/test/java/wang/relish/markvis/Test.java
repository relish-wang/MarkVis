package wang.relish.markvis;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author relish
 * @since 2017/08/31
 */
public class Test extends Application {

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


    @org.junit.Test
    public void main() {
        launch(null);
    }

    @Override
    public void start(Stage stage) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(876);
        stage.setHeight(500);

        String path = getClass().getResource("/json.json").getPath();
//        TableView table = DynamicClassGenerator.tableView(path);//tableView(getClass().getResource("/json.json").getPath());
        String json = Util.readStringFromFile(path);
//        TableView table = Temp.tableView(json);
//
//        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }
}
