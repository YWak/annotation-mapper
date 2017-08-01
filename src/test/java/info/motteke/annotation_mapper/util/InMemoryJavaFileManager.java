package info.motteke.annotation_mapper.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.security.SecureClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

public class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, InMemoryJavaFileObject> cache = new HashMap<>();

    private ClassLoader loader;

    public InMemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
        InMemoryJavaFileObject co = new InMemoryJavaFileObject(className, kind);
        cache.put(className, co);

        return co;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        if (loader == null) {
            loader = new InMemoryClassLoader(Collections.unmodifiableMap(cache));
        }

        return loader;
    }

    private static class InMemoryClassLoader extends SecureClassLoader {

        private final Map<String, InMemoryJavaFileObject> objectCache;

        private final Map<InMemoryJavaFileObject, Class<?>> classCache;

        public InMemoryClassLoader(Map<String, InMemoryJavaFileObject> cache) {
            this.objectCache = cache;
            this.classCache = new HashMap<>();
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            InMemoryJavaFileObject co = objectCache.get(name);

            if (co == null) {
                return super.findClass(name);
            }

            Class<?> clazz = classCache.get(co);

            if (clazz == null) {
                byte[] b = co.getBytes();
                clazz = super.defineClass(name, b, 0, b.length);
                classCache.put(co, clazz);
            }

            return clazz;
        }
    }

    private static class InMemoryJavaFileObject extends SimpleJavaFileObject {

        private final ByteArrayOutputStream binary = new ByteArrayOutputStream();

        private String source;

        public InMemoryJavaFileObject(String name, Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            if (!kind.equals(Kind.SOURCE)) {
                return super.getCharContent(ignoreEncodingErrors);
            }
        
            if (source == null) {
                source = new String(binary.toByteArray());
            }
        
            return source;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return binary;
        }

        public byte[] getBytes() {
            return binary.toByteArray();
        }
    }

}
