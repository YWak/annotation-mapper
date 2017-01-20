package info.motteke.annotation_mapper.typical;

import static info.motteke.annotation_mapper.utils.RecursiveFieldMatcher.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class FlatMapperTest {

    @Test
    public void test1() throws Exception {
        List<Flat1> values = load("test1_source.yaml");
        List<Company> expected = load("test1_expected.yaml");

        assertThat(Flat1Mapper.map(values), hasSameValues(expected));
    }

    @SuppressWarnings("unchecked")
    private <E> List<E> load(String name) throws IOException {
        InputStream in = getClass().getResourceAsStream(name);
        try {
            List<E> values = new ArrayList<E>();

            for (E t : (Iterable<E>) new Yaml().load(in)) {
                values.add(t);
            }

            return values;
        } finally {
            in.close();
        }
    }
}
