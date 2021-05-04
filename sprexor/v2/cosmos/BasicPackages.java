package sprexor.v2.cosmos;

import sprexor.v2.components.SCommand;
import sprexor.v2.components.SFrame;
import sprexor.v2.lib.Utils;

/**
 * It is a Class to provide BasicPackages and to make CommandClass for who don't know.
 * @author PICOPress
 * @since 0.2.4
 */
public class BasicPackages implements SFrame{
	@Override
	public SCommand[] references() {
		//return a(SprexorFor.class, SprexorCmdlst.class, SprexorEcho.class, SprexorVar.class); //better way is call a constructor directly.
		return Utils.<SCommand>a(new SprexorFor(), new SprexorCmdlst(), new SprexorEcho(), new SprexorVar(), 
				new SprexorChangeD(), new SprexorMkdir(), new SprexorRm());
	}
	@Override
	public String getDescription() {
		return "";
	}
	@Override
	public String PackageName() {
		return "basic";
	}
}