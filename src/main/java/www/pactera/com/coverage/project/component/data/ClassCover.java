package www.pactera.com.coverage.project.component.data;


import java.util.ArrayList;
import java.util.List;

public class ClassCover {

    private List<Integer> codeCoverLine;
    private List<Integer> missCoverLine;
    private List<Integer> notCoverLine;

    public List<Integer> getCodeCoverLine() {
        return codeCoverLine;
    }

    public void setCodeCoverLine(List<Integer> codeCoverLine) {
        this.codeCoverLine = codeCoverLine;
    }

    public List<Integer> getMissCoverLine() {
        return missCoverLine;
    }

    public void setMissCoverLine(List<Integer> missCoverLine) {
        this.missCoverLine = missCoverLine;
    }

    public List<Integer> getNotCoverLine() {
        return notCoverLine;
    }

    public void setNotCoverLine(List<Integer> notCoverLine) {
        this.notCoverLine = notCoverLine;
    }

    public ClassCover(List<Integer> codeCoverLine, List<Integer> missCoverLine, List<Integer> notCoverLine) {
        this.codeCoverLine = codeCoverLine;
        this.missCoverLine = missCoverLine;
        this.notCoverLine = notCoverLine;
    }

    public ClassCover() {
        this.codeCoverLine = new ArrayList<>();
        this.missCoverLine = new ArrayList<>();
        this.notCoverLine = new ArrayList<>();
    }

}
