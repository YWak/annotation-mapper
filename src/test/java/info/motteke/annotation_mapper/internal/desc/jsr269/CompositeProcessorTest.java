package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.errors.Warnings;
import info.motteke.annotation_mapper.typical.Flat;

import org.seasar.aptina.unit.AptinaTestCase;

public class CompositeProcessorTest extends AptinaTestCase {

    protected void setUp() throws Exception {
        setCharset("UTF-8");
        addOption("-source", "1.6");
        addSourcePath("src/test/java");

        CompositeProcessor processor = new CompositeProcessor();
        addProcessor(processor);
    }

    public void test_typical_pattern() throws Exception {
        addCompilationUnit(Flat.class);

        compile();

        System.out.println(getGeneratedSource("info.motteke.annotation_mapper.typical.FlatMapper"));
        assertGeneratedSource("info.motteke.annotation_mapper.typical.FlatMapper");
    }

    public void test_warnings() throws Exception {
        addCompilationUnit(Warnings.class);
        compile();

        assertFalse(getCompiledResult());
    }

    /**
     * 生成されたコードが想定と一致しているかを検証します。
     *
     * @param fqn 生成されたコードのFully Qualified Name
     *
     * @throws Exception
     */
    private void assertGeneratedSource(String fqn) throws Exception {
        String resource = fqn.replaceAll("\\.", "/");

        assertEqualsGeneratedSourceWithResource(resource, fqn);
    }
}
