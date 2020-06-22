package ru.otus.edu.levina.atm.impl.cmd;

import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.api.cmd.Command;

public class GetRestCmd extends AbstractCmd implements Command {

    @Override
    protected CmdResponse executeInternal(ATM atm) {
        return new GetRestCmdResponse(cmdId, atm.getId(), true, null, atm.getAvailableMoney());
    }

    @Override
    protected CmdResponse fail(ATM atm, Exception e) {
        return new GetRestCmdResponse(cmdId, atm.getId(), false, e.getMessage(), null);
    }

}
