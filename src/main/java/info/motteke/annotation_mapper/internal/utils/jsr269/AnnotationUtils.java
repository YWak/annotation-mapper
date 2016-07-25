package info.motteke.annotation_mapper.internal.utils.jsr269;

import java.lang.annotation.Annotation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

public class AnnotationUtils {

    public static AnnotationMirror getAnnotationMirror(Elements utils, Element element, Class<? extends Annotation> clazz) {
        Element expected = utils.getTypeElement(clazz.getCanonicalName());

        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            Element type = annotation.getAnnotationType().asElement();

            if (type.equals(expected)) {
                return annotation;
            }
        }

        return null;
    }
}
