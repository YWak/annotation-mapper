package info.motteke.annotation_mapper.internal.utils.jsr269;

import info.motteke.annotation_mapper.internal.utils.AbstractBeanUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class BeanUtils {

    private static BeanUtils INSTANCE;

    public static void init(ProcessingEnvironment env) {
        INSTANCE = new BeanUtils(env);
    }

    public static boolean isGetter(ExecutableElement method) {
        return INSTANCE.utils.isGetter(method);
    }

    public static boolean isSetter(ExecutableElement method) {
        return INSTANCE.utils.isSetter(method);
    }

    public static String getName(ExecutableElement method) {
        return INSTANCE.utils.getName(method);
    }

    private final Types types;

    private final Elements elements;

    private final AbstractBeanUtils<ExecutableElement> utils = new AbstractBeanUtils<ExecutableElement>() {

        @Override
        protected boolean isStatic(ExecutableElement method) {
            return method.getModifiers().contains(Modifier.STATIC);
        }

        @Override
        protected boolean returnsVoid(ExecutableElement method) {
            return method.getReturnType().getKind().equals(TypeKind.VOID);
        }

        @Override
        protected boolean returnsBoolean(ExecutableElement method) {
            return method.getReturnType().accept(booleanType, null);
        }

        @Override
        protected int getParameterSize(ExecutableElement method) {
            return method.getParameters().size();
        }

        @Override
        protected String getMethodName(ExecutableElement method) {
            return method.getSimpleName().toString();
        }
    };

    private final TypeVisitorAdapter<Boolean, Object> booleanType = new TypeVisitorAdapter<Boolean, Object>(false) {

        @Override
        public Boolean visitPrimitive(PrimitiveType t, Object p) {
            return t.getKind().equals(TypeKind.BOOLEAN);
        }

        @Override
        public Boolean visitTypeVariable(TypeVariable t, Object p) {
            return types.isSameType(t.asElement().asType(), elements.getTypeElement(Boolean.class.getName()).asType());
        }
    };

    private BeanUtils(ProcessingEnvironment env) {
        this.types = env.getTypeUtils();
        this.elements = env.getElementUtils();
    }
}
