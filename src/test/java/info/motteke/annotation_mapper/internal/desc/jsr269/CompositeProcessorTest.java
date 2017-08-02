package info.motteke.annotation_mapper.internal.desc.jsr269;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import info.motteke.annotation_mapper.typical.Company;
import info.motteke.annotation_mapper.typical.Flat1;
import info.motteke.annotation_mapper.util.Beans;
import info.motteke.annotation_mapper.util.Compiler;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CompositeProcessorTest {

    @Rule
    public final Compiler compiler = new Compiler();

    @Rule
    public final Beans beans = new Beans();

    @Before
    public void setUp() throws Exception {
        compiler.setCharset("UTF-8");
        compiler.addOption("-source", "1.6");
        compiler.addSourcePath("src/test/java");
        compiler.addProcessor(new CompositeProcessor());
    }

    @Test
    public void test_typical_pattern() throws Exception {
        compiler.addCompilationUnit("info.motteke.annotation_mapper.typical.Flat1");
        compiler.compile();

        assertThat(compiler.getCompileResult(), is(true));

        Class<?> clazz = compiler.getCompiledClass("info.motteke.annotation_mapper.typical.Flat1Mapper");
        List<Flat1> list = beans.loadAll("test1_source.yaml", Flat1.class);

        List<Company> actual = (List<Company>) clazz.getMethod("map", Collection.class).invoke(null, list);

        beans.verify(actual, "test1_expected.yaml");
    }
}
