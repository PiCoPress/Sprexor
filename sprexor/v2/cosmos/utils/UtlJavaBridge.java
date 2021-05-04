package sprexor.v2.cosmos.utils;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;

public class UtlJavaBridge implements SCommand {

	@Override
	public String name() {
		return "javabridge";
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
