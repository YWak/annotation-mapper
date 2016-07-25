package info.motteke.annotation_mapper.internal.desc;


public interface IField extends IErrorNotifierHolder {

    public IAttribute<String> source();

    public IAttribute<Boolean> id();

    public IAttribute<String> property();
}
