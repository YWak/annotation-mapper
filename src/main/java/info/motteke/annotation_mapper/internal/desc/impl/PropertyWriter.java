package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.AccessModifier;
import info.motteke.annotation_mapper.internal.desc.Accessor;
import info.motteke.annotation_mapper.internal.desc.IPropertyWriter;

public class PropertyWriter extends AbstractPropertyAccessor implements IPropertyWriter, Comparable<PropertyWriter> {

    public PropertyWriter(AccessModifier modifier, Accessor accessor, String name) {
        super(modifier, accessor, name);
    }

    @Override
    public int compareTo(PropertyWriter o) {
        return super.compareTo(o);
    }
}
