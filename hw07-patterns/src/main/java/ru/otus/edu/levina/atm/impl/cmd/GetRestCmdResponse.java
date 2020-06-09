package ru.otus.edu.levina.atm.impl.cmd;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetRestCmdResponse extends CmdResponse {
    private final Integer availableMoney;

    public GetRestCmdResponse(String cmdId, Integer atmId, boolean success, String descr, Integer availableMoney) {
        super(cmdId, atmId, success, descr);
        this.availableMoney = availableMoney;
    }

}
