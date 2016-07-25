package info.motteke.annotation_mapper.internal.desc.impl;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MessageHolder {

    private static final Map<String, String> messages;

    static {
        ResourceBundle resource = ResourceBundle.getBundle("messages");
        messages = new HashMap<String, String>();

        for (Enumeration<String> en = resource.getKeys(); en.hasMoreElements();) {
            String key = en.nextElement();

            messages.put(key, resource.getString(key));
        }
    }

    public String get(String key, Object... args) {
        String pattern = messages.get(key);

        if (pattern == null) {
            throw new IllegalArgumentException("message key [" + key + "] is not defined");
        }
        return MessageFormat.format(pattern, args);
    }
}