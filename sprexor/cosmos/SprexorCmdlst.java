package sprexor.cosmos;

import sprexor.IOCenter;
import sprexor.SCommand;
import sprexor.Sprexor;
import sprexor.lib.Utils;

public class SprexorCmdlst implements SCommand {
	@Override
	public String name() {
		return "commands";
	}

	@Override
	public int main(IOCenter io, Sprexor Environment) {
		io.out.printf(Utils.join(Environment.getList(), "\t") + "\n");
		return 0;
	}
	
}
