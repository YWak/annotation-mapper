package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.utils.MultiValueMap;

import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

public class MultiPropertyMap extends MultiValueMap<IProperty, IProperty> {

    private static final PropertyComparator COMPARATOR = new PropertyComparator();

    public MultiPropertyMap() {
        super(new TreeMap<IProperty, Collection<IProperty>>(COMPARATOR));
    }

    @Override
    protected Collection<IProperty> newCollection() {
        return new TreeSet<IProperty>(COMPARATOR);
    }
}
