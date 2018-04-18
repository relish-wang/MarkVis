package wang.relish.visitor.property;

import wang.relish.visitor.visitors.IVisitor;

/**
 * @author relish
 * @since 2018/04/18
 */
public class Room implements IProperty{

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void action() {
        System.out.println("----------visit Room----------");
    }
}
