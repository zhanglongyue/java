package priv.yue.auth.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import priv.yue.common.exception.handler.ApiError;

/**
 * @author ZhangLongYue
 * @since 2021/3/11 16:34
 */
@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

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
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getCode()));
    }

}

