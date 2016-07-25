package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.AccessModifier;
import info.motteke.annotation_mapper.internal.desc.Accessor;

public abstract class AbstractPropertyAccessor {

    private final AccessModifier modifier;

    private final Accessor accessor;

    private final String name;

    protected AbstractPropertyAccessor(AccessModifier modifier, Accessor accessor, String name) {
        this.modifier = modifier;
        this.accessor = accessor;
        this.name = name;
    }

    public AccessModifier getModifier() {
        return modifier;
    }

    public Accessor getAccessor() {
        return accessor;
    }

    public String getName() {
        return name;
    }

    protected int compareTo(AbstractPropertyAccessor o) {
        int c;

        if ((c = compare(this.modifier, o.modifier)) != 0) {
            return c;
        }
        if ((c = compare(this.accessor, o.accessor)) != 0) {
            return c;
        }

        return 0;
    }

    protected <T> int compare(Comparable<T> c1, T c2) {
        if (c1 != null && c2 != null) {
            return c1.compareTo(c2);
        }

        if (c1 == null && c2 == null) {
            return 0;
        } else if (c1 == null) {
            return +1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return modifier.toString().toLowerCase() + " " + accessor.toString().toLowerCase();
    }
}
