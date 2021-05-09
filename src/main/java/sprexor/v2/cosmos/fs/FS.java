package sprexor.v2.cosmos.fs;

import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;
import sprexor.v2.lib.Utils;

public class FS implements SFrame {

	@Override
	public String PackageName() {
		return "fs";
	}

	@Override
	public SCommand[] references() {
		return Utils.<SCommand>a(new SprexorChangeD(), new SprexorMkdir(), new SprexorRm());
	}

}
