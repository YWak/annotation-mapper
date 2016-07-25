package info.motteke.annotation_mapper.internal.utils.jsr269;

import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;

public interface IAnnotationValue {

    public String getName();

    public AnnotationValue get();

    public Boolean getBooleanValue();

    public String getStringValue();

    public TypeElement getClassValue();

    public List<AnnotationDescription> getArrayOfAnnotationValue();
}
