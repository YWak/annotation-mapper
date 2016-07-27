package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.IMessage;

import java.text.FieldPosition;
import java.text.MessageFormat;

public class MessageItem implements IMessage {

    private static final Object[] EMPTY = new Object[0];

    private final String pattern;

    private final Object[] args;

    public MessageItem(String pattern, Object[] args) {
        this.pattern = pattern;

        if (args == null) {
            this.args = EMPTY;
        } else {
            this.args = new Object[args.length];
            System.arraycopy(args, 0, this.args, 0, args.length);
        }
    }

    @Override
    public String toString() {
        return new MessageFormat(pattern).format(args, new StringBuffer(), new FieldPosition(0)).toString();
    }
}
