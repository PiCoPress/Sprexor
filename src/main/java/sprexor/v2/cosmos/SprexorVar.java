package sprexor.v2.cosmos;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;
import sprexor.v2.IOCenter.TYPE;
import sprexor.v2.components.SCommand;
import sprexor.v2.components.SParameter;
import sprexor.v2.components.annotations.name;
import sprexor.v2.lib.Smt;
import sprexor.v2.lib.Utils;

@name("var")
public class SprexorVar implements SCommand {
	private static final String HELP = Smt.SMT_FORM("(!) : no action[n]"
			+ "usage : var [ACTION] ...[nnt]"
			+ "delete : delete variables[nnt]"
			+ "set : set variables, (ex) abc=1 def=\"123\" ...[nnt]"
			+ "help : show this message");

	@Override
	public int main(IOCenter io, SParameter args, SManager Environment) {
		if(!Environment.useVariableExpression()) { 
		io.out.setType(TYPE.ERR);
		io.out.println("can nont use Variable : disabled");
		return 1;
	}
	int leng_arg = args.length();
	if(leng_arg >= 1) switch(args.getElement(0)) {
	case "delete" :
		if(leng_arg < 2) return 0;
		String[] Names = (String[]) Utils.cutArr(args.getArrays(), 1);
		for(String strs : Names) {
			Environment.deleteVariable(strs);
		}
		break;
	
	case "set" :
		if(leng_arg < 2) return 0;
		String[] prm = (String[]) Utils.cutArr(args.getArrays(), 1);
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
		io.out.println(HELP);
		break;
		
	default : 
		io.out.printf("unknown param : %s\n", args.getElement(0));
		return 1;
	}
	else {
	io.out.println(HELP);
	}
	return 0;
	}

}
