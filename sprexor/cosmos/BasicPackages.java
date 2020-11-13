package sprexor.cosmos;

import sprexor.CommandProvider;
import sprexor.GlobalData;
import sprexor.Tools;

/**
 * example Convenience Register Command Class.
 * @author PICOPress
 * @since 0.2.4
 */
public class BasicPackages implements CommandProvider{
	public String getCommandName() {
		return "find";
	}
	
	public String help() {
		return "::: The BasicPackages 0.2.14 :::\n" +
				"1. find : Find text(s) by the unit of line.\n" + 
				"2. for : Repeat and run commands for count.";
	}
	
	public Object emptyArgs() {
		return help();
	}
	
	public CommandProvider[] referenceClass() {
		return Tools.toCPClass(this, new For()); //Tools.toCPClass();
	}
	
	public Object error(Exception e) {
		return e;
	}
	
	@Override public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
		String tmp = Tools.arg2String(Tools.excludeArr(args, 0));
		String find = args[0];
		String[] splitedStr = tmp.split("\n");
		String result = "";
		
		for(String it : splitedStr) {
			if(it.indexOf(find) != -1)result += it + "\n";
		}
		return result;
	}
}