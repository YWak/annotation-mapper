package info.motteke.annotation_mapper;

import info.motteke.annotation_mapper.errors.Warnings;
import info.motteke.annotation_mapper.internal.desc.jsr269.CompositeProcessor;
import info.motteke.annotation_mapper.typical.Flat;

import org.seasar.aptina.unit.AptinaTestCase;

public class CompositeProcessorTest extends AptinaTestCase {

    protected void setUp() throws Exception {
        setCharset("UTF-8");
        addSourcePath("src/test/java");

        CompositeProcessor processor = new CompositeProcessor();
        addProcessor(processor);
    }

    public void test_typical_pattern() throws Exception {
        addCompilationUnit(Flat.class);

        compile();

        assertEqualsGeneratedSourceWithResource("info/motteke/annotation_mapper/typical/FlatMapper", "info.motteke.annotation_mapper.typical.FlatMapper");
    }

    public void test_warnings() throws Exception {
        addCompilationUnit(Warnings.class);
        compile();
    }
}
