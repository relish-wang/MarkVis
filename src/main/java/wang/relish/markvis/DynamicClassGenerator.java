package wang.relish.markvis;

import com.itranswarp.compiler.JavaStringCompiler;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.SimpleStringProperty;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Field;
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
        String packageName = DynamicClassGenerator.class.getPackage().getName();

        String javaFileString = generateJavaFileString(map, className, packageName);//生成文件
        System.out.println(javaFileString);

        return compiler(javaFileString, className);
    }

    public static Class<?> generateCompat(Class<?> beanClass) {
        String compatClassName = beanClass.getSimpleName() + "Compat";
        String javaFileString = generateBeanCompatString(beanClass, compatClassName);
        System.out.println(javaFileString);
        return compiler(javaFileString, compatClassName);
    }

    private static Class<?> compiler(String javaString, String className) {
        String fileName = className + ".java";
        String path = DynamicClassGenerator.class.getPackage().getName() + "." + className;
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results;
        try {
            results = compiler.compile(fileName, javaString);
        } catch (IOException e) {
            e.printStackTrace();
            return Object.class;
        }
        Class<?> clazz;
        try {
            clazz = compiler.loadClass(path, results);
        } catch (ClassNotFoundException | IOException e) {
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

    private static String generateBeanCompatString(Class<?> clazz, String compatClassName) {
        String packageName = clazz.getPackage().getName();
        String className = clazz.getSimpleName();
        TypeSpec.Builder builder = TypeSpec.classBuilder(compatClassName);
        Field[] fields = clazz.getDeclaredFields();
        String constructorParamName = toLowerCamelCase(className);
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(clazz, constructorParamName);
        for (Field field : fields) {
            String fieldName = field.getName();
            String upperCamelCaseKey = toUpperCamelCase(fieldName);
            String getMethodName = "get" + upperCamelCaseKey;
            String setMethodName = "set" + upperCamelCaseKey;
            constructorBuilder.addStatement("this.$N = new SimpleStringProperty($N+\"\")",
                    fieldName, constructorParamName + "." + getMethodName + "()");
            FieldSpec fieldSpec = FieldSpec.builder(SimpleStringProperty.class, fieldName)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            MethodSpec methodGet = MethodSpec.methodBuilder(getMethodName)
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return this.$N.get()", fieldName)
                    .build();
            MethodSpec methodSet = MethodSpec.methodBuilder(setMethodName)
                    .returns(void.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, fieldName)
                    .addStatement("this.$N = new SimpleStringProperty($N)",
                            fieldName, fieldName)
                    .build();
            builder.addField(fieldSpec)
                    .addMethod(methodGet)
                    .addMethod(methodSet);
        }
        TypeSpec typeSpec = builder.addMethod(constructorBuilder.build()).build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        StringBuilder sb = new StringBuilder();
        try {
            javaFile.writeTo(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

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

    /**
     * 变量名转小驼峰
     *
     * @param key 变量名
     * @return 小驼峰命名
     */
    private static String toLowerCamelCase(@NotNull String key) {
        if (key == null || key.equals("")) throw new IllegalArgumentException("Field should not be null.");
        if (key.length() < 2) return key;
        return Character.toLowerCase(key.charAt(0)) + key.substring(1);
    }
}
