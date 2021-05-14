package sprexor.v2.cosmos.fs;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.name;
import sprexor.v2.lib.Utils;

@name("ls")
public class SprexorLs implements SCommand {
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		Utils.join(args.getArrays(), " ");
		return 0;
	}
}
