package info.motteke.annotation_mapper.internal.desc;

public interface IPropertyReader {

    public AccessModifier getModifier();

    public Accessor getAccessor();

    public String getName();
}
