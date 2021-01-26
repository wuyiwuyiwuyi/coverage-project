package www.pactera.com.coverage.project.common.systemEmun;

public enum DataDeleteEnum {

    IS_DELETE("Y", "已删除"),
    NO_DELETE("N", "未删除");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    DataDeleteEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
