package ru.otus.edu.levina;

public class Demo {

	public static void main(String[] args) {
		TestLoggingInterface tli = Ioc.createMyClass();
		int arg = args.length > 0 ? Integer.parseInt(args[0]) : 6;
		tli.calculation(arg);
		tli.calculation();
	}

}
