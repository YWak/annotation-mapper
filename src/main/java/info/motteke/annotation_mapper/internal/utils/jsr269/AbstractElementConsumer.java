package info.motteke.annotation_mapper.internal.utils.jsr269;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public abstract class AbstractElementConsumer<P> extends ElementVisitorAdapter<Void, P> {

    protected void consume(Element e, P p) {
    }

    protected void consumePackage(PackageElement e, P p) {
    }

    protected void consumeType(TypeElement e, P p) {
    }

    protected void consumeVariable(VariableElement e, P p) {
    }

    protected void consumeExecutable(ExecutableElement e, P p) {
    }

    protected void consumeTypeParameter(TypeParameterElement e, P p) {
    }

    protected void consumeUnknown(Element e, P p) {
    }

    @Override
    public final Void visit(Element e, P p) {
        consume(e, p);
        return null;
    }

    @Override
    public final Void visitPackage(PackageElement e, P p) {
        consumePackage(e, p);
        return null;
    }

    @Override
    public final Void visitType(TypeElement e, P p) {
        consumeType(e, p);
        return null;
    }

    @Override
    public final Void visitVariable(VariableElement e, P p) {
        consumeVariable(e, p);
        return null;
    }

    @Override
    public final Void visitExecutable(ExecutableElement e, P p) {
        consumeExecutable(e, p);
        return null;
    }

    @Override
    public final Void visitTypeParameter(TypeParameterElement e, P p) {
        consumeTypeParameter(e, p);
        return null;
    }

    @Override
    public final Void visitUnknown(Element e, P p) {
        consumeUnknown(e, p);
        return null;
    }
}