package sprexor.cosmos;

import sprexor.IOCenter;
import sprexor.SCommand;
import sprexor.SParameter;
import sprexor.Sprexor;
import sprexor.IOCenter.TYPE;
import sprexor.lib.Smt;
import sprexor.lib.Utils;

public class SprexorVar implements SCommand {
	@Override
	public String name() {
		return "var";
	}

	@Override
	public int main(IOCenter io, Sprexor Environment) {
		if(!Environment.useVariableExpression()) { 
		io.out.setType(TYPE.ERR);
		io.out.println("can nont use Variable : disabled");
		return 1;
	}
	SParameter args = io.getComponent();
	int leng_arg = args.length();
	if(leng_arg >= 1) switch(args.gets(0)) {
	case "delete" :
		if(leng_arg < 2) return 0;
		String[] Names = (String[]) Utils.cutArr(args.get(), 1);
		for(String strs : Names) {
			Environment.deleteVariable(strs);
		}
		break;
	
	case "set" :
		if(leng_arg < 2) return 0;
		String[] prm = (String[]) Utils.cutArr(args.get(), 1);
		//var set a=7 a=9...
		try {
		for(String assignment : prm) {
			String[] kk = assignment.split("=");
			if(Environment.ExistsVariable(kk[0])) Environment.setVariable(kk[0], kk[1]);
			else Environment.putVariable(kk[0], kk[1]);
		}
		}catch(Exception e) {
			io.out.setType(TYPE.ERR);
			io.out.println("invalid declaring format");
		}
		break;
	
	case "help" :
		io.out.println(Smt.SMT_FORM("(!) : no action[n]"
				+ "usage : var [ACTION] ...[nnt]"
				+ "delete : delete variables[nnt]"
				+ "set : set variables, (ex) abc=1 def=\"123\" ...[nnt]"
				+ "help : show this message"));
		break;
		
	default : 
		io.out.printf("unknown param : %s\n", args.gets(0));
		return 1;
	}
	else {
	io.out.println(Smt.SMT_FORM("(!) : no action[n]"
			+ "usage : var [ACTION] ...[nnt]"
			+ "delete : delete variables[nnt]"
			+ "set : set variables, (ex) abc=1 def=\"123\" ...[nnt]"
			+ "help : show this message"));
	}
	return 0;
	}

}
