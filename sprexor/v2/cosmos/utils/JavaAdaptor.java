package sprexor.v2.cosmos.utils;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
/**
 * with javaparser
 */
public class JavaAdaptor implements SCommand {

	@Override
	public String name() {
		return "adaptor";
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
