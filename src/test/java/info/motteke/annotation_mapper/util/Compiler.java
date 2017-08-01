package info.motteke.annotation_mapper.util;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.seasar.aptina.unit.AptinaTestCase;

public class Compiler extends TestWatcher {

    private final InternalTestCase compiler = new InternalTestCase();

    private JavaFileManager javaFileManager;

    // 基本的に全メソッドを委譲する
    /**
     * @param charset
     */
    public void setCharset(String charset) {
        compiler.mirror.call("setCharset", charset);
    }

    public void addCompilationUnit(String className) {
        compiler.mirror.call("addCompilationUnit", className);
    }

    public void addOption(String... options) {
        compiler.mirror.callVarArgs("addOption", options);
    }

    public void addProcessor(Processor... processors) {
        compiler.mirror.callVarArgs("addProcessor", processors);
    }

    public void addSourcePath(String... sourcePaths) {
        compiler.mirror.callVarArgs("addSourcePath", sourcePaths);
    }

    /**
     * コンパイルを実行します。
     *
     * @throws IOException 入出力例外
     */
    public void compile() throws IOException {
        compiler.compile();
    }

    /**
     * コンパイルの結果を取得します。
     *
     * @return コンパイルの結果
     * @throws IllegalStateException コンパイルが実行されていない場合
     */
    public Boolean getCompileResult() throws IllegalStateException {
        try {
            return compiler.mirror.call("getCompiledResult");
        } catch (MirrorOperationException e) {
            e.throwIfExists(IllegalStateException.class);
            throw e;
        }
    }

    /**
     * コンパイル結果のクラスを取得します。
     *
     * @param className クラス名
     * @return 対応するクラスを表す{@link Class}のインスタンス
     * @throws IllegalStateException コンパイルが実行されていない場合
     * @throws ClassNotFoundException クラスが見つからなかった場合
     */
    public Class<?> getCompiledClass(String className) throws IllegalStateException, ClassNotFoundException {
        assertCompiled();

        return javaFileManager.getClassLoader(null).loadClass(className);
    }

    @Override
    protected void finished(Description description) {
        try {
            compiler.mirror.call("tearDown");
        } catch (Exception e) {
            throw new InternalError();
        }
    }

    private void assertCompiled() throws IllegalStateException {
        try {
            compiler.mirror.call("assertCompiled");
        } catch (MirrorOperationException e) {
            e.throwIfExists(IllegalStateException.class);
            throw e;
        }
    }

    private class InternalTestCase extends AptinaTestCase {

        private final Mirror mirror = Mirror.ofInstance(this);

        @Override
        public void compile() throws IOException {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager jfm = javaCompiler.getStandardFileManager(diagnostics, getLocale(), getCharset());
            jfm.setLocation(StandardLocation.SOURCE_PATH, mirror.get("sourcePaths"));

            // ここを変えるためだけにコピペせざるをえなくなった。
            javaFileManager = new InMemoryJavaFileManager(jfm);

            mirror.set("javaCompiler", javaCompiler);
            mirror.set("diagnostics", diagnostics);
            mirror.set("standardJavaFileManager", jfm);
            mirror.set("testingJavaFileManager", javaFileManager);

            CompilationTask task =
                    javaCompiler.getTask(mirror.get("out"), javaFileManager, null,
                                         mirror.get("options"), null, mirror.call("getCompilationUnits"));
            task.setProcessors(mirror.get("processors"));

            // 実行
            mirror.set("compiledResult", task.call());

            List<?> compilationUnits = mirror.get("compilationUnits");
            compilationUnits.clear();
        }
    }
}
