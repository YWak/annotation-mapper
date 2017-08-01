package info.motteke.annotation_mapper.util;

public class MirrorOperationException extends RuntimeException {

    public MirrorOperationException() {
    }

    public MirrorOperationException(String message) {
        super(message);
    }

    public MirrorOperationException(Throwable cause) {
        super(cause);
    }

    public MirrorOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> void throwIfExists(Class<T> clazz) throws T {
        for (Throwable t = this; t != null; t = t.getCause()) {
            if (clazz.isInstance(t)) {
                throw (T) t;
            }
        }
    }
}
