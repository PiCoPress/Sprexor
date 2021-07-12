package sprexor.v3.cosmos;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.SprexorException;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;
import sprexor.v3.lib.Smt;
import sprexor.v3.lib.Utils;

@CommandInfo(name = "for")
public class SprexorFor implements SCommand {
	private String help() {
		return "USAGE : for [OPTION] [NUM] ARGS...[nnt]" +
				"-t, -T, --text[tt] : Return the text is repeated NUM times. [nt]" +
				"-c. -C. --code[tt] : Run the script. [nt]" +
				"-h, -H, --help[tt] : Print this message.";
	}
	@Override
	public int main(IOCenter io, SParameter args, SManager SprexorInstance) {
		if(args.isEmpty()) {
			io.out.println(Smt.SMT_FORM(help()));
			return 0;
		}
		String option_1 = args.getElement(0);
		
		switch(option_1) {
		case "-t" :
		case "-T" :
		case "--text" :
			int i = Integer.parseInt(args.getElement(1));
			for(int n = 0; n < i; n ++)
			io.out.print(Utils.join((String[]) Utils.cutArr(args.getArrays(), 2), " "));
			return 0;
		case "-c" :
		case "-C" :
		case "--code" :
			 run((String[])Utils.cutArr(args.getArrays(), 2), SprexorInstance, Integer.parseInt(args.getElement(0)));
			 return 0;
		case "-h":
		case "-H":
		case "--help":
		default :
			io.out.println("Unknown option : " + option_1 + "\n" + Smt.SMT_FORM(help()));
			return 1;
		}
	}
	//
	private String run(String[] a, SManager sprex, int i) {
		try {
		for(int c = 0; c < i; c ++) sprex.exec(a[0], (String[]) Utils.cutArr(a, 1));
		} catch(SprexorException se) {
			return "";
		}
		return "";
	}
}
