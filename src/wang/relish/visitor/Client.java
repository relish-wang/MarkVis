package wang.relish.visitor;

import wang.relish.visitor.property.*;
import wang.relish.visitor.visitors.IVisitor;
import wang.relish.visitor.visitors.KitchenToiletCleaner;
import wang.relish.visitor.visitors.OverallCleaner;
import wang.relish.visitor.visitors.ToiletCleaner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 访问者模式
 *
 * @author relish
 * @since 2018/04/18
 */
public class Client {
    private Hall hall = new Hall();
    private Kitchen kitchen = new Kitchen();
    private Room room = new Room();
    private Toilet toilet = new Toilet();

    private List<IProperty> list = new ArrayList<IProperty>() {
        {
            add(hall);
            add(kitchen);
            add(room);
            add(toilet);
        }
    };

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("please choose cleaner:");
        List<IVisitor> cleaners = new ArrayList<>();
        cleaners.add(new OverallCleaner());
        cleaners.add(new KitchenToiletCleaner());
        cleaners.add(new ToiletCleaner());
        for (int i = 0; i < cleaners.size(); i++) {
            System.out.println("#" + (i + 1) + ": " + cleaners.get(i));
        }
        System.out.println();
        int choose = in.nextInt();
        if (choose < 1 || choose > cleaners.size()) {
            System.out.println("no such cleaner!");
            main(null);
            return;
        }
        IVisitor visitor = cleaners.get(choose - 1);

        Client client = new Client();
        for (IProperty property : client.list) {
            property.accept(visitor);
        }
    }
}