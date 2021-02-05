package www.pactera.com.coverage.project.common.BaseEntity;



public class Data {

    private String coverageLine;

    private String missLine;

    private String totalLine;

    private String coverageRate;

    public String getCoverageLine() {
        return coverageLine;
    }

    public void setCoverageLine(String coverageLine) {
        this.coverageLine = coverageLine;
    }

    public String getMissLine() {
        return missLine;
    }

    public void setMissLine(String missLine) {
        this.missLine = missLine;
    }

    public String getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(String totalLine) {
        this.totalLine = totalLine;
    }

    public String getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(String coverageRate) {
        this.coverageRate = coverageRate;
    }

    public Data(String coverageLine, String missLine, String totalLine, String coverageRate) {
        this.coverageLine = coverageLine;
        this.missLine = missLine;
        this.totalLine = totalLine;
        this.coverageRate = coverageRate;
    }

    public Data() {

    }

    @Override
    public String toString() {
        return "Data{" +
                "coverageLine='" + coverageLine + '\'' +
                ", missLine='" + missLine + '\'' +
                ", totalLine='" + totalLine + '\'' +
                ", coverageRate='" + coverageRate + '\'' +
                '}';
    }
}
