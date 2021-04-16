package priv.yue.logging.es7.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangLongYue
 * @since 2021/4/8 13:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "sys_log_op")
public class LogOpEs implements Serializable {

    private static final long serialVersionUID = 4891692017673396117L;

    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @Field(type = FieldType.Text, copyTo = "all")
    private String ip;

    @Field(type = FieldType.Text, copyTo = "all", analyzer = "ik_max_word")
    private String location;

    @Field(type = FieldType.Text, copyTo = "all")
    private String browser;

    @Field(name = "browser_version", type = FieldType.Text, copyTo = "all")
    private String browserVersion;

    @Field(name = "browser_engine", type = FieldType.Text, copyTo = "all")
    private String browserEngine;

    @Field(name = "browser_engine_version", type = FieldType.Text, copyTo = "all")
    private String browserEngineVersion;

    @Field(name = "is_mobile", type = FieldType.Short)
    private Short isMobile;

    @Field(type = FieldType.Text, copyTo = "all")
    private String os;

    @Field(type = FieldType.Text, copyTo = "all")
    private String platform;

    @Field(type = FieldType.Text)
    private String spider;

    @Field(name = "request_uri", type = FieldType.Text, copyTo = "all")
    private String requestUri;

    @Field(name = "error_cause", type = FieldType.Text, copyTo = "all")
    private String errorCause;

    @Field(name = "error_msg", type = FieldType.Text, copyTo = "all")
    private String errorMsg;

    @Field(type = FieldType.Text, copyTo = "all", analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(name = "create_time", type = FieldType.Date, format = DateFormat.date_optional_time)
    private Date createTime;

    @Field(name = "business_type", type = FieldType.Text, analyzer = "ik_max_word")
    private String businessType;

    @Field(name = "class_name", type = FieldType.Text, copyTo = "all")
    private String className;

    @Field(name = "method_name", type = FieldType.Text, copyTo = "all")
    private String methodName;

    @Field(type = FieldType.Text)
    private String parameter;

    @Field(name = "user_id", type = FieldType.Long, copyTo = "all")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @Field(type = FieldType.Text, copyTo = "all")
    private String username;

    @Field(name = "dept_id", type = FieldType.Long, copyTo = "all")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    @Field(name = "run_time", type = FieldType.Long)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long runTime;

    @Field(name = "return_value", type = FieldType.Text)
    private String returnValue;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;

}
