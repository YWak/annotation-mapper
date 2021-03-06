package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.IErrorNotifier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class AnnotationAttributeErrorNotifier implements IErrorNotifier {

    private final ProcessingEnvironment env;

    private final Element element;

    private final AnnotationMirror annotation;

    private final AnnotationValue value;

    public AnnotationAttributeErrorNotifier(ProcessingEnvironment env, Element element, AnnotationMirror annotation,
            AnnotationValue value) {
        this.env = env;
        this.element = element;
        this.annotation = annotation;
        this.value = value;
    }

    @Override
    public void info(Object message) {
        print(Kind.NOTE, message);
    }

    @Override
    public void warn(Object message) {
        print(Kind.WARNING, message);
    }

    @Override
    public void error(Object message) {
        print(Kind.ERROR, message);
    }

    private void print(Kind kind, Object message) {
        env.getMessager().printMessage(kind, message.toString(), element, annotation, value);
    }
}
