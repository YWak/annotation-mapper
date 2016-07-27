package info.motteke.annotation_mapper.internal.desc.impl;

import info.motteke.annotation_mapper.internal.desc.IMessageFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Message {

    private static final Map<String, String> messages;

    static {
        ResourceBundle resource = ResourceBundle.getBundle("messages");
        messages = new HashMap<String, String>();

        for (Enumeration<String> en = resource.getKeys(); en.hasMoreElements();) {
            String key = en.nextElement();
            messages.put(key, resource.getString(key));
        }
    }

    public static IMessageFactory get() {
        return (IMessageFactory) Proxy.newProxyInstance(Message.class.getClassLoader(), new Class<?>[]{ IMessageFactory.class }, new MessageHandler());
    }

    private static class MessageHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(IMessageFactory.class)) {
                return new MessageItem(messages.get(method.getName()), args);
            } else if (method.getName().equals("toString")) {
                return "MessageHolder";
            } else {
                return method.invoke(messages, args);
            }
        }
    }
}
