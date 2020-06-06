package ru.otus.edu.levina.atm.impl.cmd;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.api.cmd.Command;

@Data
public abstract class AbstractCmd implements Command {

    protected final String cmdId;
    protected final Date created;
    
    public AbstractCmd() {
        cmdId = genCmdId();
        created = new Date();
    }

    private String genCmdId() {
        return UUID.randomUUID().toString();
    }
    
    @Override
    public CmdResponse execute(ATM atm) {
        try {
            System.out.println(String.format("Execute %s for ATM %s", this, atm.getId()));
            return executeInternal(atm);
        } catch (Exception e) {
            System.err.println(String.format("failed to execute %s for ATM %s", this, atm));
            e.printStackTrace();
            return fail(atm, e);
        }
    }
    
    protected abstract CmdResponse executeInternal(ATM atm);
    protected abstract CmdResponse fail(ATM atm, Exception e);
}
