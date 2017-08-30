package wang.relish.markvis;

import com.itranswarp.compiler.JavaStringCompiler;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.istack.internal.NotNull;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Map;

/**
 * @author relish
 * @since 2017/08/30
 */
public class DynamicClassGenerator {

    private static volatile DynamicClassGenerator instance;

    private DynamicClassGenerator() {
    }

    public static synchronized DynamicClassGenerator getInstance() {
        if (instance == null) {
            instance = new DynamicClassGenerator();
        }
        return instance;
    }

    public static Class<?> generate(Map<String, String> map) {
        String className = "Bean";
        String fileName = className + ".java";
        String packageName = DynamicClassGenerator.class.getPackage().getName();
        String path = packageName + "." + className;

        String javaFileString = generateJavaFileString(map, className, packageName);//生成文件

        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results;
        try {
            results = compiler.compile(fileName, javaFileString);
        } catch (IOException e) {
            e.printStackTrace();
            return Object.class;
        }
        Class<?> clazz;
        try {
            clazz = compiler.loadClass(path, results);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Object.class;
        } catch (IOException e) {
            e.printStackTrace();
            return Object.class;
        }
        return clazz;
    }

    private static String generateJavaFileString(Map<String, String> map, @NotNull String className, @NotNull String packageName) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(className);
        for (String key : map.keySet()) {
            String upperCamelCaseKey = toUpperCamelCase(key);
            @SuppressWarnings("Since15")
            FieldSpec fieldSpec = FieldSpec.builder(String.class, key)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            @SuppressWarnings("Since15")
            MethodSpec methodGet = MethodSpec.methodBuilder("get" + upperCamelCaseKey)
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return this.$N", key)
                    .build();
            @SuppressWarnings("Since15")
            MethodSpec methodSet = MethodSpec.methodBuilder("set" + upperCamelCaseKey)
                    .returns(void.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.$N = $N;", key, key)
                    .build();
            builder.addField(fieldSpec)
                    .addMethod(methodGet)
                    .addMethod(methodSet);
        }
        TypeSpec typeSpec = builder.build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();
        StringBuilder sb = new StringBuilder();
        try {
            javaFile.writeTo(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

//    private static String generateBeanCompatString(Class<?> clazz){
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//
//        }
//    }

    /**
     * 变量名转大驼峰
     *
     * @param key 变量名
     * @return 大驼峰命名
     */
    private static String toUpperCamelCase(@NotNull String key) {
        if (key == null || key.equals("")) throw new IllegalArgumentException("Field should not be null.");
        if (key.length() < 2) return key;
        return Character.toUpperCase(key.charAt(0)) + key.substring(1);
    }
}
