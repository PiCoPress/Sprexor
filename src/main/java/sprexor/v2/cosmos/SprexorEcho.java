package sprexor.v2.cosmos;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.CommandInfo;

@CommandInfo(name = "echo")
public class SprexorEcho implements SCommand {

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		StringBuilder sb = new StringBuilder();
		for(String str : args) {
			sb.append(str);
		}
		io.out.println(sb.toString());
		return 0;
	}
}
