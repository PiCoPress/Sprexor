package sprexor.v2.cosmos;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.CommandInfo;
import sprexor.v2.lib.Utils;

@name("commands")
@CommandInfo(name = "commands", version = "0.0.1")
public class SprexorCmdlst implements SCommand {

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		io.out.printf(Utils.join(Environment.getList(), "\t") + "\n");
		return 0;
	}
}
