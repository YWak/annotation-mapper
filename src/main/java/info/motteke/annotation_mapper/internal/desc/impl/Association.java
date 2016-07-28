package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.build.AssociationComparator;
import info.motteke.annotation_mapper.internal.desc.IAssociation;
import info.motteke.annotation_mapper.internal.desc.IField;
import info.motteke.annotation_mapper.internal.desc.IMapper;
import info.motteke.annotation_mapper.internal.desc.IMessage;
import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.desc.IType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Association implements IAssociation {

    private final IType type;

    private final String name;

    private final Map<IProperty, Collection<IProperty>> keys = new MultiPropertyMap();

    private final Map<IProperty, Collection<IProperty>> properties = new MultiPropertyMap();

    private final Set<Association> associations = new TreeSet<Association>(new AssociationComparator());

    private final Association parent;

    private boolean error;

    public Association(IMapper mapper) {
        this(mapper.getTargetType(), "", null);

        IType sourceType = mapper.getSourceType();

        for (IField field : mapper.getFields()) {
            String sourceName = field.source().get();

            if (sourceName == null) {
                IMessage message = Message.get().unreadableSource();
                field.getNotifier().error(message);
                error = true;

                continue;
            }

            IProperty source = sourceType.getProperty(sourceName);
            if (source == null) {
                IMessage message = Message.get().noSuchGetter(sourceType.getName(), sourceName);
                field.getNotifier().error(message);
                error = true;

                continue;
            }

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
                IMessage message = Message.get().noSuchSetter(association.type.getName(), name);
                field.getNotifier().error(message);
                error = true;
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
            IMessage message = Message.get().noSuchGetter(association.type.getName(), name);
            field.getNotifier().error(message);
            error = true;
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
    public Map<IProperty, Collection<IProperty>> getKeys() {
        Map<IProperty, Collection<IProperty>> keys = new LinkedHashMap<IProperty, Collection<IProperty>>();
        keys.putAll(this.keys);

        return keys;
    }

    @Override
    public Map<IProperty, Collection<IProperty>> getProperties() {
        Map<IProperty, Collection<IProperty>> properties = new LinkedHashMap<IProperty, Collection<IProperty>>();
        properties.putAll(this.properties);

        return properties;
    }

    @Override
    public List<IAssociation> getAssociations() {
        return new ArrayList<IAssociation>(associations);
    }

    @Override
    public IAssociation getParent() {
        return parent;
    }

    @Override
    public boolean hasError() {
        if (this.error) {
            return true;
        }

        for (IAssociation child : associations) {
            if (child.hasError()) {
                return true;
            }
        }

        return false;
    }
}
