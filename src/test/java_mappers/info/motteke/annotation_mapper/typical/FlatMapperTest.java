package info.motteke.annotation_mapper.typical;

import static info.motteke.annotation_mapper.utils.RecursiveFieldMatcher.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FlatMapperTest {

    @Test
    public void test1() throws Exception {
        List<Flat> list = new ArrayList<Flat>();
        Flat flat;

        list.add(flat = new Flat());
        flat.userId = 10L;
        flat.setUserName("user10");
        flat.projectId = 1L;
        flat.setProjectName("project1");

        list.add(flat = new Flat());
        flat.userId = 10L;
        flat.setUserName("user10");
        flat.projectId = 2L;
        flat.setProjectName("project2");

        list.add(flat = new Flat());
        flat.userId = 11L;
        flat.setUserName("user11");
        flat.projectId = 3L;
        flat.setProjectName("project3");

        List<User> expected = new ArrayList<User>();
        User u;
        Project p;

        expected.add(u = new User());
        u.id = 10L;
        u.name = "user10";
        u.projects = new ArrayList<Project>();

        u.projects.add(p = new Project());
        p.id = 1L;
        p.name = "project1";
        p.owner = new User();
        p.owner.id = u.id;
        p.owner.name = u.name;

        u.projects.add(p = new Project());
        p.id = 2L;
        p.name = "project2";
        p.owner = new User();
        p.owner.id = u.id;
        p.owner.name = u.name;

        expected.add(u = new User());
        u.id = 11L;
        u.name = "user11";
        u.projects = new ArrayList<Project>();

        u.projects.add(p = new Project());
        p.id = 3L;
        p.name = "project3";
        p.owner = new User();
        p.owner.id = u.id;
        p.owner.name = u.name;

        assertThat(FlatMapper.map(list), hasSameValues(expected));
    }
}
