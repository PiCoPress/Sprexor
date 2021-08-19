package sprexor.v3.cosmos.fs;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;
import sprexor.v3.lib.Utils;

@CommandInfo(name = "ls")
public class SprexorLs implements SCommand {
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		Utils.join(args.getArrays(), " ");
		return 0;
	}
}
