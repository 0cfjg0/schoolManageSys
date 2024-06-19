package com.example.exception;


public class TestException extends RuntimeException{
    String message;

    public TestException(String message){
        this.message = message;
    }

    public TestException(){

    }
}
