package info.motteke.annotation_mapper.internal.desc;

public interface IProperty {

    public IType getType();

    public String getName();

    public IPropertyReader getReader();

    public IPropertyWriter getWriter();
}
