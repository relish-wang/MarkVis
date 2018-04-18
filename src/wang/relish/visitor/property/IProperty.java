package wang.relish.visitor.property;

import wang.relish.visitor.visitors.IVisitor;

/**
 * @author relish
 * @since 2018/04/18
 */
public interface IProperty {

    void accept(IVisitor visitor);

    void action();
}
