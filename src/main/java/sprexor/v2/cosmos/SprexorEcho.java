package sprexor.v2.cosmos;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.components.SCommand;

public class SprexorEcho implements SCommand {
	@Override
	public String name() {
		return "echo";
	}

	@Override
	public int main(IOCenter io, SManager Environment) {
		StringBuilder sb = new StringBuilder();
		String[] args = io.getComponent().getArrays();
		for(String str : args) {
			sb.append(str);
		}
		io.out.println(sb.toString());
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}

}
