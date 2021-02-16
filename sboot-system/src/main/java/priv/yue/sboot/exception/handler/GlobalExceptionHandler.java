package priv.yue.sboot.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.exception.BadRequestException;
import priv.yue.sboot.exception.EntityExistException;
import priv.yue.sboot.exception.EntityNotFoundException;
import priv.yue.sboot.utils.ThrowableUtils;
import priv.yue.sboot.vo.ValidateVo;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleException(Throwable e){
        // 打印堆栈信息
        log.error(ThrowableUtils.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * AuthenticationException
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> badCredentialsException(AuthenticationException e){
        log.error(e.toString());
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * UnknownAccountException 账户不存在
     */
    @ExceptionHandler(UnknownAccountException.class)
    public ResponseEntity<ApiError> badCredentialsException(UnknownAccountException e){
        return buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST.value(),"用户名或密码不正确"));
    }

    /**
     * IncorrectCredentialsException 密码验证错误
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ApiError> badCredentialsException(IncorrectCredentialsException e){
        return buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST.value(),"用户名或密码不正确"));
    }

    /**
     * IncorrectCredentialsException 无权限
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> UnauthorizedException(UnauthorizedException e){
        return buildResponseEntity(ApiError.error(HttpStatus.BAD_REQUEST.value(),"权限不足"));
    }


    /**
     * 处理自定义异常
     */
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ApiError> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtils.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getStatus(),e.getMessage()));
	}

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ResponseEntity<ApiError> entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error(ThrowableUtils.getStackTrace(e));
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiError> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ThrowableUtils.getStackTrace(e));
        return buildResponseEntity(ApiError.error(NOT_FOUND.value(),e.getMessage()));
    }

    /**
     * 验证异常
     */
    @ExceptionHandler(BindException.class)
    public RestResponse<Object> handleBindException(BindException e) {
        List<ValidateVo> validateVos = e.getBindingResult()
                .getFieldErrors()
                .parallelStream()
                .map(error -> new ValidateVo(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return RestResponse.fail("参数不正确", new HashMap() {{
                    put("fieldError", validateVos);
                }});
    }

    @ExceptionHandler({ConstraintViolationException.class, ServletRequestBindingException.class})
    public RestResponse<Object> handleValidationException(Exception e) {
        return RestResponse.fail("参数不正确", new HashMap() {{
            put("fieldError", "参数校验不通过");
        }});
    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getCode()));
    }
}
