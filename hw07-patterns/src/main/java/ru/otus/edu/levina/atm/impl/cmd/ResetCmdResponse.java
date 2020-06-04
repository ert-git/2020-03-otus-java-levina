package ru.otus.edu.levina.atm.impl.cmd;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResetCmdResponse extends CmdResponse {

    public ResetCmdResponse(String cmdId, Integer atmId, boolean success, String descr) {
        super(cmdId, atmId, success, descr);
    }


}
