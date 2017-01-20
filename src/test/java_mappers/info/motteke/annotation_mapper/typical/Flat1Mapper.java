package info.motteke.annotation_mapper.typical;

@javax.annotation.Generated("info.motteke.annotation_mapper.Mapper")
public final class Flat1Mapper {

    public static java.util.Collection<info.motteke.annotation_mapper.typical.Company> map(java.util.Collection<? extends info.motteke.annotation_mapper.typical.Flat1> values) {

        java.util.List<info.motteke.annotation_mapper.typical.Company> mappedValues = new java.util.ArrayList<info.motteke.annotation_mapper.typical.Company>();

        // declare variables
        info.motteke.annotation_mapper.typical.Company o1 = null;
        info.motteke.annotation_mapper.typical.User o2 = null;
        java.util.List<info.motteke.annotation_mapper.typical.Section> c3 = null;
        info.motteke.annotation_mapper.typical.Section o3 = null;
        info.motteke.annotation_mapper.typical.User o4 = null;
        java.util.List<info.motteke.annotation_mapper.typical.User> c5 = null;
        info.motteke.annotation_mapper.typical.User o5 = null;
        java.util.List<info.motteke.annotation_mapper.typical.Issue> c6 = null;
        info.motteke.annotation_mapper.typical.Issue o6 = null;
        info.motteke.annotation_mapper.typical.User o7 = null;

        info.motteke.annotation_mapper.typical.Flat1 prev = null;

        for (info.motteke.annotation_mapper.typical.Flat1 curr : values) {

            // check if changes
            boolean _equals1 = equals1(prev, curr);
            boolean _equals2 = equals2(prev, curr);
            boolean _equals3 = equals3(prev, curr);
            boolean _equals4 = equals4(prev, curr);
            boolean _equals5 = equals5(prev, curr);
            boolean _equals6 = equals6(prev, curr);
            boolean _equals7 = equals7(prev, curr);
            boolean _hasKey1 = hasKey1(curr);
            boolean _hasKey2 = hasKey2(curr);
            boolean _hasKey3 = hasKey3(curr);
            boolean _hasKey4 = hasKey4(curr);
            boolean _hasKey5 = hasKey5(curr);
            boolean _hasKey6 = hasKey6(curr);
            boolean _hasKey7 = hasKey7(curr);

            // create new Object if changes
            if (_hasKey1 && (!_equals1)) {
                o1 = new info.motteke.annotation_mapper.typical.Company();
                mappedValues.add(o1);
                c3 = new java.util.ArrayList<info.motteke.annotation_mapper.typical.Section>();
                o1.setSections(c3);
                // copy values
                o1.id = curr.companyId;
                o1.name = curr.companyName;

            }

            if (_hasKey2 && (!_equals2 || !_equals1)) {
                o2 = new info.motteke.annotation_mapper.typical.User();
                o1.setPresident(o2);
                // copy values
                o2.id = curr.getPresidentId();
                o2.name = curr.getPresidentName();

            }

            if (_hasKey3 && (!_equals3 || !_equals1)) {
                o3 = new info.motteke.annotation_mapper.typical.Section();
                c3.add(o3);
                c5 = new java.util.ArrayList<info.motteke.annotation_mapper.typical.User>();
                o3.staffs = c5;
                // copy values
                o3.id = curr.sectionId;
                o3.name = curr.sectionName;

            }

            if (_hasKey4 && (!_equals4 || !_equals3 || !_equals1)) {
                o4 = new info.motteke.annotation_mapper.typical.User();
                o3.manager = o4;
                // copy values
                o4.id = curr.managerId;
                o4.name = curr.managerName;

            }

            if (_hasKey5 && (!_equals5 || !_equals3 || !_equals1)) {
                o5 = new info.motteke.annotation_mapper.typical.User();
                c5.add(o5);
                c6 = new java.util.ArrayList<info.motteke.annotation_mapper.typical.Issue>();
                o5.issues = c6;
                // copy values
                o5.id = curr.staffId;
                o5.name = curr.staffName;

            }

            if (_hasKey6 && (!_equals6 || !_equals5 || !_equals3 || !_equals1)) {
                o6 = new info.motteke.annotation_mapper.typical.Issue();
                c6.add(o6);
                // copy values
                o6.id = curr.issueId;
                o6.title = curr.getIssueTitle();

            }

            if (_hasKey7 && (!_equals7 || !_equals6 || !_equals5 || !_equals3 || !_equals1)) {
                o7 = new info.motteke.annotation_mapper.typical.User();
                o6.owner = o7;
                // copy values
                o7.id = curr.staffId;
                o7.name = curr.staffName;

            }

            prev = curr;
        }

        return mappedValues;
    }

    private static boolean equals1(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.companyId, o2.companyId)) {
            return false;
        }
        return true;
    }

    private static boolean equals2(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        return true;
    }

    private static boolean equals3(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.sectionId, o2.sectionId)) {
            return false;
        }
        return true;
    }

    private static boolean equals4(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.managerId, o2.managerId)) {
            return false;
        }
        return true;
    }

    private static boolean equals5(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (o1.staffId != o2.staffId) {
            return false;
        }
        return true;
    }

    private static boolean equals6(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        if (!_equals(o1.issueId, o2.issueId)) {
            return false;
        }
        return true;
    }

    private static boolean equals7(info.motteke.annotation_mapper.typical.Flat1 o1, info.motteke.annotation_mapper.typical.Flat1 o2) {
        if (o1 == null || o2 == null) return (o1 == null && o2 == null);
        return true;
    }

    // null safe object comparison
    private static boolean _equals(Object o1, Object o2) {
        return (o1 == null || o2 == null) ? (o1 == null && o2 == null) : o1.equals(o2);
    }

    private static boolean hasKey1(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        if (o.companyId == null) return false;
        return true;
    }

    private static boolean hasKey2(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        return true;
    }

    private static boolean hasKey3(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        if (o.sectionId == null) return false;
        return true;
    }

    private static boolean hasKey4(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        if (o.managerId == null) return false;
        return true;
    }

    private static boolean hasKey5(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        return true;
    }

    private static boolean hasKey6(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        if (o.issueId == null) return false;
        return true;
    }

    private static boolean hasKey7(info.motteke.annotation_mapper.typical.Flat1 o) {
        assert o != null;
        return true;
    }

    private Flat1Mapper() { }


}
