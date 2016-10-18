package info.motteke.annotation_mapper.internal.build;

import info.motteke.annotation_mapper.internal.desc.IAssociation;

import java.util.Comparator;

public class AssociationComparator implements Comparator<IAssociation> {

    @Override
    public int compare(IAssociation o1, IAssociation o2) {
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
