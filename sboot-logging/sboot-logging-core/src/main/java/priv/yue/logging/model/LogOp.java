package priv.yue.logging.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:27
 */
public class LogOp implements Serializable {

    private static final long serialVersionUID = -280748645893932851L;

    // 操作日志id
    private Long id;

    // 当前访问的ip地址
    private String ip;

    // ip地址的实际地理位置
    private String location;

    // 浏览器内核类型
    private String browser;

    // 浏览器内核版本
    private String browserVersion;

    // 浏览器的解析引擎类型
    private String browserEngine;

    // 浏览器的解析引擎版本
    private String browserEngineVersion;

    // 是否为移动平台
    private Boolean isMobile;

    // 操作系统类型
    private String os;

    // 操作平台类型
    private String platform;

    // 爬虫的类型(如果有)
    private String spider;

    // 访问的URL获取除去host部分的路径
    private String requestUri;

    // 访问出现错误时获取到的异常原因
    private String errorCause;

    // 访问出现错误时获取到的异常信息
    private String errorMsg;

    // 模块名称
    private String title;

    // 访问的状态(0:正常,1:不正常)
    private Integer status;

    // 访问的时间
    private Date createTime;

    // 业务类型
    private String businessType;

    // 执行操作的类名称
    private String className;

    // 执行操作的方法名称
    private String methodName;

    // url参数
    private String parameter;

    // 用户ID
    private Long userId;

    // 用户名
    private String username;

    // 部门ID
    private Long deptId;

    // 执行时长
    private Long runTime;

    // 返回值
    private String returnValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getBrowserEngine() {
        return browserEngine;
    }

    public void setBrowserEngine(String browserEngine) {
        this.browserEngine = browserEngine;
    }

    public String getBrowserEngineVersion() {
        return browserEngineVersion;
    }

    public void setBrowserEngineVersion(String browserEngineVersion) {
        this.browserEngineVersion = browserEngineVersion;
    }

    public Boolean getMobile() {
        return isMobile;
    }

    public void setMobile(Boolean mobile) {
        isMobile = mobile;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSpider() {
        return spider;
    }

    public void setSpider(String spider) {
        this.spider = spider;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
}
