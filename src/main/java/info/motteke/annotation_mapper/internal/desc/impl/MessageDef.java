package info.motteke.annotation_mapper.internal.desc.impl;

public class MessageDef {

    private final Level level;

    private final String pattern;

    public MessageDef(Level level, String pattern) {
        this.level = level;
        this.pattern = pattern;
    }

    public Level getLevel() {
        return level;
    }

    public String getPattern() {
        return pattern;
    }
}
