package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex 业务异常对象
     * @return 包含错误信息的统一结果
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理数据库完整性异常（用户名重复、必填字段为空等）
     *
     * @param ex 数据库异常
     * @return 包含错误信息的统一结果
     */
    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public Result handleDataIntegrityException(Exception ex) {
        String message = ex.getMessage();
        log.error("数据库异常：{}", message);
        // 唯一键冲突，例如：Duplicate entry 'admin' for key 'employee.username'
        if (message != null && message.contains("Duplicate entry")) {
            return Result.error(MessageConstant.ALREAD_EXISTS);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
