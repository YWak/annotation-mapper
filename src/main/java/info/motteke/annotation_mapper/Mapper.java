package info.motteke.annotation_mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author YWak
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Mapper {
    /**
     * a root class of composite.
     *
     * @return
     */
    public Class<?> target() default Object.class;

    /**
     *
     * @author YWak
     */
    @Target({ ElementType.FIELD, ElementType.METHOD })
    @Retention(RetentionPolicy.SOURCE)
    @Repeatable(Fields.class)
    public static @interface Field {

        /**
         *
         * @return
         */
        public boolean id() default false;

        /**
         *
         * @return
         */
        public String property();
    }

    /**
     *
     * @author YWak
     */
    @Target({ ElementType.FIELD, ElementType.METHOD })
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Fields {

        /**
         *
         * @return
         */
        public Field[] value();
    }
}
