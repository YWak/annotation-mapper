package info.motteke.annotation_mapper.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class RecursiveFieldMatcher<T extends Collection<?>> extends TypeSafeDiagnosingMatcher<T> {

    private final T expected;

    private RecursiveFieldMatcher(T expected) {
        this.expected = expected;
    }

    public static <T> RecursiveFieldMatcher<Collection<? extends T>> hasSameValues(Collection<? extends T> expected) {
        return new RecursiveFieldMatcher<Collection<? extends T>>(expected);
    }

    @Override
    public void describeTo(Description desc) {
        desc.appendText("same fields");
    }

    @Override
    protected boolean matchesSafely(T item, Description desc) {
        LinkedList<Object> stack = new LinkedList<Object>();
        return matches(expected, item, desc, stack, new HashMap<Integer, Object>());
    }

    private boolean matches(Object expected, Object actual, Description desc,
                            LinkedList<Object> stack, Map<Integer, Object> history) {
        if (expected == null && actual == null) {
            return true;
        }
        if (isPrimitive(expected) && isPrimitive(actual) && expected.equals(actual)) {
            return true;
        }

        Object old = history.put(System.identityHashCode(expected), actual);
        if (old != null && old == actual) {
            return true;
        }

        if (old == null && expected instanceof Collection && actual instanceof Collection) {
            return matchesCollection((Collection<?>) expected, (Collection<?>) actual, desc, stack, history);
        }
        if (old == null && expected != null && actual != null && expected.getClass().equals(actual.getClass())) {
            return matchesFields(expected, actual, desc, stack, history);
        }

        desc.appendText("\n");
        desc.appendText("expected");
        desc.appendText(toString(stack));
        desc.appendText(" is ");
        desc.appendValue(expected);
        desc.appendText(" but was ");
        desc.appendValue(actual);
        return false;
    }

    private boolean matchesCollection(Collection<?> expectedItems, Collection<?> actualItems,
                                      Description desc, LinkedList<Object> stack, Map<Integer, Object> history) {
        if (expectedItems.size() != actualItems.size()) {
            desc.appendText("\n");
            desc.appendText("expected");
            desc.appendText(toString(stack));
            desc.appendText(".size() == ");
            desc.appendValue(expectedItems.size());
            desc.appendText(". but actual.size() == " );
            desc.appendValue(actualItems.size());
            return false;
        }

        boolean matches = true;

        Iterator<?> expected = expectedItems.iterator();
        Iterator<?> actual = actualItems.iterator();

        int i = 0;

        while (expected.hasNext() && actual.hasNext()) {

            stack.addLast(i++);
            matches &= matches(expected.next(), actual.next(), desc, stack, history);
            stack.removeLast();
        }

        return matches;
    }

    private boolean matchesFields(Object expected, Object actual,
                                  Description desc, LinkedList<Object> stack, Map<Integer, Object> history) {
        boolean matches = true;

        for (Field field : getFields(expected)) {
            stack.addLast(field.getName());

            try {
                matches &= matches(field.get(expected), field.get(actual), desc, stack, history);
            } catch(ReflectiveOperationException e) {
                matches = false;
            }

            stack.removeLast();
        }

        return matches;
    }

    private boolean isPrimitive(Object o) {
        if (o instanceof String) {
            return true;
        }
        if (o instanceof Number) {
            return true;
        }
        if (o instanceof Boolean) {
            return true;
        }
        if (o instanceof Character) {
            return true;
        }

        return false;
    }

    private List<Field> getFields(Object o) {
        List<Field> fields = new ArrayList<Field>();

        for (Field f : o.getClass().getDeclaredFields()) {
            int mod = f.getModifiers();

            if (Modifier.isStatic(mod)) {
                continue;
            }
            if (Modifier.isFinal(mod)) {
                continue;
            }

            fields.add(f);
            f.setAccessible(true);
        }

        Collections.sort(fields, ORDER_BY_NAME);
        return fields;
    }

    private String toString(LinkedList<Object> stack) {
        StringBuilder name = new StringBuilder();
        Object[] array = stack.toArray();
        int i = 0;

        while (i < array.length) {
            Object value = array[i];

            if (value instanceof Integer) {
                name.append("[");
                name.append(value);
                name.append("]");
            } else {
                name.append(".");
                name.append(value);
            }
            i++;
        }

        return name.toString();
    }

    private static final Comparator<Field> ORDER_BY_NAME = new Comparator<Field>() {

        @Override
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
