package info.motteke.annotation_mapper.internal.utils.jsr269;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ElementUtils {

    /**
     * {@code element}を{@code TypeElement}に変換します。
     *
     * @param element
     * @return
     */
    public static TypeElement toTypeElement(Element element) {
        return element.accept(TO_TYPE_ELEMENT, null);
    }

    public static TypeElement toTypeElement(TypeMirror typeMirror) {
        return typeMirror.accept(new TypeVisitorAdapter<TypeElement, Void>() {
            @Override
            public TypeElement visitDeclared(DeclaredType t, Void p) {
                return t.asElement().accept(TO_TYPE_ELEMENT, null);
            }
        }, null);
    }

    public static boolean isInterface(TypeMirror type) {
        return toTypeElement(type).getKind().isInterface();
    }

    public static boolean isAbstract(TypeMirror type) {
        return toTypeElement(type).getModifiers().contains(Modifier.ABSTRACT);
    }

    private static final ElementVisitor<TypeElement, Void> TO_TYPE_ELEMENT = new ElementVisitorAdapter<TypeElement, Void>() {
        public TypeElement visitType(TypeElement e, Void p) {
            return e;
        };
    };
}
