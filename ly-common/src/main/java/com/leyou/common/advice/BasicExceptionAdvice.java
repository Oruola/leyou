package com.leyou.common.advice;

import com.leyou.common.exceptions.LyException;
import com.leyou.common.vo.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 该类的作用:
 */
@ControllerAdvice
@Slf4j
public class BasicExceptionAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    //捕获自定义异常，返回值是自定义的ExceptionResult
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleLyException(LyException e) {
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
    }
}
