package info.motteke.annotation_mapper.internal.desc.impl;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class Message {

    private static final MessageHolder messages = new MessageHolder();

    private final Messager messager;

    public Message(Messager messager) {
        this.messager = messager;
    }

    public void compositeRequiresTargetOrValue(Element element, AnnotationMirror annotation) {
        String message = messages.get("COMPOSITE_REQUIRES_TARGET_OR_VALUE");
        mandatoryWarning(message, element, annotation);
    }

    public void compositeRequiresOneOfTargetOrValue(Element element, AnnotationMirror annotation) {
        String message = messages.get("COMPOSITE_REQUIRES_ONE_OF_TARGET_OR_VALUE");
        warning(message, element, annotation);
    }

    public void fieldWithNonGetterExecutable(Element element, AnnotationMirror annotation) {
        String message = messages.get("FIELD_WITH_NON_GETTER_EXECUTABLE");
        mandatoryWarning(message, element, annotation);
    }

    public void fieldWithoutTarget(Element element, AnnotationMirror annotation) {
        String message = messages.get("FIELD_WITHOUT_TARGET");
        mandatoryWarning(message, element, annotation);
    }

    public void fieldRequiresPropertyOrValue(Element element, AnnotationMirror annotation) {
        String message = messages.get("FIELD_REQUIRES_PROPERTY_OR_VALUE");
        mandatoryWarning(message, element, annotation);
    }

    private void warning(String message, Element element, AnnotationMirror annotation) {
        messager.printMessage(Kind.WARNING, message, element, annotation);
    }

    private void mandatoryWarning(String message, Element element, AnnotationMirror annotation) {
        messager.printMessage(Kind.MANDATORY_WARNING, message, element, annotation);
    }
}
