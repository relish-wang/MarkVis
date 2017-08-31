package wang.relish.markvis;

/**
 * @author relish
 * @since 2017/08/31
 */
public class ClassUtil {

    public static Class<?> getRawClass(Class<?> clazz) {
        return clazz.getComponentType();
    }
}
