package sprexor.cosmos;

import sprexor.IOCenter;
import sprexor.SCommand;
import sprexor.Sprexor;

public class SprexorEcho implements SCommand {

	@Override
	public String name() {
		return "echo";
	}

	@Override
	public int main(IOCenter io, Sprexor Environment) {
		String sum = "";
		String[] args = io.getComponent().get();
		for(String str : args) {
			sum += str + " ";
		}
		io.out.println(sum);
		return 0;
	}

}
