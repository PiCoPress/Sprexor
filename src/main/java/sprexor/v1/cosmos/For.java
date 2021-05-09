package sprexor.v1.cosmos;

import sprexor.v1.CommandFactory;
import sprexor.v1.Component;
import sprexor.v1.IOCenter;
import sprexor.v1.Sprexor;
import sprexor.v1.SprexorException;
import sprexor.v1.lib.Smt;
import sprexor.v1.lib.Utils;

class For implements CommandFactory {
	public String getCommandName() {
		return "for";
	}
	public String help() {
		return "USAGE : for [OPTION] [NUM] ARGS...[nnt]" +
				"-t, -T, --text[tt] : Return the text is repeated NUM times. [nt]" +
				"-c. -C. --code[tt] : Run the script. [nt]" +
				"-h, -H, --help[tt] : Print this message.";
	}
	public int code(IOCenter io, Sprexor SprexorInstance) {
		Component args = io.getComponent();
		if(args.isEmpty()) {
			io.out.println(Smt.SMT_FORM(help()));
			return 0;
		}
		String option_1 = args.getsf(0);
		switch(option_1) {
		case "-t" :
		case "-T" :
		case "--text" :
			int i = Integer.parseInt(args.getsf(1));
			io.out.println(text(args.get(), i));
			return 0;
		case "-c" :
		case "-C" :
		case "--code" :
			 run(Utils.cutArr(args.get(), 2), SprexorInstance, Integer.parseInt(args.gets(0)));
			 return 0;
		case "-h":
		case "-H":
		case "--help":
		default :
			io.out.println("Unknown option : " + option_1 + "\n" + Smt.SMT_FORM(help().substring(16)));
			return 1;
		}
	}
	//
	//features
	private String text(String[] a, int i) { return Utils.arg2String(Utils.cutArr(a, 2), " ").repeat(i); }
	//
	private String run(String[] a, Sprexor sprex, int i) {
		try {
		for(int c = 0; c < i; c ++) sprex.exec(a[0], Utils.cutArr(a, 1));
		} catch(SprexorException se) {
			return "";
		}
		return "";
	}
}
