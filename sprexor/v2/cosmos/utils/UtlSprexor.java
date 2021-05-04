package sprexor.v2.cosmos.utils;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
/**
 * with java reflection
 */
public class UtlSprexor implements SCommand {

	@Override
	public String name() {
		return "jupeach";
	}

	@Override
	public int main(IOCenter io, SManager Environment) {
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}

}
