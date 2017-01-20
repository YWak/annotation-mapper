package info.motteke.annotation_mapper.typical;

import info.motteke.annotation_mapper.Mapper;

@Mapper(target = Company.class)
public class Flat1 {

    @Mapper.Field(property = "id", id = true)
    public Integer companyId;

    @Mapper.Field(property = "name")
    public String companyName;

    private long presidentId;

    @Mapper.Field(property = "president.name")
    private String presidentName;

    @Mapper.Field(property = "sections.id", id = true)
    public Long sectionId;

    @Mapper.Field(property = "sections.name")
    public String sectionName;

    @Mapper.Field(property = "sections.manager.id", id = true)
    public Long managerId;

    @Mapper.Field(property = "sections.manager.name")
    public String managerName;

    @Mapper.Fields({
        @Mapper.Field(property = "sections.staffs.id", id = true),
        @Mapper.Field(property = "sections.staffs.issues.owner.id")
    })
    public long staffId;

    @Mapper.Fields({
        @Mapper.Field(property = "sections.staffs.name"),
        @Mapper.Field(property = "sections.staffs.issues.owner.name"),
    })
    public String staffName;

    @Mapper.Field(property = "sections.staffs.issues.id", id = true)
    public Long issueId;

    @Mapper.Field(property = "president.id")
    public long getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(long presidentId) {
        this.presidentId = presidentId;
    }

    public String getPresidentName() {
        return presidentName;
    }

    public void setPresidentName(String presidentName) {
        this.presidentName = presidentName;
    }

    @Mapper.Field(property = "sections.staffs.issues.title")
    public String getIssueTitle() {
        return "issue-" + issueId;
    }
}
