package sprexor.cosmos;
import sprexor.GlobalData;
import sprexor.IOCenter;
import sprexor.Tools;

class For implements sprexor.CommandProvider {
	public String getCommandName() {
		return "for";
	}
	public String help() {
		return "USAGE : for (txt | code) (count) COMMANDS...\n\n" +
				"";
	}
	@Override
	public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope) {
		String op = args [ 0 ] ;
		switch ( op ) {
		case "txt" :
			return new IOCenter ( Tools . arg2String ( Tools . cutArr ( args , 2 ) , " " ) . repeat ( Integer . parseInt ( args [ 1 ] ) ) , IOCenter . STDOUT ) ;
		case "code" :
			return new IOCenter("Developing...");
		default :
			return new IOCenter(help(), IOCenter.CMT);
		}
	}
	public Object emptyArgs() {
		return help();
	}
	public Object error(Exception er) {
		er.printStackTrace();
		return "Error occured.";
	}
}
