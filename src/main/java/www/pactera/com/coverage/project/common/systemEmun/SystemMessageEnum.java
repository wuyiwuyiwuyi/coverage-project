package www.pactera.com.coverage.project.common.systemEmun;

public enum SystemMessageEnum {

    SUCCESS("00000", "请求成功"),
    ERROR("00001", "请求错误"),
    EXCEPTION("00002", "请求异常"),
    MERGE_HANDLE_USER_EXCEPTION("00003", "请选择相同的用户进行合并"),
    MERGE_VERSION_NUMBER_EXCEPTION("00004", "请选择同期的版本进行合并"),
    CMD_EXECUTE_EXCEPTION("00005", "命令行执行异常"),
    REGISTER_EXCEPTION("00006","此用户名已注册"),
    LOGIN_SUCCESS("00007","登陆成功"),
    LOGIN_FAILURE("00008","登陆失败"),
    CONNECTION_EXCEPTION("00009","连接收集服务失败"),
    PARSING_EXCEPTION("000010","覆盖率数据解析异常"),
    PARAMETER_LACK("00011","收集覆盖率缺少必要参数"),
    PARAMETER_LACK_QUERY_COVERAGE("00012","查询覆盖率缺少必要参数"),
    ANALYSIS_FILE_EXCEPTION("00013","文件解析时异常"),
    SOURCES_FILE_NOT_FIND("00014","项目源文件未发现"),
    PORT_RELEVANCE_NOT_FIND("00015","收集项目暂不持支覆盖率收集");


    private String code;

    private String msg;

    SystemMessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
