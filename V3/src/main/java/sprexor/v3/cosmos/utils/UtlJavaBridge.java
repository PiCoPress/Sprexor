package sprexor.v3.cosmos.utils;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;

@CommandInfo(name = "javabridge")
public class UtlJavaBridge implements SCommand {
	
	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		return 0;
	}
}
