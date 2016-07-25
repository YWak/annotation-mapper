package info.motteke.annotation_mapper.internal.utils.jsr269;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public abstract class ElementVisitorAdapter<R, P> implements ElementVisitor<R, P> {

    private final R defaultValue;

    public ElementVisitorAdapter() {
        this(null);
    }

    public ElementVisitorAdapter(R defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public R visit(Element e, P p) {
        return defaultValue;
    }

    @Override
    public final R visit(Element e) {
        return visit(e, null);
    }

    @Override
    public R visitPackage(PackageElement e, P p) {
        return defaultValue;
    }

    @Override
    public R visitType(TypeElement e, P p) {
        return defaultValue;
    }

    @Override
    public R visitVariable(VariableElement e, P p) {
        return defaultValue;
    }

    @Override
    public R visitExecutable(ExecutableElement e, P p) {
        return defaultValue;
    }

    @Override
    public R visitTypeParameter(TypeParameterElement e, P p) {
        return defaultValue;
    }

    @Override
    public R visitUnknown(Element e, P p) {
        return defaultValue;
    }
}