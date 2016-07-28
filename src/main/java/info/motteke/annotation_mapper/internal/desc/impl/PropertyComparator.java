package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.IProperty;

import java.util.Comparator;

public class PropertyComparator implements Comparator<IProperty> {
    @Override
    public int compare(IProperty o1, IProperty o2) {
        String n1 = o1.getName();
        String n2 = o2.getName();

        if (n1 != null && n2 != null) {
            return n1.compareTo(n2);
        } else if (n1 != null) {
            return -1;
        } else if (n2 != null) {
            return +1;
        } else {
            return 0;
        }
    }
}