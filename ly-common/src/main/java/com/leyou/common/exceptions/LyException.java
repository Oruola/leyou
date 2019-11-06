package com.leyou.common.exceptions;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Getter;

/**
 * 该类的作用:
 */
@Getter
public class LyException extends RuntimeException{
    private int status;

    public LyException(ExceptionEnum em) {
        super(em.getMessage());
        this.status = em.getStatus();
    }

    public int getStatus() {
        return status;
    }
}
