package info.motteke.annotation_mapper.typical;

import java.util.List;

public class Company {

    public Integer id;

    public String name;

    private User president;

    private List<Section> sections;

    public User getPresident() {
        return president;
    }

    public void setPresident(User president) {
        this.president = president;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
