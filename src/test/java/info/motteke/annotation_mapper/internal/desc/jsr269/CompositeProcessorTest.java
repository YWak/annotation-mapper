package info.motteke.annotation_mapper.internal.desc.jsr269;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import info.motteke.annotation_mapper.util.Compiler;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CompositeProcessorTest {

    @Rule
    public final Compiler compiler = new Compiler();

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

        System.out.println(clazz);
        Arrays.asList(clazz.getMethods()).forEach(System.out::println);
    }
}
