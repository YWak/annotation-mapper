package info.motteke.annotation_mapper.internal.utils.jsr269;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class AnnotationDescription {

    private final Map<String, IAnnotationValue> values;

    private final AnnotationMirror annotation;

    public AnnotationDescription(AnnotationMirror annotation, Elements utils) {
        this.values = initialize(annotation, utils);
        this.annotation = annotation;
    }

    public AnnotationMirror getAnnotation() {
        return annotation;
    }

    public IAnnotationValue getValue(String name) {
        return values.get(name);
    }

    private Map<String, IAnnotationValue> initialize(AnnotationMirror annotation, Elements utils) {

        Map<String, IAnnotationValue> values = new HashMap<String, IAnnotationValue>();
        Map<? extends ExecutableElement, ? extends AnnotationValue> allValues = utils.getElementValuesWithDefaults(annotation);

        for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : allValues.entrySet()) {
            String name = entry.getKey().getSimpleName().toString();

            BasicAnnotationValue value = new BasicAnnotationValue(name, entry.getValue(), utils);
            values.put(name, value);
        }

        return values;
    }

    @SuppressWarnings("unused")
    private static class BasicAnnotationValue implements IAnnotationValue {

        private final String name;

        private final AnnotationValue original;

        private Boolean booleanValue;

        private List<Boolean> arrayOfBooleanValue;

        private Character characterValue;

        private List<Character> arrayOfCharacterValue;

        private Byte byteValue;

        private List<Byte> arrayOfByteValue;

        private Short shortValue;

        private List<Short> arrayOfShortValue;

        private Integer integerValue;

        private List<Integer> arrayOfIntegerValue;

        private Long longValue;

        private List<Long> arrayOfLongValue;

        private Float floatValue;

        private List<Float> arrayOfFloatValue;

        private Double doubleValue;

        private List<Double> arrayOfDoubleValue;

        private String stringValue;

        private List<String> arrayOfStringValue;

        private TypeElement classValue;

        private List<TypeElement> arrayOfClassValue;

        private AnnotationDescription annotationValue;

        private List<AnnotationDescription> arrayOfAnnotationValue;

        public BasicAnnotationValue(String name, AnnotationValue value, Elements utils) {
            this.name = name;
            this.original = value;
            value.accept(new ValueCollector(utils), this);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public AnnotationValue get() {
            return original;
        }

        @Override
        public Boolean getBooleanValue() {
            return notNull(booleanValue, "boolean");
        }

        @Override
        public String getStringValue() {
            return notNull(stringValue, "String");
        }

        @Override
        public TypeElement getClassValue() {
            return notNull(classValue, "Class");
        }

        @Override
        public List<AnnotationDescription> getArrayOfAnnotationValue() {
            return notNull(arrayOfAnnotationValue, "Annotation[]");
        }

        private <T> T notNull(T value, String type) {
            if (value == null) {
                throw new UnsupportedOperationException(name + " is not " + type);
            }

            return value;
        }
    }

    private static class ValueCollector implements AnnotationValueVisitor<Object, AnnotationDescription.BasicAnnotationValue> {

        private final Elements utils;

        private ValueCollector(Elements utils) {
            this.utils = utils;
        }

        @Override
        public Object visitUnknown(AnnotationValue av, BasicAnnotationValue p) {
            return null;
        }

        @Override
        public Object visitType(TypeMirror t, BasicAnnotationValue p) {
            p.classValue = ElementUtils.toTypeElement(t);
            return null;
        }

        @Override
        public Object visitString(String s, BasicAnnotationValue p) {
            p.stringValue = s;
            return null;
        }

        @Override
        public Object visitShort(short s, BasicAnnotationValue p) {
            p.shortValue = s;
            return null;
        }

        @Override
        public Object visitLong(long i, BasicAnnotationValue p) {
            p.longValue = i;
            return null;
        }

        @Override
        public Object visitInt(int i, BasicAnnotationValue p) {
            p.integerValue = i;
            return null;
        }

        @Override
        public Object visitFloat(float f, BasicAnnotationValue p) {
            p.floatValue = f;
            return null;
        }

        @Override
        public Object visitEnumConstant(VariableElement c, BasicAnnotationValue p) {
            // TODO 必要になったら
            return null;
        }

        @Override
        public Object visitDouble(double d, BasicAnnotationValue p) {
            p.doubleValue = d;
            return null;
        }

        @Override
        public Object visitChar(char c, BasicAnnotationValue p) {
            p.characterValue = c;
            return null;
        }

        @Override
        public Object visitByte(byte b, BasicAnnotationValue p) {
            p.byteValue = b;
            return null;
        }

        @Override
        public Object visitBoolean(boolean b, BasicAnnotationValue p) {
            p.booleanValue = b;
            return null;
        }

        @Override
        public Object visitArray(List<? extends AnnotationValue> vals, BasicAnnotationValue p) {
            for (AnnotationValue v : vals) {
                v.accept(new ArrayValueCollector(utils), p);
            }
            return null;
        }

        @Override
        public Object visitAnnotation(AnnotationMirror a, BasicAnnotationValue p) {
            p.annotationValue = new AnnotationDescription(a, utils);
            return null;
        }

        @Override
        public Object visit(AnnotationValue av, BasicAnnotationValue p) {
            return null;
        }

        @Override
        public Object visit(AnnotationValue av) {
            return null;
        }
    }

    private static class ArrayValueCollector implements AnnotationValueVisitor<Object, AnnotationDescription.BasicAnnotationValue> {

        private final Elements utils;

        private ArrayValueCollector(Elements utils) {
            this.utils = utils;
        }

        @Override
        public Object visitUnknown(AnnotationValue av, BasicAnnotationValue p) {
            return null;
        }

        @Override
        public Object visitType(TypeMirror t, BasicAnnotationValue p) {
            p.arrayOfClassValue = add(p.arrayOfClassValue, ElementUtils.toTypeElement(t));
            return null;
        }

        @Override
        public Object visitString(String s, BasicAnnotationValue p) {
            p.arrayOfStringValue = add(p.arrayOfStringValue, s);
            return null;
        }

        @Override
        public Object visitShort(short s, BasicAnnotationValue p) {
            p.arrayOfShortValue = add(p.arrayOfShortValue, s);
            return null;
        }

        @Override
        public Object visitLong(long i, BasicAnnotationValue p) {
            p.arrayOfLongValue = add(p.arrayOfLongValue, i);
            return null;
        }

        @Override
        public Object visitInt(int i, BasicAnnotationValue p) {
            p.arrayOfIntegerValue = add(p.arrayOfIntegerValue, i);
            return null;
        }

        @Override
        public Object visitFloat(float f, BasicAnnotationValue p) {
            p.arrayOfFloatValue = add(p.arrayOfFloatValue, f);
            return null;
        }

        @Override
        public Object visitEnumConstant(VariableElement c, BasicAnnotationValue p) {
            // TODO 必要になったら
            return null;
        }

        @Override
        public Object visitDouble(double d, BasicAnnotationValue p) {
            p.arrayOfDoubleValue = add(p.arrayOfDoubleValue, d);
            return null;
        }

        @Override
        public Object visitChar(char c, BasicAnnotationValue p) {
            p.arrayOfCharacterValue = add(p.arrayOfCharacterValue, c);
            return null;
        }

        @Override
        public Object visitByte(byte b, BasicAnnotationValue p) {
            p.arrayOfByteValue = add(p.arrayOfByteValue, b);
            return null;
        }

        @Override
        public Object visitBoolean(boolean b, BasicAnnotationValue p) {
            p.arrayOfBooleanValue = add(p.arrayOfBooleanValue, b);
            return null;
        }

        @Override
        public Object visitArray(List<? extends AnnotationValue> vals, BasicAnnotationValue p) {
            throw new InternalError();
        }

        @Override
        public Object visitAnnotation(AnnotationMirror a, BasicAnnotationValue p) {
            p.arrayOfAnnotationValue = add(p.arrayOfAnnotationValue, new AnnotationDescription(a, utils));
            return null;
        }

        @Override
        public Object visit(AnnotationValue av, BasicAnnotationValue p) {
            return null;
        }

        @Override
        public Object visit(AnnotationValue av) {
            return null;
        }

        private <T> List<T> add(List<T> list, T value) {
            if (list == null) {
                list = new ArrayList<T>();
            }

            list.add(value);
            return list;
        }
    }
}
