package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.IAttribute;
import info.motteke.annotation_mapper.internal.desc.IErrorNotifier;
import info.motteke.annotation_mapper.internal.desc.IField;
import info.motteke.annotation_mapper.internal.utils.jsr269.AnnotationDescription;
import info.motteke.annotation_mapper.internal.utils.jsr269.BeanUtils;
import info.motteke.annotation_mapper.internal.utils.jsr269.ElementVisitorAdapter;
import info.motteke.annotation_mapper.internal.utils.jsr269.IAnnotationValue;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class Jsr269Field implements IField {

    private final IErrorNotifier notifier;

    private final Attribute<String> source;

    private final Attribute<Boolean> id;

    private final Attribute<String> property;

    public Jsr269Field(ProcessingEnvironment env, Element source, AnnotationMirror base, AnnotationMirror annotation) {
        AnnotationDescription field = new AnnotationDescription(annotation, env.getElementUtils());
        IAnnotationValue id = field.getValue("id");
        IAnnotationValue property = field.getValue("property");

        this.notifier = new AnnotationErrorNotifier(env, source, base);
        this.source = new Attribute<String>(source.accept(SourceNameCollector.INSTANCE, null), notifier);
        this.id = new Attribute<Boolean>(id.getBooleanValue(),
                                         new AnnotationAttributeErrorNotifier(env,
                                                                              source,
                                                                              base,
                                                                              id.get()));
        this.property = new Attribute<String>(property.getStringValue(),
                                              new AnnotationAttributeErrorNotifier(env,
                                                                                   source,
                                                                                   annotation,
                                                                                   property.get()));
    }

    @Override
    public IAttribute<String> source() {
        return source;
    }

    @Override
    public IAttribute<Boolean> id() {
        return id;
    }

    @Override
    public IAttribute<String> property() {
        return property;
    }

    @Override
    public IErrorNotifier getNotifier() {
        return notifier;
    }

    private static class SourceNameCollector extends ElementVisitorAdapter<String, Void> {

        private static final SourceNameCollector INSTANCE = new SourceNameCollector();

        @Override
        public String visitExecutable(ExecutableElement e, Void p) {
            return BeanUtils.getName(e);
        }

        @Override
        public String visitVariable(VariableElement e, Void p) {
            return e.getSimpleName().toString();
        }
    }
}
