package priv.yue.sboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "操作日志信息")
@TableName("sys_log_op")
public class LogOp {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "操作日志id", example = "0")
    private Long id;

    @ApiModelProperty(value = "当前访问的ip地址")
    private String ip;

    @ApiModelProperty(value = "ip地址的实际地理位置")
    private String location;

    @ApiModelProperty(value = "浏览器内核类型")
    private String browser;

    @ApiModelProperty(value = "浏览器内核版本")
    private String browserVersion;

    @ApiModelProperty(value = "浏览器的解析引擎类型")
    private String browserEngine;

    @ApiModelProperty(value = "浏览器的解析引擎版本")
    private String browserEngineVersion;

    @ApiModelProperty(value = "是否为移动平台")
    private Boolean isMobile;

    @ApiModelProperty(value = "操作系统类型")
    private String os;

    @ApiModelProperty(value = "操作平台类型")
    private String platform;

    @ApiModelProperty(value = "爬虫的类型(如果有)")
    private String spider;

    @ApiModelProperty(value = "访问的URL获取除去host部分的路径")
    private String requestUri;

    @ApiModelProperty(value = "访问出现错误时获取到的异常原因")
    private String errorCause;

    @ApiModelProperty(value = "访问出现错误时获取到的异常信息")
    private String errorMsg;

    @ApiModelProperty(value = "模块名称")
    private String title;

    @ApiModelProperty(value = "访问的状态(0:正常,1:不正常)", example = "0")
    private Integer status;

    @ApiModelProperty(value = "访问的时间")
    private Date createTime;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "执行操作的类名称")
    private String className;

    @ApiModelProperty(value = "执行操作的方法名称")
    private String methodName;

    @ApiModelProperty(value = "url参数")
    private String parameter;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "执行时长")
    private Long runTime;

    @ApiModelProperty(value = "返回值")
    private String returnValue;

}
