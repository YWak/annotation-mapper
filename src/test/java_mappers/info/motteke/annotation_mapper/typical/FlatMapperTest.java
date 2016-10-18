package info.motteke.annotation_mapper.typical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class FlatMapperTest {

    @Test
    public void test1() throws Exception {
        List<Flat> list = new ArrayList<Flat>();
        Flat flat;

        flat = new Flat();
        flat.userId = 10L;
        flat.setUserName("user10");
        flat.projectId = 1L;
        flat.setProjectName("project1");
        list.add(flat);

        flat = new Flat();
        flat.userId = 10L;
        flat.setUserName("user10");
        flat.projectId = 2L;
        flat.setProjectName("project2");
        list.add(flat);

        flat = new Flat();
        flat.userId = 11L;
        flat.setUserName("user11");
        flat.projectId = 3L;
        flat.setProjectName("project3");
        list.add(flat);

        Collection<User> users = FlatMapper.map(list);

        System.out.println(users);
    }
}
