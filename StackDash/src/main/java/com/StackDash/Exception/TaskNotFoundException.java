package com.StackDash.Exception;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.autoconfigure.http.codec.HttpCodecsProperties;

public class TaskNotFoundException extends RuntimeException {

    private String message;
    private HttpCodecsProperties http;

    public TaskNotFoundException(String message){
    }
}
