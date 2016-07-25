package info.motteke.annotation_mapper.internal.desc;

import java.util.List;

public interface IType {

    public String getName();

    public boolean isCollection();

    public boolean isList();

    public boolean isSet();

    public boolean isPrimitive();

    public boolean isArray();

    public boolean isConcrete();

    public IType getSubType();

    public List<String> getPropertyNames();

    public IProperty getProperty(String name);
}
