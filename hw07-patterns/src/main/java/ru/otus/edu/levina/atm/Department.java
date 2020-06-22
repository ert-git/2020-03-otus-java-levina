package ru.otus.edu.levina.atm;

import java.util.List;

import lombok.Getter;
import ru.otus.edu.levina.atm.api.ATM;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;
import ru.otus.edu.levina.atm.api.cmd.CmdResponseListener;
import ru.otus.edu.levina.atm.impl.cmd.GetRestCmd;
import ru.otus.edu.levina.atm.impl.cmd.ResetCmd;

public class Department implements CmdResponseListener {
    @Getter
    private List<ATM> atmList;
    @Getter
    private CmdResponseStore store;

    public Department(List<ATM> atmList) {
        this.atmList = atmList;
        this.store = new CmdResponseStore();
    }
    
    public void init() {
        atmList.forEach(atm -> atm.addCmdListener(this));
    }
    
    public void resetAll() {
        atmList.forEach(atm -> atm.process(new ResetCmd()));
    }
    
    public void getAllRests() {
        atmList.forEach(atm -> atm.process(new GetRestCmd()));
    }
    
    @Override
    public void onResponse(CmdResponse resp) {
        System.out.println(String.format("onResponse: %s", resp));
        store.getResponses().add(resp);
    }

}
