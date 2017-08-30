package wang.relish.markvis;

import com.google.gson.Gson;
import com.itranswarp.compiler.JavaStringCompiler;

import java.io.IOException;
import java.util.Map;

/**
 * @author relish
 * @since 2017/08/30
 */
public class ClassGenerator {

    private static volatile ClassGenerator instance;

    private ClassGenerator() {
    }

    public static synchronized ClassGenerator getInstance() {
        if (instance == null) {
            instance = new ClassGenerator();
        }
        return instance;
    }

    public void generate(Map<String, String> map) {
        String packageName = getClass().getPackage().getName();
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("public class Bean {");
        for (String key : map.keySet()) {
            sb.append("\tprivate String ").append(key).append(";\n\n");
            String keyToUpperCamelCase = Character.toUpperCase(key.charAt(0)) + key.substring(1);
            sb.append("\tpublic String get").append(keyToUpperCamelCase).append("(){ \n" +
                    "\t\treturn ").append(key).append(";\n" +
                    "\t}\n\n");
            sb.append("\tpublic void set").append(keyToUpperCamelCase).append("(String ").append(key).append("){ \n" +
                    "\t\tthis.").append(key).append("=").append(key).append(";\n" +
                    "\t}\n\n");
        }
        sb.append("}\n");
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results;
        try {
            results = compiler.compile("Bean.java", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Class<?> clazz;
        try {
            clazz = compiler.loadClass(packageName + ".Bean", results);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        Object o = gson.fromJson(gson.toJson(map), clazz);
        System.out.println(o);
    }


}
