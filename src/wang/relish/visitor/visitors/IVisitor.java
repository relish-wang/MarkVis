package wang.relish.visitor.visitors;


import wang.relish.visitor.property.Hall;
import wang.relish.visitor.property.Kitchen;
import wang.relish.visitor.property.Room;
import wang.relish.visitor.property.Toilet;

/**
 * 访问者
 *
 * @author relish
 * @since 2018/04/18
 */
public interface IVisitor {
    void visit(Hall hall);

    void visit(Kitchen kitchen);

    void visit(Room room);

    void visit(Toilet toilet);
}
