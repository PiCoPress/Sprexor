package sprexor;

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
	public abstract int main(IOCenter io, Sprexor Environment);
}
