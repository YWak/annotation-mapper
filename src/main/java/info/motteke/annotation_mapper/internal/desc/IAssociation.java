package info.motteke.annotation_mapper.internal.desc;

import java.util.List;
import java.util.Map;

public interface IAssociation {

    public String getName();

    public IType getType();

    public Map<IProperty, List<IProperty>> getKeys();

    public Map<IProperty, List<IProperty>> getProperties();

    public List<IAssociation> getAssociations();

    public IAssociation getParent();

    public boolean hasError();
}
