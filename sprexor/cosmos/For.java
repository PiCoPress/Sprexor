package sprexor.cosmos;
import sprexor.CommandFactory;
import sprexor.CommandNotFoundException;
import sprexor.GlobalData;
import sprexor.IOCenter;
import sprexor.Tools;
import sprexor.Sprexor;
import sprexor.SprexorException;

class For implements sprexor.CommandFactory {
	Sprexor sp = null;
	boolean[] is;
	int cou;
	String[] tmpArr;
	boolean flag = false;
	
	public String getCommandName() {
		return "for";
	}
	public String help() {
		return "USAGE : for (txt | code) (count) COMMANDS...\n\n" +
				"";
	}
	@Override
	public IOCenter code(String[] args, boolean[] isWrapped, GlobalData scope, Sprexor sprex) {
		String op = args [ 0 ] ;
		switch ( op ) {
		case "txt" :
			if(args.length <= 2) return new IOCenter("");
			return new IOCenter ( Tools . arg2String ( Tools . cutArr ( args , 2 ) , " " ) . repeat ( Integer . parseInt ( args [ 1 ] ) ) , IOCenter . STDOUT ) ;
		case "code" :
			if(args.length <= 2) return new IOCenter("Argument length must be longer than 2.");
			sprex.call("entry_on");
			sp = sprex;
			is = isWrapped;
			cou = Integer.parseInt(args[1]);
			tmpArr = Tools.cutArr(args, 2);
			return new IOCenter("4");
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
	public Object EntryMode(String msg) throws CommandNotFoundException, SprexorException {
		if(flag) return "22";
		flag = true;
		if(msg == null) {
			flag = false;
			return msg;
		}
		if(msg.contentEquals("break")) {
			sp.call("entry_off");
			flag = false;
			return "3";
		}
		String[] swap = new String[tmpArr.length];
		int c = 0;
		for(String str : tmpArr) {
			if(!is[c] && str.contentEquals("break")) {
				sp.call("entry_off");
				flag = false;
				return "2";
			}
			swap[c] = str;
			c ++;
		}
		String tmp = Tools.arg2String(swap, " ");
		for(int i = 0; i < cou; i ++) {
			sp.exec(tmp);
		}
		flag = false;
		return msg;
	}
}
