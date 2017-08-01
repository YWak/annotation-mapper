package info.motteke.annotation_mapper.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * リフレクションを使って間接的にアクセスするためのラッパーです。
 *
 * @author YWak
 */
public class Mirror {

    /** アクセス対象のインスタンス。 */
    private final Object target;

    /** アクセス対象のクラス情報 */
    private final Class<?> clazz;

    /**
     * インスタンスを対象にしてリフレクションを実施するためのインスタンスを作ります。
     *
     * @param target リフレクションの対象インスタンス
     * @return ラッパー
     */
    public static Mirror ofInstance(Object target) {
        return new Mirror(target, target.getClass());
    }

    /**
     * スタティックメソッド/変数を対象にしてリフレクションを実施するためのインスタンスを作ります。
     *
     * @param clazz リフレクションの対象クラス
     * @return ラッパー
     */
    public static Mirror ofStatic(Class<?> clazz) {
        return new Mirror(null, clazz);
    }

    private Mirror(Object target, Class<?> clazz) {
        this.target = target;
        this.clazz = clazz;
    }

    /**
     * 変数から値を取得します。
     *
     * @param name 変数名
     * @return 変数の値
     * @throws MirrorOperationException 操作に失敗した場合
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) throws MirrorOperationException {
        try {
            return (T) getField(name).get(target);
        } catch (ReflectiveOperationException e) {
            throw new MirrorOperationException(e);
        }
    }

    /**
     * 変数に値を設定します。
     *
     * @param name 変数名
     * @param value 値
     * @throws MirrorOperationException 操作に失敗した場合
     */
    public void set(String name, Object value) throws MirrorOperationException {
        try {
            getField(name).set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new MirrorOperationException(e);
        }
    }

    /**
     * メソッドを実行し、結果を返します。
     * 対象のメソッドが可変長引数のみを引数として受け入れる場合は{@linkplain #callVarArgs(String, Object[])}を使用します。
     *
     * @param name メソッド名
     * @param args 引数
     * @return メソッドの実行結果
     * @throws MirrorOperationException 操作に失敗した場合
     */
    @SuppressWarnings("unchecked")
    public <T> T call(String name, Object... args) throws MirrorOperationException {
        try {
            Method method = find(name, args);
            method.setAccessible(true);

            return (T) method.invoke(target, args);
        } catch (ReflectiveOperationException e) {
            throw new MirrorOperationException(e);
        }
    }

    /**
     * 可変長引数のみを引数に持つメソッドを実行し、結果を返します。
     *
     * @param name メソッド名
     * @param args 引数
     * @return メソッドの実行結果
     * @throws MirrorOperationException 操作に失敗した場合
     */
    public <T> T callVarArgs(String name, Object[] args) throws MirrorOperationException {
        return call(name, new Object[]{ args });
    }

    /**
     * メソッドを検索して返します。
     *
     * @param name メソッド名
     * @param args 引数
     * @return メソッド
     * @throws ReflectiveOperationException メソッドが見つからない場合
     */
    private Method find(String name, Object[] args) throws ReflectiveOperationException {
        Method method = dig(clazz, c -> Arrays.asList(c.getDeclaredMethods()), m -> checkMethod(m, name, args));

        if (method == null) {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");

            for (Object o : args) {
                if (o instanceof String) {
                    joiner.add("'" + o + "'");
                } else {
                    joiner.add(String.valueOf(o));
                }
            }

            throw new NoSuchMethodException(name + joiner.toString());
        }

        return method;
    }

    /**
     * メソッドが指定したメソッド名と引数に一致するかを確認します。
     *
     * @param method メソッド
     * @param name メソッド名
     * @param args 引数
     * @return 対応するメソッド化どうか
     */
    private boolean checkMethod(Method method, String name, Object[] args) {
        if (!method.getName().equals(name)) {
            return false;
        }

        Class<?>[] paramTypes = method.getParameterTypes();

        if (paramTypes.length != args.length) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (!paramTypes[i].isInstance(args[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * フィールドを検索して返します。
     *
     * @param name フィールド名
     * @return 該当するフィールド
     * @throws ReflectiveOperationException
     */
    private Field getField(String name) throws ReflectiveOperationException {
        Field field = dig(clazz, c -> Arrays.asList(c.getDeclaredFields()), f -> f.getName().equals(name));

        if (field == null) {
            throw new NoSuchFieldException(name);
        }

        field.setAccessible(true);

        return field;
    }

    private <T> T dig(Class<?> clazz, Function<Class<?>, List<T>> expander, Predicate<T> matcher) {
        if (clazz.equals(Object.class)) {
            return null;
        }
        for (T t : expander.apply(clazz)) {
            if (matcher.test(t)) {
                return t;
            }
        }

        return dig(clazz.getSuperclass(), expander, matcher);
    }
}
