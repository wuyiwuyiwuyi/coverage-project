package www.pactera.com.coverage.project.common.BaseEntity;

import java.util.Arrays;

public class SourceTree {

    private String label;

    private SourceTree[] children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SourceTree[] getChildren() {
        return children;
    }

    public void setChildren(SourceTree[] children) {
        this.children = children;
    }

    public SourceTree(String label, SourceTree[] children) {
        this.label = label;
        this.children = children;
    }

    public SourceTree() {

    }

    @Override
    public String toString() {
        return "SourceTree{" +
                "label='" + label + '\'' +
                ", children=" + Arrays.toString(children) +
                '}';
    }
}
