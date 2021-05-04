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
		String sum = "";
		String[] args = io.getComponent().get();
		for(String str : args) {
			sum += str + " ";
		}
		io.out.println(sum);
		return 0;
	}

	@Override
	public String version() {
		return "0.0.0";
	}

}
