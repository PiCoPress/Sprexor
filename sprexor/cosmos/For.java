package sprexor.cosmos;

import sprexor.CommandProvider;
import java.util.HashMap;
import sprexor.CommandFactory;
import sprexor.IOCenter;
import sprexor.Tools;
import sprexor.Sprexor;
import sprexor.SprexorException;
import sprexor.Component;

public class For implements CommandFactory {
	public HashMap<String, CommandProvider> cc;
	public HashMap<String, CommandProvider> cl;
	@Override
	public String getCommandName() {
		return "for";
	}
	@Override
	public String help() {
		return "**USE_SMT_FORM**" +
				"USAGE : for [OPTION] [NUM] ARGS...[nnt]" +
				"-t, -T, --text[tt] : Return the text is repeated NUM times. [nt]" +
				"-c. -C. --code[tt] : Run the script. [nt]" +
				"-h, -H, --help[tt] : Print this message.";
	}
	@Override
	public IOCenter code(Component args, Sprexor sprex) {
		String option_1 = args.getsf(0);
		switch(option_1) {
		case "-t" :
		case "-T" :
		case "--text" :
			int i = Integer.parseInt(args.getsf(1));
			return new IOCenter(text(args.get(), i));
		case "-c" :
		case "-C" :
		case "--code" :
			return run(Tools.cutArr(args.get(), 2), sprex, Integer.parseInt(args.gets(0)));
		case "-h":
		case "-H":
		case "--help":
		default :
			return new IOCenter("Unknown option : " + option_1 + "\n" + Tools.SMT_FORM(help().substring(16)));
		}
	}
	@Override
	public IOCenter emptyArgs(Sprexor sprex) {
		return new IOCenter(Tools.SMT_FORM(help().substring(16)));
	}
	@Override
	public String error(Exception er) {
		if(er.equals(NumberFormatException.class)) return "Invalid Number value.";
		return "Error occured.";
	}
	//
	//features
	private String text(String[] a, int i) { return Tools.arg2String(Tools.cutArr(a, 2), " ").repeat(i); }
	//
	private IOCenter run(String[] a, Sprexor sprex, int i) {
		try {
		for(int c = 0; c < i; c ++) sprex.exec(a[0], Tools.cutArr(a, 1));
		} catch(SprexorException se) {
			return new IOCenter(se.status + "", IOCenter.ERR);
		}
		return new IOCenter("");
	}
}
