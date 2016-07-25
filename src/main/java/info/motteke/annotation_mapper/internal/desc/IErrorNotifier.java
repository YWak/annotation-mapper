package info.motteke.annotation_mapper.internal.desc;

public interface IErrorNotifier {

    public void info(String message);

    public void warn(String message);

    public void error(String message);
}
