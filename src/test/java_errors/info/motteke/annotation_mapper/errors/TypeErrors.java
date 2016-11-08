package info.motteke.annotation_mapper.errors;

import info.motteke.annotation_mapper.Mapper;
import info.motteke.annotation_mapper.Mapper.Field;
import info.motteke.annotation_mapper.Mapper.Fields;

import java.util.List;

public class TypeErrors {

    @Mapper(target = To.class)
    public static class From {

        @Field(property = "a")
        public Long a;

        @Fields({
            @Field(property = "b"),
            @Field(property = "c.id")
        })
        public Integer b;
    }

    public static class To {

        public long a;

        public int b;

        public List<? extends Object> c;

    }
}
