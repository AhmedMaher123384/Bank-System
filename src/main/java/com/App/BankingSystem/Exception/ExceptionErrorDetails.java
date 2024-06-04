package com.App.BankingSystem.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExceptionErrorDetails {

    private String uri;
    private String Message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date TimeStamp;

    public ExceptionErrorDetails() {
        this.TimeStamp=new Date();
    }

    public ExceptionErrorDetails(String uri, String message) {
        this();
        this.uri = uri;
        Message = message;
    }
}
