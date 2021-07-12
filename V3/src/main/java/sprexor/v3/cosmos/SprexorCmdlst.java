package sprexor.v3.cosmos;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;
import sprexor.v3.lib.Utils;

@CommandInfo(name = "commands", version = "0.0.1")
public class SprexorCmdlst implements SCommand {

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		io.out.printf(Utils.join(Environment.getList(), "\t") + "\n");
		return 0;
	}
}
