package sprexor.v2.components;

import sprexor.v2.IOCenter;
import sprexor.v2.SManager;

public interface SCommand {
	/**
	 * getCommandName defines command name as returned value.
	 * @return String : The name has only English is recommended.
	 */
	public abstract String name();
	/**
	 * Operate with java source overrided method.
	 * @param io : The processed parameter.
	 * @param Environment
	 * @return int exit code
	 */
	public abstract int main(IOCenter io, SManager Environment);
	public abstract String version();
}
