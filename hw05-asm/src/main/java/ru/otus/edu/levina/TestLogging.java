package ru.otus.edu.levina;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int data) {
        System.out.println("Start calculation with log: data=" + data);
    }

    @Override
    public void calculation() {
        System.out.println("Start calculation without log");
    }

}
