package com.example.exception;


import com.example.entity.R;

public class TestException extends RuntimeException{
    String message;

    public TestException(String message){
        super(message);
    }

    public TestException(){

    }
}
