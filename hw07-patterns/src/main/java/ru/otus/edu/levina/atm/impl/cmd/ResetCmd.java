package ru.otus.edu.levina.atm.impl.cmd;

import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.api.cmd.Command;

public class ResetCmd extends AbstrCmd implements Command {

    @Override
    protected CmdResponse executeInternal(ATM atm) {
        atm.reset();
        return new ResetCmdResponse(cmdId, atm.getId(), true, null);
    }

    @Override
    protected CmdResponse fail(ATM atm, Throwable e) {
        return new ResetCmdResponse(cmdId, atm.getId(), false, e.getMessage());
    }
}
