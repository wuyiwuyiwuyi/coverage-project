package www.pactera.com.coverage.project.common.BaseEntity;

import java.text.SimpleDateFormat;

public class ResponseData<T> {

    private String serverTime;

    private String code;

    private String message;

    private T value;

    public boolean isSuccess() {
        return this.code.startsWith("0000");
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "serverTime='" + serverTime + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", value=" + value +
                '}';
    }

    public ResponseData(String code, String message, T value) {
        this.serverTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        this.code = code;
        this.message = message;
        this.value = value;
    }

    public ResponseData() {

    }
}
