package info.motteke.annotation_mapper.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
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
        compiler.setCharset(charset);
    }

    public void addCompilationUnit(String className) {
        compiler.addCompilationUnit(className);
    }

    public void addOption(String... options) {
        compiler.addOption(options);
    }

    public void addProcessor(Processor... processors) {
        compiler.addProcessor(processors);
    }

    public void addSourcePath(String... sourcePaths) {
        compiler.addSourcePath(sourcePaths);
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
        return compiler.getCompiledResult();
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
        compiler.assertCompiled();
        return javaFileManager.getClassLoader(null).loadClass(className);
    }

    public <T> void verifyErrorMessage(Class<T> clazz, String message) {
        verifyErrorMessage(clazz, null, message);
    }

    public <T> void verifyErrorMessage(Class<T> clazz, Locale locale, String message) {
        verifyMessage(clazz, Kind.ERROR, locale, message);
    }

    public <T> void verifyWarningMessage(Class<T> clazz, String message) {
        verifyWarningMessage(clazz, null, message);
    }

    public <T> void verifyWarningMessage(Class<T> clazz, Locale locale, String message) {
        verifyMessage(clazz, Kind.WARNING, locale, message);
    }

    private <T> void verifyMessage(Class<T> clazz, Kind kind, Locale locale, String message) {
        for (Diagnostic<? extends JavaFileObject> diagnostic: compiler.getDiagnostics(clazz, kind)) {
            if (diagnostic.getMessage(locale).equals(message)) {
                return;
            }
        }

        // レベルが違うけど同じメッセージがある？
        for (Diagnostic<? extends JavaFileObject> diag : compiler.getDiagnostics(clazz)) {
            if (diag.getMessage(locale).equals(message) && !diag.getKind().equals(kind)) {
                String detailMessage = "message [" + message + "] exists. but the KIND was [" + diag.getKind() + "]";
                throw new AssertionError(detailMessage);
            }
        }

        throw new AssertionError();
    }

    @Override
    protected void finished(Description description) {
        try {
            compiler.tearDown();
        } catch (Exception e) {
            throw new InternalError();
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

        protected void assertCompiled() throws IllegalStateException {
            try {
                mirror.call("assertCompiled");
            } catch (MirrorOperationException e) {
                throw e.throwIfExists(IllegalStateException.class);
            }
        }

        // 委譲
        @Override protected void setCharset(String charset) { super.setCharset(charset); }
        @Override protected void addCompilationUnit(String className) { super.addCompilationUnit(className); }
        @Override protected void addOption(String... options) { super.addOption(options); }
        @Override protected void addProcessor(Processor... processors) { super.addProcessor(processors); }
        @Override protected void addSourcePath(String... sourcePaths) { super.addSourcePath(sourcePaths); }
        @Override protected Boolean getCompiledResult() throws IllegalStateException { return super.getCompiledResult(); }
        @Override protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(Class<?> clazz) { return super.getDiagnostics(clazz); }
        @Override protected List<Diagnostic<? extends JavaFileObject>> getDiagnostics(Class<?> clazz, Kind kind) { return super.getDiagnostics(clazz, kind); }
        @Override protected void tearDown() throws Exception { super.tearDown(); }
    }
}
