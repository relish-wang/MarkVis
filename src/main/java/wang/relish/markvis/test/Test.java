package wang.relish.markvis.test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * @author relish
 * @since 2017/09/03
 */
public class Test {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException {
        new ChildA();

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.getCtClass("wang.relish.markvis.test.ChildA");
        ctClass.setSuperclass(pool.get("wang.relish.markvis.test.A"));
        ctClass.setName("ChildA2");
        ctClass.writeFile();

        Class aClass = ctClass.toClass();
        Object o = aClass.newInstance();
    }

}
