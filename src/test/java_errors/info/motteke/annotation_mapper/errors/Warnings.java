package info.motteke.annotation_mapper.errors;

import info.motteke.annotation_mapper.Mapper;
import info.motteke.annotation_mapper.Mapper.Field;
import info.motteke.annotation_mapper.typical.User;

@Mapper(target = User.class)
public class Warnings {

    @Field(property = "id")
    public void getTest() {
        return;
    }

    @Field(property = "id")
    public int get() {
        return 0;
    }

    @Field(property = "id2")
    public long getId() {
        return 0;
    }

    @Field(property = "id.id")
    public long getId2() {
        return 1;
    }
}
