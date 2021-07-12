package sprexor.v3.cosmos;

import sprexor.v3.IOCenter;
import sprexor.v3.SManager;
import sprexor.v3.components.SCommand;
import sprexor.v3.components.SParameter;
import sprexor.v3.components.annotations.CommandInfo;

@CommandInfo(name = "echo")
public class SprexorEcho implements SCommand {

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		StringBuilder sb = new StringBuilder();
		for(String str : args) {
			sb.append(str).append(' ');
		}
		io.out.println(sb.toString());
		return 0;
	}
}
