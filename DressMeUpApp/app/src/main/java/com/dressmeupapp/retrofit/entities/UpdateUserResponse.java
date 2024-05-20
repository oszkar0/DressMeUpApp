package com.dressmeupapp.retrofit.entities;

public class UpdateUserResponse {
    private Boolean result;
    private String errorMessage;

    public UpdateUserResponse(Boolean result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
