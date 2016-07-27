package info.motteke.annotation_mapper.internal.desc;

public interface IErrorNotifier {

    public void info(Object message);

    public void warn(Object message);

    public void error(Object message);
}
