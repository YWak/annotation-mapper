package info.motteke.annotation_mapper.internal.desc;

public interface IMessageFactory {

    public IMessage fieldWithNonGetterExecutable();

    public IMessage unreadableSource();

    public IMessage noSuchSetter(String className, String setterName);

    public IMessage noSuchGetter(String className, String getterName);

}
