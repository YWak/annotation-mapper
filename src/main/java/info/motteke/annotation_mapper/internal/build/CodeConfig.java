package info.motteke.annotation_mapper.internal.build;

public class CodeConfig {

    public int getCountStart() {
        return 1;
    }

    public String getCompareMethodName(int n) {
        return "equals" + n;
    }

    public String getObjectVariableName(int n) {
        return "obj" + n;
    }

    public String getCollectionVariableName(int n) {
        return "c" + n;
    }

    public String getComparisonVariableName(int n) {
        return "_equals" + n;
    }
}
