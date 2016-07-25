package info.motteke.annotation_mapper.internal.desc;

public interface IPropertyWriter {

    public AccessModifier getModifier();

    public Accessor getAccessor();

    public String getName();
}
