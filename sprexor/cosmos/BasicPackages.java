package sprexor.cosmos;

import sprexor.CommandFactory;
import sprexor.Sprexor;
import sprexor.IOCenter;
import sprexor.Tools;
import sprexor.Component;

/**
 * It is a Class to provide BasicPackages and to make CommandClass for who don't know.
 * @author PICOPress
 * @since 0.2.4
 */
public class BasicPackages implements CommandFactory{
	public String getCommandName() {
		return "find";
	}
	@Override
	public String help() {
		return "::: The BasicPackages 0.2.18 :::\n" +
				"1. find : Find text(s) by the unit of line.\n" + 
				"2. repeat : Repeat the text.";
	}
	@Override
	public CommandFactory[] referenceClass() {
		return Tools.toCFClass(this, new For()); //Tools.toCFClasses();
	}
	@Override
	public int code(IOCenter io, Sprexor SprexorInstance) {
		Component args = io.getComponent();
		String tmp = Tools.arg2String(Tools.excludeArr(args.get(), 0));
		String find = args.gets(0);
		String[] splitedStr = tmp.split("\n");
		String result = "";
		
		for(String it : splitedStr) {
			if(it.indexOf(find) != -1)result += it + "\n";
		}
		io.out.println(result);
		return 0;
	}
}