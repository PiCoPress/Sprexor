package sprexor.v2.cosmos;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.lib.Utils;

public class SprexorCmdlst implements SCommand {
	@Override
	public String name() {
		return "commands";
	}

	@Override
	public int main(IOCenter io, SManager Environment) {
		io.out.printf(Utils.join(Environment.getList(), "\t") + "\n");
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}
	
}
