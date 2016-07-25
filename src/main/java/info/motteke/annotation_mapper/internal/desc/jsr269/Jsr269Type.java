package info.motteke.annotation_mapper.internal.desc.jsr269;

import static info.motteke.annotation_mapper.internal.utils.jsr269.BeanUtils.*;
import static info.motteke.annotation_mapper.internal.utils.jsr269.ElementUtils.*;
import static javax.lang.model.element.Modifier.*;
import info.motteke.annotation_mapper.internal.desc.AccessModifier;
import info.motteke.annotation_mapper.internal.desc.Accessor;
import info.motteke.annotation_mapper.internal.desc.IProperty;
import info.motteke.annotation_mapper.internal.desc.IType;
import info.motteke.annotation_mapper.internal.desc.impl.PropertyReader;
import info.motteke.annotation_mapper.internal.desc.impl.PropertyWriter;
import info.motteke.annotation_mapper.internal.utils.jsr269.AbstractElementConsumer;
import info.motteke.annotation_mapper.internal.utils.jsr269.BeanUtils;
import info.motteke.annotation_mapper.internal.utils.jsr269.TypeVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

public class Jsr269Type implements IType {

    private final Map<String, Jsr269Property> properties = new HashMap<String, Jsr269Property>();

    private final String name;

    private boolean primitive;

    private boolean collection;

    private boolean list;

    private boolean set;

    private boolean array;

    private boolean concrete;

    private Jsr269Type subType;

    public Jsr269Type(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isList() {
        return list;
    }

    @Override
    public boolean isSet() {
        return set;
    }

    @Override
    public boolean isPrimitive() {
        return primitive;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public boolean isConcrete() {
        return concrete;
    }

    @Override
    public boolean isCollection() {
        return collection;
    }

    @Override
    public IType getSubType() {
        return subType;
    }

    @Override
    public List<String> getPropertyNames() {
        return new ArrayList<String>(properties.keySet());
    }

    @Override
    public IProperty getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }

    public static class Jsr269TypeBuilder extends TypeVisitorAdapter<Jsr269Type, Void> {

        private final Map<String, Jsr269Type> cache = new HashMap<String, Jsr269Type>();

        @Override
        public Jsr269Type visitPrimitive(PrimitiveType t, Void p) {
            String name = t.getKind().name().toLowerCase();

            Jsr269Type type = cache.get(name);

            if (type == null) {
                type = new Jsr269Type(name);
                type.primitive = true;

                cache.put(name, type);
            }

            return type;
        }

        @Override
        public Jsr269Type visitDeclared(DeclaredType t, Void p) {
            TypeElement typeElement = toTypeElement(t.asElement());
            String name = typeElement.getQualifiedName().toString();

            Jsr269Type type = cache.get(name);

            if (type == null) {
                type = new Jsr269Type(name);
                cache.put(name, type);

                checkCollection(t, type);

                PropertyCollector collector = new PropertyCollector(this);

                for (Element e : typeElement.getEnclosedElements()) {
                    e.accept(collector, type);
                }
            }

            return type;
        }

        @Override
        public Jsr269Type visitArray(ArrayType t, Void p) {
            // TODO
            return super.visitArray(t, p);
        }

        private void checkCollection(DeclaredType t, Jsr269Type type) {
            TypeElement _type = toTypeElement(t.asElement());

            type.list = isList(_type);
            type.set = isSet(_type);
            type.collection = type.list || type.set;

            type.concrete = !(isInterface(_type.asType()) || isAbstract(_type.asType()));

            if (!type.collection) {
                return;
            }

            TypeMirror subType = getSubType(t);

            if (subType != null) {
                type.subType = subType.accept(this, null);
            }
        }

        private boolean isList(TypeElement type) {
            return type.getQualifiedName().toString().equals(List.class.getCanonicalName());
        }

        private boolean isSet(TypeElement type) {
            return type.getQualifiedName().toString().equals(Set.class.getCanonicalName());
        }

        private TypeMirror getSubType(DeclaredType t) {
            return t.getTypeArguments().get(0);
        }
    }

    private static class PropertyCollector extends AbstractElementConsumer<Jsr269Type> {

        private final Jsr269TypeBuilder builder;

        public PropertyCollector(Jsr269TypeBuilder builder) {
            this.builder = builder;
        }

        @Override
        protected void consumeExecutable(ExecutableElement e, Jsr269Type type) {

            String name = BeanUtils.getName(e);

            if (name == null) {
                return;
            }
            AccessModifier modifier = getModifier(e.getModifiers());

            if (!modifier.isAccessible()) {
                return;
            }

            Jsr269Property p = type.properties.get(name);

            String methodName = e.getSimpleName().toString();
            if (isGetter(e)) {
                if (p == null) {
                    p = new Jsr269Property(getGetterType(e), name);
                    type.properties.put(name, p);
                }
                p.updateReader(new PropertyReader(modifier, Accessor.METHOD, methodName));
            }
            if (isSetter(e)) {
                if (p == null) {
                    p = new Jsr269Property(getSetterType(e), name);
                    type.properties.put(name, p);
                }
                p.updateWriter(new PropertyWriter(modifier, Accessor.METHOD, methodName));
            }
        }

        private Jsr269Type getGetterType(ExecutableElement e) {
            return e.getReturnType().accept(builder, null);
        }

        private Jsr269Type getSetterType(ExecutableElement e) {
            return e.getParameters().get(0).asType().accept(builder, null);
        }

        @Override
        protected void consumeVariable(VariableElement e, Jsr269Type type) {

            String name = e.getSimpleName().toString();
            Set<Modifier> modifiers = e.getModifiers();
            AccessModifier modifier = getModifier(modifiers);

            if (modifiers.contains(STATIC)) {
                return;
            }

            Jsr269Property p = type.properties.get(name);

            if (p == null) {
                p = new Jsr269Property(getVariableType(e), name);
                type.properties.put(name, p);
            }

            if (modifier.isAccessible()) {
                p.updateReader(new PropertyReader(modifier, Accessor.FIELD, name));
            }
            if (modifier.isAccessible() && !modifiers.contains(FINAL)) {
                p.updateWriter(new PropertyWriter(modifier, Accessor.FIELD, name));
            }
        }

        private Jsr269Type getVariableType(VariableElement e) {
            return e.asType().accept(builder, null);
        }

        private AccessModifier getModifier(Set<Modifier> modifiers) {

            if (modifiers.contains(Modifier.PUBLIC)) {
                return AccessModifier.PUBLIC;
            }
            if (modifiers.contains(Modifier.PROTECTED)) {
                return AccessModifier.PROTECTED;
            }
            if (modifiers.contains(Modifier.PRIVATE)) {
                return AccessModifier.PRIVATE;
            }

            return AccessModifier.PACKAGE;
        }
    }
}
