package wang.relish.visitor.visitors;

import wang.relish.visitor.property.Hall;
import wang.relish.visitor.property.Kitchen;
import wang.relish.visitor.property.Room;
import wang.relish.visitor.property.Toilet;

/**
 * @author relish
 * @since 2018/04/18
 */
public class KitchenToiletCleaner implements IVisitor {
    @Override
    public void visit(Hall hall) {
        hall.action();
        System.out.println("cleaned " + hall.getClass().getSimpleName() + ".");
    }

    @Override
    public void visit(Kitchen kitchen) {
        kitchen.action();
        System.out.println("cleaned " + kitchen.getClass().getSimpleName() + " and take out the trash.");
    }

    @Override
    public void visit(Room room) {
        room.action();
        System.out.println("Did not clean the " + room.getClass().getSimpleName() + ".");
    }

    @Override
    public void visit(Toilet toilet) {
        toilet.action();
        System.out.println("cleaned " + toilet.getClass().getSimpleName() + " and do the toilet brush.");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"(clean all without Room)";
    }
}
