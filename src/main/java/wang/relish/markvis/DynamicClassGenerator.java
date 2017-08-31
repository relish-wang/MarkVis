package wang.relish.markvis;

import com.google.gson.Gson;
import com.itranswarp.compiler.JavaStringCompiler;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
        TypeSpec typeSpec = builder.addModifiers(Modifier.PUBLIC).build();
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
            constructorBuilder.addStatement("this.$N = new $T($N+\"\")",
                    fieldName, SimpleStringProperty.class, constructorParamName + "." + getMethodName + "()");
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
                    .addStatement("this.$N = new $T($N)",
                            fieldName, SimpleStringProperty.class, fieldName)
                    .build();
            builder.addField(fieldSpec)
                    .addMethod(methodGet)
                    .addMethod(methodSet);
        }
        TypeSpec typeSpec = builder
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructorBuilder.build()).build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        StringBuilder sb = new StringBuilder();
        try {
            javaFile.writeTo(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static TableView tableView(String path) {
        String json = Util.readStringFromFile(path);
        List<Map<String, String>> maps = Util.jsonArrToList(path);
        @SuppressWarnings("AccessStaticViaInstance")
        Class<?> beanClazz = DynamicClassGenerator.generate(maps.get(0)); //Bean
        Class<?> beanCompatClazz = DynamicClassGenerator.generateCompat(beanClazz);
        Class<?> tempClass = generateTempClass(beanClazz, beanCompatClazz);
        try {
            Method tableView = tempClass.getMethod("tableView", String.class);
            tableView.setAccessible(true);
            Object invoke = tableView.invoke(null, json);
            return ((TableView) invoke);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> generateTempClass(Class<?> clazz, Class<?> clazzCompat) {
        String packageName = clazz.getPackage().getName();
        String className = clazz.getSimpleName();
        String classCompatName = clazzCompat.getSimpleName();
        /*
         * private static List compat(Object[] been) {
         *     List<BeanCompat> data = new ArrayList<BeanCompat>();
         *     for (Bean datum : been) {
         *         data.add(new BeanCompat(datum));
         *     }
         *     return data;
         * }
         */
        String paramName = "been";
        String listName = "data";
        String itemName = "datum";
        String compatMethodName = "compat";
        MethodSpec compatMethod = MethodSpec.methodBuilder(compatMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(List.class)
                .addParameter(Object[].class, paramName)
                .addStatement("List<$N> $N = new $T<$N>()", classCompatName, listName, ArrayList.class, classCompatName)
                .beginControlFlow("for ($N $N : $N)", Object.class.getSimpleName(), itemName, paramName)
                .addStatement("$N.add(new $N(($N)$N))", listName, classCompatName, className, itemName)
                .endControlFlow()
                .addStatement("return $N", listName)
                .build();
        /*
         * private static ObservableList getItems(Object[] been) {
         *     return new ObservableListWrapper<BeanCompat>(compat(been));
         * }
         */
        String getItemsMethodName = "getItems";
        MethodSpec getItemsMethod = MethodSpec.methodBuilder(getItemsMethodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ObservableList.class)
                .addParameter(Object[].class, paramName)
                .addStatement("return new $T<$N>($N($N))", ObservableListWrapper.class, classCompatName, compatMethodName, paramName)
                .build();
        /*
         * private static <Compat> TableColumn[] generateTableColumn(Class<?> clazz) {
         *     Field[] fields = clazz.getDeclaredFields();
         *     TableColumn[] tableColumns = new TableColumn[fields.length];
         *     for (int i = 0; i < fields.length; i++) {
         *         tableColumns[i] = new TableColumn(fields[i].getName());
         *         tableColumns[i].setCellValueFactory(new PropertyValueFactory<Compat, SimpleObjectProperty>(fields[i].getName()));
         *     }
         *     return tableColumns;
         * }
         */
        String fields = "fields";
        String tableColumns = "tableColumns";
        String tableColumnParamName = "clazz";
        String generateTableColumnMethodName = "generateTableColumn";
        MethodSpec generateTableColumnMethod = MethodSpec.methodBuilder(generateTableColumnMethodName)
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(TableColumn[].class)
                .addParameter(Class.class, tableColumnParamName)
                .addStatement("$T[] $N = $N.getDeclaredFields()", Field.class, fields, tableColumnParamName)
                .addStatement("$T[] $N = new $T[$N.length]", TableColumn.class, tableColumns, TableColumn.class, fields)
                .beginControlFlow("for (int i = 0; i < $N.length; i++)", fields)
                .addStatement("$N[i] = new $T($N[i].getName())", tableColumns, TableColumn.class, fields)
                .addStatement("$N[i].setCellValueFactory(new $T<$N, $T>($N[i].getName()))", tableColumns, PropertyValueFactory.class, classCompatName, SimpleObjectProperty.class, fields)
                .endControlFlow()
                .addStatement("return $N", tableColumns)
                .build();

        /*
         * public static TableView tableView(String json, Class<?> clazz, Class<?> classCompat){
         *     Bean[] bean = new Gson().fromJson(json, Bean[].class);
         *     ObservableList<BeanCompat> items = getItems(bean);
         *     TableView<BeanCompat> table = new TableView<BeanCompat>();
         *     table.setItems(items);
         *     TableColumn[] generate = generateTableColumn(Bean.class);
         *     table.getColumns().addAll(generate);
         *     table.setMinWidth(876);
         *     return table;
         * }
         */
        String paramJson = "json";
        String been = "been";
        String items = "items";
        String table = "table";
        String generate = "generate";
        MethodSpec tableViewMethod = MethodSpec.methodBuilder("tableView")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(TableView.class)
                .addParameter(String.class, paramJson)
                .addStatement("$T[] $N = new $T().fromJson($N, $T[].class)", clazz, been, Gson.class, paramJson, clazz)
                .addStatement("$T<$N> $N = $N($N)", ObservableList.class, classCompatName, items, getItemsMethodName, been)
                .addStatement("$T<$N> $N = new $T<$N>()", TableView.class, classCompatName, table, TableView.class, classCompatName)
                .addStatement("$N.setItems($N)", table, items)
                .addStatement("$T[] $N = $N($N.class)", TableColumn.class, generate, generateTableColumnMethodName, className)
                .addStatement("$N.getColumns().addAll($N)", table, generate)
                .addStatement("$N.setMinWidth(876);", table)
                .addStatement("return $N", table)
                .build();


        String resultClassName = "Temp";
        TypeSpec typeSpec = TypeSpec.classBuilder(resultClassName)
                .addMethod(compatMethod)
                .addMethod(getItemsMethod)
                .addMethod(generateTableColumnMethod)
                .addMethod(tableViewMethod)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();

        StringBuilder sb = new StringBuilder();
        try {
            javaFile.writeTo(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return compiler(sb.toString(), resultClassName);
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
