package sprexor.v2.cosmos.utils;

import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;
import sprexor.v2.lib.Utils;

public class CTools implements SFrame {

	@Override
	public String PackageName() {
		return "tools";
	}

	@Override
	public SCommand[] references() {
		return Utils.<SCommand>a(new UtlJavaBridge(), new UtlCurl());
	}

}
