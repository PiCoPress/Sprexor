package sprexor.cosmos;

import sprexor.Component;
import sprexor.IOCenter;
import sprexor.Sprexor;
import sprexor.SprexorException;
import sprexor.Tools;

class For implements sprexor.CommandFactory {
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
			io.out.println(Tools.SMT_FORM(help()));
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
			 run(Tools.cutArr(args.get(), 2), SprexorInstance, Integer.parseInt(args.gets(0)));
			 return 0;
		case "-h":
		case "-H":
		case "--help":
		default :
			io.out.println("Unknown option : " + option_1 + "\n" + Tools.SMT_FORM(help().substring(16)));
			return 1;
		}
	}
	//
	//features
	private String text(String[] a, int i) { return Tools.arg2String(Tools.cutArr(a, 2), " ").repeat(i); }
	//
	private String run(String[] a, Sprexor sprex, int i) {
		try {
		for(int c = 0; c < i; c ++) sprex.exec(a[0], Tools.cutArr(a, 1));
		} catch(SprexorException se) {
			return se.status + "";
		}
		return "";
	}
}
