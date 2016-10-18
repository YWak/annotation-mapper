package info.motteke.annotation_mapper.internal.desc.jsr269;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

public class CompositeProcessorTest extends AbstractCompositeProcessorTestCase {

    @Override
    public void setUp() throws Exception {
        setCharset("UTF-8");
        addOption("-source", "1.6");
        addSourcePath("src/test/java", "src/test/java_errors");
        addProcessor(new CompositeProcessor());

        setExpectedSourcesDir("src/test/java_mappers");
    }

    public void test_typical_pattern() throws Exception {
        addCompilationUnit("info.motteke.annotation_mapper.typical.Flat");

        compile();

        assertGeneratedSource("info.motteke.annotation_mapper.typical.FlatMapper");
    }

    public void test_warnings() throws Exception {
        addCompilationUnit("info.motteke.annotation_mapper.errors.Warnings");
        compile();

        assertFalse(getCompiledResult());
        List<Diagnostic<? extends JavaFileObject>> diagnostics = getDiagnostics("info.motteke.annotation_mapper.errors.Warnings");

        assertThat(diagnostics.size(), is(2));
        assertThat(diagnostics.get(0), hasKind(Kind.ERROR)
                                           .line(10)
                                           .column(5)
                                           .meessage("@Fieldが値を読み出せない箇所に設定されています。"));
        assertThat(diagnostics.get(1), hasKind(Kind.ERROR)
                                           .line(15)
                                           .column(5)
                                           .meessage("@Fieldが値を読み出せない箇所に設定されています。"));
    }
}
