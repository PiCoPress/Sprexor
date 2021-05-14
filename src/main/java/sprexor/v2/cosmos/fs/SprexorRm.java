package sprexor.v2.cosmos.fs;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.name;

@name("rm")
public class SprexorRm implements SCommand {
	
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		return 0;
	}
}
