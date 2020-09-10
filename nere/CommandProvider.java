package pico.chall.nere;

import pico.chall.nere.unknownCommand;

@SuppressWarnings("unused")
public interface CommandProvider {
	public final String VERSION = "0.4.1 testing";
	
		
	public default Object ErrorEventListener(Exception e) {
		return null;
	}
	public default Object no_arg_apply() {
		return "NO_ARGUMENT : please implement this function.";
	};
	//public Object apply(String[] args);
	public Object apply(String[] args, boolean[] isWrapped);
}
