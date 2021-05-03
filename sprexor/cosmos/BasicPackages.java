package sprexor.cosmos;

import sprexor.SFrame;
import sprexor.SParameter;
import sprexor.SCommand;
import sprexor.Sprexor;
import sprexor.IOCenter;
import sprexor.lib.Utils;
import static sprexor.lib.Utils.a;

/**
 * It is a Class to provide BasicPackages and to make CommandClass for who don't know.
 * @author PICOPress
 * @since 0.2.4
 */
public class BasicPackages implements SFrame{
	@Override
	public SCommand[] references() {
		return a(For.class, SprexorCmdlst.class, SprexorEcho.class, SprexorVar.class); //better way is call a constructor directly.
	}
	
	public int main(IOCenter io, Sprexor SprexorInstance) {
		SParameter args = io.getComponent();
		String tmp = Utils.join((String[])Utils.excludeArr(args.get(), 0));
		String find = args.gets(0);
		String[] splitedStr = tmp.split("\n");
		String result = "";
		
		for(String it : splitedStr) {
			if(it.indexOf(find) != -1)result += it + "\n";
		}
		io.out.println(result);
		return 0;
	}

	@Override
	public String PackageName() {
		return "basic";
	}
}