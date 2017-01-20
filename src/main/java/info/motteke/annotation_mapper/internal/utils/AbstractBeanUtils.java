package info.motteke.annotation_mapper.internal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractBeanUtils<T> {

    private static final Pattern GETTER_NORMAL = Pattern.compile("get([A-Z])(.*)");

    private static final Pattern GETTER_BOOLEAN = Pattern.compile("is([A-Z])(.*)");

    private static final Pattern SETTER_NORMAL = Pattern.compile("set([A-Z])(.*)");

    public boolean isGetter(T method) {

        if (getParameterSize(method) != 0){
            return false;
        }
        if (returnsVoid(method)) {
            return false;
        }
        if (isStatic(method)) {
            return false;
        }

        String name = getMethodName(method);
        boolean isNormalGetter = (GETTER_NORMAL.matcher(name).matches());
        boolean isBooleanGetter = (returnsBoolean(method) && GETTER_BOOLEAN.matcher(name).matches());

        return isNormalGetter || isBooleanGetter;
    }

    public boolean isSetter(T method) {
        if (getParameterSize(method) != 1) {
            return false;
        }
        if (isStatic(method)) {
            return false;
        }

        if (!SETTER_NORMAL.matcher(getMethodName(method)).matches()) {
            return false;
        }

        if (!returnsVoid(method)) {
            return false;
        }

        return true;
    }

    public String getName(T method) {
        String methodName = getMethodName(method);
        final Matcher matcher;

        if (isSetter(method)) {
            matcher = SETTER_NORMAL.matcher(methodName);
            matcher.matches();
        } else if (isGetter(method)) {
            Matcher m;

            if ((m = GETTER_NORMAL.matcher(methodName)).matches()) {
                matcher = m;
            } else if ((m = GETTER_BOOLEAN.matcher(methodName)).matches()) {
                matcher = m;
            } else {
                return null;
            }
        } else {
            return null;
        }

        return matcher.group(1).toLowerCase() + matcher.group(2);
    }

    protected abstract boolean isStatic(T method);

    protected abstract int getParameterSize(T method);

    protected abstract String getMethodName(T method);

    protected abstract boolean returnsVoid(T method);

    protected abstract boolean returnsBoolean(T method);
}
