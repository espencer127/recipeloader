package com.spencer.recipeloader.exception;

import org.springframework.core.NestedRuntimeException;

public class GrocyClientException extends NestedRuntimeException {

    public GrocyClientException(String msg) {
        super(msg);
    }

}
