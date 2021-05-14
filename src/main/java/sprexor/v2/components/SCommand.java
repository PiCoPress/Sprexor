package sprexor.v2.components;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;

/**
 * It is for make a command used in Sprexor.
 */
public interface SCommand {
	/**
	 * Operate with java source overrided method.
	 * @param io : to use IO
	 * @param Environment
	 * @return int exit code
	 */
	public abstract int main(IOCenter io, SParameter args, SManager Environment);
}
