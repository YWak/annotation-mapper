package info.motteke.annotation_mapper.internal.desc.jsr269;

import info.motteke.annotation_mapper.internal.desc.impl.Message;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Context {

    private static final ThreadLocal<Context> INSTANCES = new ThreadLocal<Context>();

    private final ProcessingEnvironment processingEnv;

    private final Message message;

    public static Context get() {
        return INSTANCES.get();
    }

    public Context(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.message = new Message(processingEnv.getMessager());

        INSTANCES.set(this);
    }

    public Message getMessage() {
        return message;
    }

    public Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    public Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }
}
