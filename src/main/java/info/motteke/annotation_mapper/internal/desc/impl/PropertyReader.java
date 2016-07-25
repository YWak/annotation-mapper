package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.AccessModifier;
import info.motteke.annotation_mapper.internal.desc.Accessor;
import info.motteke.annotation_mapper.internal.desc.IPropertyReader;

public class PropertyReader extends AbstractPropertyAccessor implements IPropertyReader, Comparable<PropertyReader> {

    public PropertyReader(AccessModifier modifier, Accessor accessor, String name) {
        super(modifier, accessor, name);
    }

    @Override
    public int compareTo(PropertyReader o) {
        return super.compareTo(o);
    }
}
