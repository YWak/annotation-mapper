package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.IAttribute;
import info.motteke.annotation_mapper.internal.desc.IErrorNotifier;

public class Attribute<T> implements IAttribute<T> {

    private final T value;

    private final IErrorNotifier notifier;

    public Attribute(T value, IErrorNotifier notifier) {
        this.value = value;
        this.notifier = notifier;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public IErrorNotifier getNotifier() {
        return notifier;
    }
}
