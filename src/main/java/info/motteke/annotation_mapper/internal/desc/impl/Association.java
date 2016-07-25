package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.IAssociation;
import info.motteke.annotation_mapper.internal.desc.IField;
import info.motteke.annotation_mapper.internal.desc.IMapper;
import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.desc.IType;
import info.motteke.annotation_mapper.internal.utils.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Association implements IAssociation {

    private final IType type;

    private final String name;

    private final Map<IProperty, List<IProperty>> keys = new MultiValueMap<IProperty, IProperty>();

    private final Map<IProperty, List<IProperty>> properties = new MultiValueMap<IProperty, IProperty>();

    private final List<Association> associations = new ArrayList<Association>();

    private final Association parent;

    public Association(IMapper mapper) {
        this(mapper.getTargetType(), "", null);

        IType sourceType = mapper.getSourceType();

        for (IField field : mapper.getFields()) {
            IProperty source = sourceType.getProperty(field.source().get());
            String path = field.property().get();

            append(this, source, field, path.split("\\."), 0);
        }
    }

    protected Association(IType type, String name, Association parent) {
        if (type == null) {
            throw new IllegalArgumentException("type can't be null " + name);
        }
        this.type = type;
        this.name = name;
        this.parent = parent;
    }

    private void append(Association association, IProperty source, IField field, String[] paths, int i) {
        String name = paths[i];

        if (i == paths.length - 1) {
            IProperty target = getProperty(association.type, name);

            if (target == null) {
                field.getNotifier().error("そんなフィールドはない");
                return;
            }

            association.properties.get(source).add(target);

            if (field.id().get()) {
                association.keys.get(source).add(target);
            }

            return;
        }

        Association next = association.getChild(name);

        if (next == null) {
            field.getNotifier().error("そんなフィールドはない");
            return;
        }

        append(next, source, field, paths, i + 1);
    }

    private Association getChild(String name) {
        for (Association a : associations) {
            if (a.name.equals(name)) {
                return a;
            }
        }

        IProperty property = getProperty(type, name);

        if (property == null) {
            return null;
        }

        Association a = new Association(property.getType(), name, this);
        this.associations.add(a);

        return a;
    }

    private IProperty getProperty(IType type, String name) {
        if (type.getSubType() == null) {
            return type.getProperty(name);
        } else {
            return type.getSubType().getProperty(name);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IType getType() {
        return type;
    }

    @Override
    public Map<IProperty, List<IProperty>> getKeys() {
        return new HashMap<IProperty, List<IProperty>>(keys);
    }

    @Override
    public Map<IProperty, List<IProperty>> getProperties() {
        return new HashMap<IProperty, List<IProperty>>(properties);
    }

    @Override
    public List<IAssociation> getAssociations() {
        return new ArrayList<IAssociation>(associations);
    }

    @Override
    public IAssociation getParent() {
        return parent;
    }
}
