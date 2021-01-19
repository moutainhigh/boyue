package com.boyue.common.advice;

import com.boyue.common.exception.ByException;
import com.boyue.common.vo.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Intellij IDEA.
 *
 * @Date: 2021/1/15 23:05
 * @Author: Jacky
 * @Description: 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class BasicExceptionAdvice {
    @ExceptionHandler(ByException.class)
    public ResponseEntity<ExceptionResult> handleLyException(ByException e) {
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
    }
}
