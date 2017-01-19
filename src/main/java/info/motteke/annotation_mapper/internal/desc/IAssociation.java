package info.motteke.annotation_mapper.internal.desc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IAssociation {

    public String getName();

    public IType getType();

    public IType getBeanType();

    public Map<IProperty, Collection<IProperty>> getKeys();

    public Map<IProperty, Collection<IProperty>> getProperties();

    public List<IAssociation> getAssociations();

    public IAssociation getParent();

    public boolean hasError();
}
