package sprexor.v2.cosmos.fs;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.lib.Utils;

public class SprexorLs implements SCommand {

	@Override
	public String name() {
		return "ls";
	}

	@Override
	public int main(IOCenter io, SManager Environment) {
		SParameter args = io.getComponent();
		Utils.join(args.getArrays(), " ");
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}

}
