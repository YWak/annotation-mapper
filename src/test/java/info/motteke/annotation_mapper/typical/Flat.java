package info.motteke.annotation_mapper.typical;

import info.motteke.annotation_mapper.Mapper;
import info.motteke.annotation_mapper.Mapper.Field;
import info.motteke.annotation_mapper.Mapper.Fields;

@Mapper(target = User.class)
public class Flat {

    @Fields({
        @Field(property = "id", id = true),
        @Field(property = "projects.owner.id", id = true),
    })
    public Long userId;

    private String userName;

    @Mapper.Field(property = "projects.id", id = true)
    public Long projectId;

    private String projectName;

    @Fields({
        @Field(property = "name"),
        @Field(property = "projects.owner.name"),
    })
    public String getUserName() {
        return userName;
    }

    @Mapper.Field(property = "projects.name")
    public String getProjectName() {
        return projectName;
    }
}
