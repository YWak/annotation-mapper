package info.motteke.annotation_mapper.internal.desc;

import java.util.List;

public interface IMapper {

    public IType getSourceType();

    public IType getTargetType();

    public List<IField> getFields();
}
