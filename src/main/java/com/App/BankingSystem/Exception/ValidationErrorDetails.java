package com.App.BankingSystem.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ValidationErrorDetails {

    private List<String> errors;
    private String uri;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timeStamp;

    public void addError(String error){
        this.errors.add(error);
    }

    public ValidationErrorDetails() {
        this.timeStamp=new Date();
        this.errors=new ArrayList<>();
    }

    public ValidationErrorDetails(String uri) {
        this();
        this.uri = uri;

    }
}
