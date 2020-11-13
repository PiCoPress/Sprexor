package sprexor.cosmos;
import sprexor.GlobalData;

class For implements sprexor.CommandProvider {
	public String getCommandName() {
		return "for";
	}
	public String help() {
		return "USAGE : for (count) COMMANDS...\n\n" +
				"";
	}
	@Override
	public Object code(String[] args, boolean[] isWrapped, GlobalData scope) {
		return "developing...";
	}
	public Object emptyArgs() {
		return help();
	}
}
