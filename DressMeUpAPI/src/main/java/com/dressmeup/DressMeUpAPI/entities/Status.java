package com.dressmeup.DressMeUpAPI.entities;

import lombok.Getter;
import lombok.Setter;

public class Status{
    @Getter
    private String status;

    private Status(){

    }

    public static Status success(){
        Status status = new Status();
        status.status = "SUCCESS";
        return status;
    }

    public static Status failure(){
        Status status = new Status();
        status.status = "FAILURE";
        return status;
    }
}
