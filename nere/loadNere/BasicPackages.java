package nere.loadNere;

import nere.*;
/**
 * example Convenience Register Command Class. Before code(version 0.2.3's BasicPackages is toooo inefficient, so remade.)
 * @author PICOPress
 * @since 0.2.4
 */
public class BasicPackages implements CommandProvider{
	public String getCommandName() {
		return "example";
	}
	
	public String help() {
		return "example helping message.";
	}
	
	public static BasicPackages call() {
		return new BasicPackages();
	}
	
	public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
		return args;
	}
}