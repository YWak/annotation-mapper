package info.motteke.annotation_mapper.typical;

@javax.annotation.Generated("info.motteke.annotation_mapper.Mapper")
public final class FlatMapper {

    public static java.util.Collection<info.motteke.annotation_mapper.typical.User> map(java.util.Collection<? extends info.motteke.annotation_mapper.typical.Flat> values) {

        java.util.List<info.motteke.annotation_mapper.typical.User> mappedValues = new java.util.ArrayList<info.motteke.annotation_mapper.typical.User>();

        // declare variables
        info.motteke.annotation_mapper.typical.User o1 = null;
        java.util.List<info.motteke.annotation_mapper.typical.Project> c2 = null;
        info.motteke.annotation_mapper.typical.Project o2 = null;
        info.motteke.annotation_mapper.typical.User o3 = null;

        info.motteke.annotation_mapper.typical.Flat prev = null;

        for (info.motteke.annotation_mapper.typical.Flat curr : values) {

            // check if changes
            boolean _equals1 = equals1(prev, curr);
            boolean _equals2 = equals2(prev, curr);
            boolean _equals3 = equals3(prev, curr);

            // create new Object if changes
            if (!_equals1) {
                o1 = new info.motteke.annotation_mapper.typical.User();
                mappedValues.add(o1);
                c2 = new java.util.ArrayList<info.motteke.annotation_mapper.typical.Project>();
                o1.projects = c2;

            }

            if (!_equals2 || !_equals1) {
                o2 = new info.motteke.annotation_mapper.typical.Project();
                c2.add(o2);

            }

            if (!_equals3 || !_equals2 || !_equals1) {
                o3 = new info.motteke.annotation_mapper.typical.User();
                o2.owner = o3;

            }


            // copy values
            o1.id = curr.userId;
            o1.name = curr.getUserName();
            o2.id = curr.projectId;
            o2.name = curr.getProjectName();
            o3.id = curr.userId;
            o3.name = curr.getUserName();
            prev = curr;
        }

        return mappedValues;
    }

    private static boolean equals1(info.motteke.annotation_mapper.typical.Flat o1, info.motteke.annotation_mapper.typical.Flat o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.userId, o2.userId)) {
            return false;
        }
        return true;
    }

    private static boolean equals2(info.motteke.annotation_mapper.typical.Flat o1, info.motteke.annotation_mapper.typical.Flat o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.projectId, o2.projectId)) {
            return false;
        }
        return true;
    }

    private static boolean equals3(info.motteke.annotation_mapper.typical.Flat o1, info.motteke.annotation_mapper.typical.Flat o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.userId, o2.userId)) {
            return false;
        }
        return true;
    }

    // null safe object comparison
    private static boolean _equals(Object o1, Object o2) {
        return (o1 == null || o2 == null) ? (o1 == null && o2 == null) : o1.equals(o2);
    }

    private FlatMapper() { }


}
