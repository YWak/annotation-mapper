package info.motteke.annotation_mapper.internal.desc;

public enum AccessModifier {

    PRIVATE(false),

    PACKAGE(true),

    PROTECTED(true),

    PUBLIC(true),

    ;

    private final boolean accessible;

    private AccessModifier(boolean accessible) {
        this.accessible = accessible;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
