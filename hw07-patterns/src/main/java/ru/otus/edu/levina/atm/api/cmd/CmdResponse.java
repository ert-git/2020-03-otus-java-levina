package ru.otus.edu.levina.atm.api.cmd;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;

@Data
public class CmdResponse {
    @NonNull
    private String cmdId;
    @NonNull
    private Integer atmId;

    private boolean success;

    private String descr;
    
    protected Date created = new Date();

    public CmdResponse(String cmdId, Integer atmId, boolean success) {
        this(cmdId, atmId, success, null);
    }
    
    public CmdResponse(String cmdId, Integer atmId, boolean success, String descr) {
        this.cmdId = cmdId;
        this.atmId = atmId;
        this.success = success;
        this.descr = descr;
    }
}
