package com.dressmeupapp.retrofit.entities;

public class RateExistsResponse {
    private Boolean rateExists;

    public RateExistsResponse(Boolean rateExists) {
        this.rateExists = rateExists;
    }

    public Boolean getRateExists() {
        return rateExists;
    }

    public void setRateExists(Boolean rateExists) {
        this.rateExists = rateExists;
    }

}
