package com.btmessenger.model;

/**
 * The ErrorDataModel class is a model to form a library error data
 */
public class ErrorDataModel {

    /**
     * Variables
     **/
    private int errorType;
    private String errorMessage;

    /**
     * Constructor
     **/
    public ErrorDataModel(int errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    /**
     * Methods
     **/
    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
