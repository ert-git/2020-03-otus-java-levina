package ru.otus.edu.levina.atm;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import ru.otus.edu.levina.atm.api.cmd.CmdResponse;

@Getter 
public class CmdResponseStore {

    private Set<CmdResponse> responses = new HashSet<>();
}
