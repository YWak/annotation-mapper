package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.IErrorNotifier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class AnnotationErrorNotifier implements IErrorNotifier {

    private final ProcessingEnvironment env;

    private final Element element;

    private final AnnotationMirror annotation;

    public AnnotationErrorNotifier(ProcessingEnvironment env, Element element, AnnotationMirror annotation) {
        this.env = env;
        this.element = element;
        this.annotation = annotation;
    }

    @Override
    public void info(String message) {
        print(Kind.NOTE, message);
    }

    @Override
    public void warn(String message) {
        print(Kind.WARNING, message);
    }

    @Override
    public void error(String message) {
        print(Kind.ERROR, message);
    }

    private void print(Kind kind, String message) {
        env.getMessager().printMessage(kind, message, element, annotation);
    }
}