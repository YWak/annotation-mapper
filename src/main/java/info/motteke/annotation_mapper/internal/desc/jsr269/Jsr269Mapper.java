package info.motteke.annotation_mapper.internal.desc.jsr269;

import static info.motteke.annotation_mapper.internal.utils.jsr269.AnnotationUtils.*;
import static info.motteke.annotation_mapper.internal.utils.jsr269.ElementUtils.*;
import info.motteke.annotation_mapper.Mapper.Field;
import info.motteke.annotation_mapper.Mapper.Fields;
import info.motteke.annotation_mapper.internal.desc.IField;
import info.motteke.annotation_mapper.internal.desc.IMapper;
import info.motteke.annotation_mapper.internal.desc.IType;
import info.motteke.annotation_mapper.internal.desc.jsr269.Jsr269Type.Jsr269TypeBuilder;
import info.motteke.annotation_mapper.internal.utils.jsr269.AnnotationDescription;
import info.motteke.annotation_mapper.internal.utils.jsr269.ElementVisitorAdapter;
import info.motteke.annotation_mapper.internal.utils.jsr269.IAnnotationValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class Jsr269Mapper implements IMapper {

    private final Jsr269Type source;

    private final Jsr269Type target;

    private final List<IField> fields;

    public Jsr269Mapper(ProcessingEnvironment env, Element source) {
        AnnotationMirror mapperAnnotation = getMapperAnnotation(source, env.getElementUtils());
        AnnotationDescription mapper = new AnnotationDescription(mapperAnnotation, env.getElementUtils());
        IAnnotationValue target = mapper.getValue("target");

        Jsr269TypeBuilder builder = new Jsr269TypeBuilder();
        this.source = toTypeElement(source).asType().accept(builder, null);
        this.target = target.getClassValue().asType().accept(builder, null);
        this.fields = collectFields(env, source);
    }

    @Override
    public IType getSourceType() {
        return source;
    }

    @Override
    public IType getTargetType() {
        return target;
    }

    @Override
    public List<IField> getFields() {
        return new ArrayList<IField>(fields);
    }

    private AnnotationMirror getMapperAnnotation(Element element, Elements utils) {
        return getAnnotationMirror(utils, element, info.motteke.annotation_mapper.Mapper.class);
    }

    private List<IField> collectFields(ProcessingEnvironment env, Element source) {
        List<IField> fields = new ArrayList<IField>();

        for (Element element : source.getEnclosedElements()) {
            fields.addAll(element.accept(FieldCollector.INSTANCE, env));
        }

        return fields;
    }

    private static class FieldCollector extends ElementVisitorAdapter<List<IField>, ProcessingEnvironment> {

        private static final FieldCollector INSTANCE = new FieldCollector();

        private FieldCollector() {
            super(Collections.<IField>emptyList());
        }

        @Override
        public List<IField> visitExecutable(ExecutableElement e, ProcessingEnvironment env) {
            return getFields(e, env);
        }

        @Override
        public List<IField> visitVariable(VariableElement e, ProcessingEnvironment env) {
            return getFields(e, env);
        }

        private List<IField> getFields(Element element, ProcessingEnvironment env) {
            Elements utils = env.getElementUtils();
            List<IField> found = new ArrayList<IField>();

            AnnotationMirror fieldAnnotation = getAnnotationMirror(utils, element, Field.class);

            if (fieldAnnotation != null) {
                found.add(new Jsr269Field(env, element, fieldAnnotation, fieldAnnotation));
            }

            AnnotationMirror fieldsAnnotation = getAnnotationMirror(utils, element, Fields.class);

            if (fieldsAnnotation != null) {
                AnnotationDescription fields = new AnnotationDescription(fieldsAnnotation, env.getElementUtils());

                for (AnnotationDescription field : fields.getValue("value").getArrayOfAnnotationValue()) {
                    found.add(new Jsr269Field(env, element, fieldsAnnotation, field.getAnnotation()));
                }
            }

            return found;
        }
    }
}
