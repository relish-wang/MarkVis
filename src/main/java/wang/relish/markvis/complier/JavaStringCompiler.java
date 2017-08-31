package wang.relish.markvis.complier;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory compile Java source code as String.
 *
 * @author michael
 */
public class JavaStringCompiler {

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public JavaStringCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    /**
     * key: Java file name, e.g. "Test.java"
     * value: The source code as String.
     */
    private Map<String, String> map = new HashMap<>();

    private JavaStringCompiler(Map<String, String> map) {
        this();
        this.map = map;
    }

    /**
     * Compile a Java source file in memory.
     *
     * @return The compiled results as Map that contains class name as key,
     * class binary as value.
     * @throws IOException If compile error.
     */
    public Map<String, byte[]> compile() throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            List<JavaFileObject> javaFileObjects = new ArrayList<>();
            for (String key : map.keySet()) {
                javaFileObjects.add(manager.makeStringSource(key, map.get(key)));
            }
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, javaFileObjects);
            Boolean result = task.call();
            if (result == null || !result) {
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    /**
     * Compile a Java source file in memory.
     *
     * @param fileName Java file name, e.g. "Test.java"
     * @param source   The source code as String.
     * @return The compiled results as Map that contains class name as key,
     * class binary as value.
     * @throws IOException If compile error.
     */
    public Map<String, byte[]> compile(String fileName, String source) throws IOException {
        map = new HashMap<>();
        map.put(fileName, source);
        return compile();
    }

    public Map<String, byte[]> compile(Map<String, String> map) throws IOException {
        this.map = map;
        return compile();
    }

    /**
     * Load class from compiled classes.
     *
     * @param name       Full class name.
     * @param classBytes Compiled results as a Map.
     * @return The Class instance.
     * @throws ClassNotFoundException If class not found.
     * @throws IOException            If load error.
     */
    public static Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }


    public static class Builder {
        Map<String, String> map = new HashMap<>();

        /**
         * 添加需要一起编译的文件
         *
         * @param fileName   文件名: Test.java
         * @param sourceCode 文件内容
         */
        public Builder add(String fileName, String sourceCode) {
            map.put(fileName, sourceCode);
            return this;
        }

        public JavaStringCompiler build() {
            return new JavaStringCompiler(this.map);
        }

    }
}
