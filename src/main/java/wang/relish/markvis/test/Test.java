package wang.relish.markvis.test;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * @author relish
 * @since 2017/09/03
 */
public class Test {

    public static void main(String[] args) {
        MethodSpec methodSpec = MethodSpec.methodBuilder("f")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addStatement("$T.out.println($S)", System.class, "HelloWord!")
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder("A")
                .addMethod(methodSpec)
                .build();
        JavaFile file = JavaFile.builder("", typeSpec).build();

        try {
            new A().finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    static class A {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("123");
            super.finalize();
        }
    }

}
