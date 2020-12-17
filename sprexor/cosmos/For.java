package sprexor.cosmos;
import sprexor.CommandFactory;
import sprexor.CommandNotFoundException;
import sprexor.GlobalData;
import sprexor.IOCenter;
import sprexor.Tools;
import sprexor.Sprexor;
import sprexor.SprexorException;

class For implements sprexor.CommandFactory {
	@Override
	public String getCommandName() {
		return "repeat";
	}
	@Override
	public String help() {
		return "**USE_SMT_FORM**" +
				"USAGE : repeat [OPTION] [NUM] ARGS...[nnt]" +
				"-t, -T, --text[tt] : Return the text is repeated NUM times. [nt]" +
				"-h, -H, --help[tt] Print this message.";
	}
	@Override
	public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope, Sprexor sprex) {
		String option_1 = args[0];
		switch(option_1) {
		case "-t" :
		case "-T" :
		case "--text" :
			int i = Integer.parseInt(args[1]);
			return new IOCenter(Tools.arg2String(Tools.cutArr(args, 2), " ").repeat(i));
		case "-h":
		case "-H":
		case "--help":
		default :
			return new IOCenter(Tools.SMT_FORM(help().substring(16)));
		}
	}
	@Override
	public IOCenter emptyArgs(GlobalData scope, Sprexor sprex) {
		return new IOCenter(Tools.SMT_FORM(help().substring(16)));
	}
	@Override
	public Object error(Exception er) {
		return "Error occured.";
	}
}
