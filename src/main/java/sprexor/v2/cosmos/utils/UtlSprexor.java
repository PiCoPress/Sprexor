package sprexor.v2.cosmos.utils;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.CommandInfo;

@CommandInfo(name = "jupeach", version = "0.0.1")
public class UtlSprexor implements SCommand {
	public static String Version = "0.0.1";
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		if(args.findOption(strarray("-v", "--version"), 0)) {
			io.out.println(Version);
			return 0;
		}
		return 0;
	}
}
